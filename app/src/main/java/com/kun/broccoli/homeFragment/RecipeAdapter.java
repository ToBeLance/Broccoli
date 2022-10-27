package com.kun.broccoli.homeFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kun.broccoli.CookBookApplication;
import com.kun.broccoli.R;
import com.kun.broccoli.dao.CookBookDao;
import com.kun.broccoli.model.cookbook.CookBook;
import com.kun.broccoli.model.cookbook.MakeProcess;
import com.kun.broccoli.model.cookbook.Material;
import com.kun.broccoli.model.cookbook.Recipe;
import com.kun.broccoli.model.cookbook.SingleCookBookSearchResponse;
import com.kun.broccoli.network.RecipeSearchByDetail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private List<CookBook> list;
    private Context context;

    private TextView name,peopleNum,prepareTime,cookingTime,content,tag,material;
    private ImageView pic;
    private LinearLayout recipeDetail;
    private ImageView collect;
    private CookBook cookBook;
    private int fragmentTag;
    public static final int HOMEFRAGMENT = 0;
    public static final int COLLECTFRAGMENT = 2;

    private int listFrom;
    public static final int SELFCREATE = 4;
    public static final int FROMAPI = 5;

    private AppCompatActivity compatActivity;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private View fragmentView;

    public RecipeAdapter(List<CookBook> list, Context context,int fragmentTag,int listFrom) {
        this.list = list;
        this.context = context;
        this.fragmentTag = fragmentTag;
        this.listFrom = listFrom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getName());
        holder.tag.setText("标签: " + list.get(position).getTag());
        holder.content.setText(list.get(position).getContent().replace("<br />",System.getProperty("line.separator")));
        if (listFrom == FROMAPI) {
            Glide.with(context)
                    .load(list.get(position).getPic())
                    .into(holder.pic);
        }
        //界面滑动很卡，图片加载的问题
//        if (listFrom == SELFCREATE) {
//            try {
//                File file = new File(context.getExternalCacheDir(),list.get(position).getPic());
//                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(getImageUri(file)));
//                bitmap = rotateIfRequired(bitmap, file);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 1, baos);
//                byte[] bytes=baos.toByteArray();
//                Glide.with(context)
//                        .load(bytes)
//                        .centerCrop()
//                        .into(holder.pic);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
        compatActivity = (AppCompatActivity) RecipeAdapter.this.context;
        fragmentManager = compatActivity.getSupportFragmentManager();
        if (fragmentTag == COLLECTFRAGMENT) {
            holder.delete.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentTag == HOMEFRAGMENT) {
                    fragment = fragmentManager.findFragmentByTag("f0");
                    fragmentView = fragment.getView();
                    LinearLayout homePage = fragmentView.findViewById(R.id.home_page);
                    TextView cancelTV = fragmentView.findViewById(R.id.home_tv_cancel);
                    cancelTV.setVisibility(View.VISIBLE);
                    homePage.setVisibility(View.GONE);
                    showDetailRecipe(position);
                }
                if (fragmentTag == COLLECTFRAGMENT) {
                    fragment = fragmentManager.findFragmentByTag("f2");
                    fragmentView = fragment.getView();
                    fragmentView.findViewById(R.id.other_recipe_rv).setVisibility(View.GONE);
                    fragmentView.findViewById(R.id.detail_recipe_page).setVisibility(View.VISIBLE);
                    showDetailRecipe(position);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                if (listFrom == FROMAPI) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (position<list.size())
                                CookBookApplication.cookBookDao.deleteCookBook(list.get(position));
                        }
                    }).start();
                    notifyDataSetChanged();
                    list.remove(position);
                }

                if (listFrom == SELFCREATE) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (position < list.size()) {
                                AppCompatActivity context = (AppCompatActivity) RecipeAdapter.this.context;
                                CookBook cookBook = list.get(position);
                                Recipe recipe = new Recipe(cookBook.getName(), cookBook.getPeoplenum(), cookBook.getPreparetime(), cookBook.getCookingtime(),
                                        cookBook.getContent(), cookBook.getPic(), cookBook.getTag());
                                CookBookApplication.recipeDao.deleteRecipe(recipe);
                                List<Material> material = CookBookApplication.materialDao.findMaterial(recipe.getName());
                                List<MakeProcess> process = CookBookApplication.processDao.findProcess(recipe.getName());
                                List<String> processIDList = new ArrayList<>();
                                for (int i = 0; i < material.size(); i++) {
                                    CookBookApplication.materialDao.deleteMaterial(material.get(i));
                                }
                                MakeProcess makeProcess;
                                for (int i = 0; i < process.size(); i++) {
                                    makeProcess = process.get(i);
                                    CookBookApplication.processDao.deleteProcess(makeProcess);
                                    processIDList.add(makeProcess.getProcessid());
                                }
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            File recipeIMGFile = new File(context.getExternalCacheDir(), "recipe_image_" + recipe.getName() + ".jpg");
                                            recipeIMGFile.delete();
                                            File processIMGFile;
                                            for (int i = 0; i < processIDList.size(); i++) {
                                                processIMGFile = new File(context.getExternalCacheDir(),"process_image" + processIDList.get(i) + ".jpg");
                                                processIMGFile.delete();
                                            }
                                            Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                    }).start();
                    notifyDataSetChanged();
                    list.remove(position);
                }
            }
        });



    }



    private void showDetailRecipe (int position) {
        CardView viewById = fragmentView.findViewById(R.id.detail_recipe_page);
        name = fragmentView.findViewById(R.id.name);
        peopleNum = fragmentView.findViewById(R.id.peoplenum);
        prepareTime = fragmentView.findViewById(R.id.preparetime);
        cookingTime = fragmentView.findViewById(R.id.cookingtime);
        tag = fragmentView.findViewById(R.id.tag);
        content = fragmentView.findViewById(R.id.content);
        material = fragmentView.findViewById(R.id.material);
        pic = fragmentView.findViewById(R.id.pic);
        recipeDetail = fragmentView.findViewById(R.id.recipe_detail);
        collect = fragmentView.findViewById(R.id.collect);
        if (listFrom == FROMAPI) {
            collect.setSelected(false);
            CookBookDao cookBookDao = CookBookApplication.cookBookDao;
            CookBookApplication.retrofit.create(RecipeSearchByDetail.class).getRecipeByDetail(list.get(position).getId()).enqueue(new Callback<SingleCookBookSearchResponse>() {
                @Override
                public void onResponse(Call<SingleCookBookSearchResponse> call, Response<SingleCookBookSearchResponse> response) {
                    cookBook = response.body().getResult().getCookBook();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (cookBookDao.findCookBook(cookBook.getId()) != null) {
                                compatActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        collect.setSelected(true);
                                    }
                                });
                            }
                        }
                    }).start();
                    collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (cookBookDao.findCookBook(cookBook.getId()) == null) {
                                        cookBookDao.insertCookBook(RecipeAdapter.this.cookBook);
                                        compatActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                collect.setSelected(true);
                                                Toast.makeText(context,"收藏 " + cookBook.getName() + " 菜谱",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        cookBookDao.deleteCookBook(cookBook);
                                        compatActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                collect.setSelected(false);
                                                Toast.makeText(context,"删除 " + cookBook.getName() + " 菜谱",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });
                    Glide.with(context)
                            .load(cookBook.getPic())
                            .into(pic);
                    name.setText(cookBook.getName());
                    peopleNum.setText("食用人数: " + cookBook.getPeoplenum());
                    prepareTime.setText("准备食材时间: " + cookBook.getPreparetime());
                    cookingTime.setText("食材烹饪时间: " + cookBook.getCookingtime());
                    tag.setText("标签: " + cookBook.getTag());
                    content.setText("菜谱描述: " + cookBook.getContent().replace("<br />",System.getProperty("line.separator")));
                    List<Material> materialList = cookBook.getMaterial();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < materialList.size(); i++) {
                        stringBuilder.append(materialList.get(i).getMname() + "\t\t\t\t" + materialList.get(i).getAmount() + "\n");
                    }
                    material.setText("材料准备: " + "\n" + stringBuilder.toString());

                    List<MakeProcess> process = cookBook.getProcess();
                    recipeDetail.removeAllViews();
                    for (int i = 0; i < process.size(); i++) {
                        TextView textView = new TextView(context);
                        textView.setTextSize(20);
                        textView.setText("\n" + process.get(i).getPcontent().replace("<br />",System.getProperty("line.separator")) + "\n");
                        ImageView imageView = new ImageView(context);
                        Glide.with(context)
                                .load(process.get(i).getPic())
                                .placeholder(R.drawable.placeholder_img)
                                .into(imageView);
                        recipeDetail.addView(textView);
                        recipeDetail.addView(imageView);
                    }
                    if (fragmentTag == HOMEFRAGMENT) {
                        fragment.getView().findViewById(R.id.rv_search_page).setVisibility(View.GONE);
                        viewById.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<SingleCookBookSearchResponse> call, Throwable t) {
                    Toast.makeText(context,"菜谱详情获取失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (listFrom == SELFCREATE) {
            collect.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String recipeName = list.get(position).getName();
                    Recipe recipe = CookBookApplication.recipeDao.findRecipe(recipeName);
                    List<Material> materialList = CookBookApplication.materialDao.findMaterial(recipeName);
                    List<MakeProcess> processList = CookBookApplication.processDao.findProcess(recipeName);
                    AppCompatActivity activity = (AppCompatActivity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File file = new File(context.getExternalCacheDir(),recipe.getPic());
                                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(getImageUri(file)));
                                bitmap = rotateIfRequired(bitmap, file);
                                pic.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            name.setText(recipe.getName());
                            peopleNum.setText("食用人数: " + recipe.getPeoplenum());
                            prepareTime.setText("准备食材时间: " + recipe.getPreparetime());
                            cookingTime.setText("食材烹饪时间: " + recipe.getCookingtime());
                            tag.setText("标签: " + recipe.getTag());
                            content.setText("菜谱描述: " + recipe.getContent());
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < materialList.size(); i++) {
                                stringBuilder.append(materialList.get(i).getMname() + "\t\t\t\t" + materialList.get(i).getAmount() + "\n");
                            }
                            material.setText("材料准备: " + "\n" + stringBuilder.toString());

                            for (int i = 0; i < processList.size(); i++) {
                                TextView textView = new TextView(context);
                                textView.setTextSize(20);
                                textView.setText("\n" + processList.get(i).getPcontent() + "\n");
                                ImageView imageView = new ImageView(context);
                                try {
                                    File file = new File(context.getExternalCacheDir(),processList.get(i).getPic());
                                    Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(getImageUri(file)));
                                    bitmap = rotateIfRequired(bitmap, file);
                                    imageView.setImageBitmap(bitmap);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                recipeDetail.addView(textView);
                                recipeDetail.addView(imageView);
                            }
                        }
                    });

                }
            }).start();
        }

    }

    private Uri getImageUri(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context,"com.kun.broccoli.fileprovider",file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private Bitmap rotateIfRequired(Bitmap bitmap, File file) {
        ExifInterface exifInterface;
        int orientation = 0;
        try {
            exifInterface = new ExifInterface(file.getPath());
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap,90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap,180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap,270);
            default:
                return bitmap;
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap,int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotatedBitmap;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,tag,content;
        ImageView pic;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipe_name);
            tag = itemView.findViewById(R.id.recipe_tag);
            content = itemView.findViewById(R.id.recipe_content);
            pic = itemView.findViewById(R.id.recipe_pic);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}

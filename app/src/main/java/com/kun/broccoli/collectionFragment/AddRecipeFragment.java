package com.kun.broccoli.collectionFragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kun.broccoli.CookBookApplication;
import com.kun.broccoli.R;
import com.kun.broccoli.homeFragment.RecipeAdapter;
import com.kun.broccoli.model.cookbook.CookBook;
import com.kun.broccoli.model.cookbook.MakeProcess;
import com.kun.broccoli.model.cookbook.Material;
import com.kun.broccoli.model.cookbook.Recipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText name,tag,peopleNum,prepareTime,cookingTime,content;
    private ImageView recipeImg;
    private ImageView takeRecipePhotoBtn;
    private EditText materialName,materialAmount;
    private ImageView addMaterial,removeMaterial;
    private EditText processContent;
    private ImageView processImage;
    private ImageView takeProcessPhotoBtn;
    private ImageView addProcess,removeProcess;
    private ImageView addRecipe;

    private LinearLayout materialLL,processLL;
    private TextView materialNameTV,materialAmountTV;
    private TextView processContentTV;
    private ImageView processImg;


    private List<TextView> materialNameTVList = new ArrayList<>(),materialAmountTVList = new ArrayList<>();

    private List<String> materialNameList = new ArrayList<>(),materialAmountList = new ArrayList<>();

    private List<TextView> processTVList = new ArrayList<>();
    private List<String> processList = new ArrayList<>();
    private List<ImageView> processImgList = new ArrayList<>();

    private Bitmap bitmap;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private File outputProcessImage;
    private List<String> outputProcessImageList = new ArrayList<>();
    private final static int TAKEPHOTO = 1;

    private Bitmap recipeBitmap;
    private File outputRecipeImage;
    private final static int RECIPETAKEPHONE = 3;

    private int materialid,processid;

    private List<String> allRecipeName = new ArrayList<>();
    private List<String> materialidList = new ArrayList<>(),processidList = new ArrayList<>();

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecipeFragment newInstance(String param1, String param2) {
        AddRecipeFragment fragment = new AddRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                materialid = CookBookApplication.materialDao.getAllMaterial().size() + 1;
                processid = CookBookApplication.processDao.getAllProcess().size() + 1;
                List<Recipe> allRecipe = CookBookApplication.recipeDao.getAllRecipe();
                Recipe recipe;
                allRecipeName.clear();
                for (int i = 0; i < allRecipe.size(); i++) {
                    recipe = allRecipe.get(i);
                    allRecipeName.add(recipe.getName());
                }
            }
        }).start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        name = view.findViewById(R.id.name);
        tag = view.findViewById(R.id.tag);
        peopleNum = view.findViewById(R.id.peoplenum);
        prepareTime = view.findViewById(R.id.preparetime);
        cookingTime = view.findViewById(R.id.cookingtime);
        content = view.findViewById(R.id.content);
        recipeImg = view.findViewById(R.id.recipe_img);
        takeRecipePhotoBtn = view.findViewById(R.id.takephoto_recipe);
        materialName = view.findViewById(R.id.materialname);
        materialAmount = view.findViewById(R.id.materialamount);
        addMaterial = view.findViewById(R.id.addmaterial);
        removeMaterial = view.findViewById(R.id.removematerial);
        processContent = view.findViewById(R.id.progresscontent);
        processImage = view.findViewById(R.id.progressimage);
        takeProcessPhotoBtn = view.findViewById(R.id.takephoto);
        addProcess = view.findViewById(R.id.addprogress);
        removeProcess = view.findViewById(R.id.removeprogress);
        addRecipe = view.findViewById(R.id.addrecipe);

        materialLL = view.findViewById(R.id.materialll);
        processLL = view.findViewById(R.id.progressll);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (allRecipeName.contains(name.getText().toString())) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"菜谱明已存在，请换名",Toast.LENGTH_SHORT).show();
                            name.setText("");
                        }
                    });
                } else {
                    outputRecipeImage = new File(getContext().getExternalCacheDir(),"recipe_image_"+ name.getText().toString() + ".jpg");
                    if (outputRecipeImage.exists()) {
                        outputRecipeImage.delete();
                    }
                    try {
                        outputRecipeImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        takeRecipePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().equals("")) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,getImageUri(outputRecipeImage));
                    startActivityForResult(intent,RECIPETAKEPHONE);
                } else {
                    Toast.makeText(getContext(),"请填写菜谱名字再拍照",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!materialName.getText().toString().equals("") && !materialAmount.getText().toString().equals("")) {
                    materialNameTV = new TextView(getContext());
                    materialNameTV.setText(materialName.getText());
                    materialAmountTV = new TextView(getContext());
                    materialAmountTV.setText(materialAmount.getText());
                    materialLL.addView(materialNameTV);
                    materialLL.addView(materialAmountTV);
                    materialNameTVList.add(materialNameTV);
                    materialAmountTVList.add(materialAmountTV);
                    materialNameList.add(materialName.getText().toString());
                    materialAmountList.add(materialAmount.getText().toString());
                    materialName.setText("");
                    materialAmount.setText("");
                    materialidList.add("" + materialid);
                    materialid++;
                } else {
                    Toast.makeText(getContext(),"食材填写不正确",Toast.LENGTH_SHORT).show();
                }
            }
        });

        removeMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (materialNameTVList.size() > 0 && materialAmountTVList.size() > 0) {
                    materialLL.removeView(materialNameTVList.get(materialNameTVList.size() - 1));
                    materialLL.removeView(materialAmountTVList.get(materialAmountTVList.size() - 1));
                    materialNameTVList.remove(materialNameTVList.size() - 1);
                    materialAmountTVList.remove(materialAmountTVList.size() - 1);
                    materialNameList.remove(materialNameList.size() - 1);
                    materialAmountList.remove(materialAmountList.size() - 1);
                    materialidList.remove(materialidList.size() - 1);
                    materialid--;
                } else {
                    Toast.makeText(getContext(),"无食材可删除",Toast.LENGTH_SHORT).show();
                }
            }
        });

        takeProcessPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputProcessImage = new File(getContext().getExternalCacheDir(),"process_image" + processid + ".jpg");
                if (outputProcessImage.exists()) {
                    outputProcessImage.delete();
                }
                try {
                    outputProcessImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,getImageUri(outputProcessImage));
                startActivityForResult(intent, TAKEPHOTO);
            }
        });

        addProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!processContent.getText().toString().equals("") &&
                        bitmap != null) {
                    processContentTV = new TextView(getContext());
                    processContentTV.setText(processContent.getText());
                    processImg = new ImageView(getContext());
                    processImg.setImageBitmap(bitmap);
                    processImg.setLayoutParams(new LinearLayout.LayoutParams(400,400));
                    processLL.addView(processContentTV);
                    processLL.addView(processImg);
                    processTVList.add(processContentTV);
                    processImgList.add(processImg);
                    bitmapList.add(bitmap);
                    processList.add(processContent.getText().toString());
                    outputProcessImageList.add(outputProcessImage.getName());
                    processContent.setText("");
                    processImage.setImageResource(R.drawable.placeholder_img);
                    processidList.add("" + processid);
                    processid++;
                } else {
                    Toast.makeText(getContext(),"烹饪过程填写不完整",Toast.LENGTH_SHORT).show();
                }

            }
        });

        removeProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (processTVList.size() > 0 && processImgList.size() > 0 && bitmapList.size() > 0) {
                    processLL.removeView(processTVList.get(processTVList.size() - 1));
                    processLL.removeView(processImgList.get(processImgList.size() - 1));
                    processTVList.remove(processTVList.size() - 1);
                    processImgList.remove(processImgList.size() - 1);
                    bitmapList.remove(bitmapList.size() - 1);
                    outputProcessImageList.remove(outputProcessImageList.size() - 1);
                    processidList.remove(processidList.size() - 1);
                    processid--;
                } else {
                    Toast.makeText(getContext(),"已经没有烹饪过程可删除",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().equals("") && !tag.getText().toString().equals("")
                        && !peopleNum.getText().toString().equals("")
                && !prepareTime.getText().toString().equals("") && !cookingTime.getText().toString().equals("")
                && !content.getText().toString().equals("") && materialNameTVList.size() > 0
                && materialAmountTVList.size() > 0 && processTVList.size() > 0
                && processImgList.size() > 0 && bitmapList.size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (allRecipeName.contains(name.getText().toString())) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"菜谱名重复，请改名",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            Recipe recipe = new Recipe(name.getText().toString(), peopleNum.getText().toString(), prepareTime.getText().toString(),
                                    cookingTime.getText().toString(), content.getText().toString()
                                    , outputRecipeImage.getName(), tag.getText().toString());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    CookBookApplication.recipeDao.insertRecipe(recipe);
                                }
                            }).start();
                            for (int i = 0; i < materialNameList.size(); i++) {
                                Material material = new Material(materialidList.get(i),name.getText().toString(),
                                        materialNameList.get(i),materialAmountList.get(i));
                                CookBookApplication.materialDao.insertMaterial(material);
                            }
                            for (int i = 0; i < processList.size(); i++) {
                                MakeProcess process = new MakeProcess(processidList.get(i),name.getText().toString(),
                                        processList.get(i), outputProcessImageList.get(i));
                                CookBookApplication.processDao.insertProcess(process);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"菜谱添加成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                            Fragment collectFragment = supportFragmentManager.findFragmentByTag("f2");
                            View collectFragmentView = collectFragment.getView();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    collectFragmentView.findViewById(R.id.add_recipe_fragment).setVisibility(View.GONE);
                                    RecyclerView recipeManagerRV = collectFragmentView.findViewById(R.id.other_recipe_rv);
                                    recipeManagerRV.setVisibility(View.VISIBLE);
                                    recipeManagerRV.setLayoutManager(new LinearLayoutManager(getContext()));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<Recipe> allRecipe = CookBookApplication.recipeDao.getAllRecipe();
                                            List<CookBook> cookBookList = new ArrayList<>();
                                            CookBook cookBook;
                                            Recipe tempRecipe;
                                            for (int i = 0; i < allRecipe.size(); i++) {
                                                tempRecipe = allRecipe.get(i);
                                                cookBook = new CookBook(null,null,tempRecipe.getName(),tempRecipe.getPeoplenum(),
                                                        tempRecipe.getPreparetime(), tempRecipe.getCookingtime(), tempRecipe.getContent(), tempRecipe.getPic(), tempRecipe.getTag());
                                                cookBookList.add(cookBook);
                                            }
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    recipeManagerRV.setAdapter(new RecipeAdapter(cookBookList,getContext(),RecipeAdapter.COLLECTFRAGMENT,RecipeAdapter.SELFCREATE));
                                                }
                                            });

                                        }
                                    }).start();

                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(getContext(),"菜谱未填写完整或正确",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKEPHOTO:
            {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(getImageUri(outputProcessImage)));
                        bitmap = rotateIfRequired(bitmap, outputProcessImage);
                        processImage.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case RECIPETAKEPHONE:
            {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        recipeBitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(getImageUri(outputRecipeImage)));
                        recipeBitmap = rotateIfRequired(recipeBitmap, outputRecipeImage);
                        recipeImg.setImageBitmap(recipeBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
            default:
                break;
        }
    }

    private Uri getImageUri(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getContext(),"com.kun.broccoli.fileprovider",file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private Bitmap rotateIfRequired(Bitmap bitmap,File file) {
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
}
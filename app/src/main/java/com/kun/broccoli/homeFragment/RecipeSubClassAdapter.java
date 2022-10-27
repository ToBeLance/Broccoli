package com.kun.broccoli.homeFragment;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kun.broccoli.CookBookApplication;
import com.kun.broccoli.R;
import com.kun.broccoli.dao.CookBookDao;
import com.kun.broccoli.model.cookbook.CookBook;
import com.kun.broccoli.model.cookbook.CookBooksSearchResponse;
import com.kun.broccoli.model.cookbook.MakeProcess;
import com.kun.broccoli.model.cookbook.Material;
import com.kun.broccoli.model.recipeClass.RecipeClass;
import com.kun.broccoli.model.recipeClass.RecipeClassResponse;
import com.kun.broccoli.model.recipeClass.RecipeDetailClass;
import com.kun.broccoli.network.RecipeClassService;
import com.kun.broccoli.network.RecipeSearchByClassService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSubClassAdapter extends RecyclerView.Adapter<RecipeSubClassAdapter.ViewHolder> {
    private List<RecipeDetailClass> recipeDetailClassList = new ArrayList<>();
    private Context context;

    public RecipeSubClassAdapter(List<RecipeDetailClass> recipeDetailClassList, Context context) {
        this.recipeDetailClassList = recipeDetailClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeSubClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_class_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSubClassAdapter.ViewHolder holder, int position) {
        RecipeDetailClass recipeDetailClass = recipeDetailClassList.get(position);
        holder.textView.setText(recipeDetailClass.getName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CookBookApplication.retrofit.create(RecipeSearchByClassService.class).getRecipeByClass(recipeDetailClass.getClassid()).enqueue(new Callback<CookBooksSearchResponse>() {
                    @Override
                    public void onResponse(Call<CookBooksSearchResponse> call, Response<CookBooksSearchResponse> response) {
                        List<CookBook> cookBookList = response.body().getResult().getResult().getList();

                        AppCompatActivity compatActivity = (AppCompatActivity) RecipeSubClassAdapter.this.context;
                        FragmentManager fragmentManager = compatActivity.getSupportFragmentManager();
                        Fragment fragment = fragmentManager.findFragmentByTag("f0");
                        View fragmentView = fragment.getView();
                        fragmentView.findViewById(R.id.home_page).setVisibility(View.GONE);
                        fragmentView.findViewById(R.id.home_tv_cancel).setVisibility(View.VISIBLE);
                        RecyclerView searchRV = fragmentView.findViewById(R.id.rv_search_page);
                        searchRV.setVisibility(View.VISIBLE);
                        searchRV.setAdapter(new RecipeAdapter(cookBookList,context,RecipeAdapter.HOMEFRAGMENT,RecipeAdapter.FROMAPI));
                    }

                    @Override
                    public void onFailure(Call<CookBooksSearchResponse> call, Throwable t) {
                        Toast.makeText(context,"菜谱详情获取失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeDetailClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_class);
        }
    }
}

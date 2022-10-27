package com.kun.broccoli.homeFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kun.broccoli.CookBookApplication;
import com.kun.broccoli.R;
import com.kun.broccoli.model.recipeClass.RecipeClass;
import com.kun.broccoli.model.recipeClass.RecipeClassResponse;
import com.kun.broccoli.network.RecipeClassService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeClassAdapter extends RecyclerView.Adapter<RecipeClassAdapter.ViewHolder> {
    private Context context;
    private List<String> classNameList = new ArrayList<>();
    private RecyclerView homeFragmentSubClassRV;
    private Fragment fragment;

    public RecipeClassAdapter(Context context) {
        Log.d("lance", "RecipeClassAdapter: ");
        this.context = context;
        classNameList = Arrays.asList("功效", "人群", "疾病", "体质", "菜系", "小吃", "菜品", "口味","加工工艺", "厨房用具", "场景", "其他");
        AppCompatActivity activity = (AppCompatActivity) this.context;
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        fragment = supportFragmentManager.findFragmentByTag("f0");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_class_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        View fragmentView = fragment.getView();
        homeFragmentSubClassRV = fragmentView.findViewById(R.id.rv_sub_class);
        getRecipeSubClass(0);
        holder.textView.setText(classNameList.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 11) {
                    Toast.makeText(context,"菜谱类型研究中...",Toast.LENGTH_SHORT).show();
                } else {
                    getRecipeSubClass(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return classNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_class);
        }
    }

    private void getRecipeSubClass(int position) {
        CookBookApplication.retrofit.create(RecipeClassService.class).getRecipeClass().enqueue(new Callback<RecipeClassResponse>() {
            @Override
            public void onResponse(Call<RecipeClassResponse> call, Response<RecipeClassResponse> response) {
                if (response.body().getResult() != null) {
                    List<RecipeClass> result = response.body().getResult().getResult();
                    homeFragmentSubClassRV.setAdapter(new RecipeSubClassAdapter(result.get(position).getList(),context));
                } else {
                    Toast.makeText(context,"每天请求次数超时",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RecipeClassResponse> call, Throwable t) {

            }
        });
    }
}

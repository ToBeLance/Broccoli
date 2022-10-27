package com.kun.broccoli;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kun.broccoli.dao.CookBookDao;
import com.kun.broccoli.dao.MaterialDao;
import com.kun.broccoli.dao.MyDatabase;
import com.kun.broccoli.dao.ProcessDao;
import com.kun.broccoli.dao.RecipeDao;
import com.kun.broccoli.homeFragment.RecipeClassAdapter;
import com.kun.broccoli.model.recipeClass.RecipeClass;
import com.kun.broccoli.model.recipeClass.RecipeClassResponse;
import com.kun.broccoli.network.RecipeClassService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CookBookApplication extends Application {
    public static Context context;
    public static String APPKEY = "ac3df7dc8589d4a2fac3ece1990f7c14";
    public static CookBookDao cookBookDao;
    public static MaterialDao materialDao;
    public static ProcessDao processDao;
    public static RecipeDao recipeDao;
    public static String userName;

    public static String BASE_URL = "https://way.jd.com/jisuapi/";
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static List<RecipeClass> recipeClassList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MyDatabase databaseInstance = MyDatabase.getDatabaseInstance(context);
        cookBookDao = databaseInstance.cookBookDao();
        materialDao = databaseInstance.materialDao();
        processDao = databaseInstance.processDao();
        recipeDao = databaseInstance.recipeDao();
        getRecipeClassList();
    }

    private void getRecipeClassList() {
        CookBookApplication.retrofit.create(RecipeClassService.class).getRecipeClass().enqueue(new Callback<RecipeClassResponse>() {
            @Override
            public void onResponse(Call<RecipeClassResponse> call, Response<RecipeClassResponse> response) {
                if (response.body().getResult() != null) {
                    recipeClassList = response.body().getResult().getResult();
                } else {
                    Toast.makeText(context,"每天请求次数超时",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeClassResponse> call, Throwable t) {
                Toast.makeText(context,"菜谱分类信息获取错误",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

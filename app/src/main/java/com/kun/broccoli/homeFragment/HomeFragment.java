package com.kun.broccoli.homeFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kun.broccoli.CookBookApplication;
import com.kun.broccoli.R;
import com.kun.broccoli.model.cookbook.CookBook;
import com.kun.broccoli.model.cookbook.CookBooksSearchResponse;
import com.kun.broccoli.model.cookbook.SingleCookBookSearchResponse;
import com.kun.broccoli.model.recipeClass.RecipeClass;
import com.kun.broccoli.model.recipeClass.RecipeClassResponse;
import com.kun.broccoli.model.recipeClass.RecipeDetailClass;
import com.kun.broccoli.network.RecipeClassService;
import com.kun.broccoli.network.RecipeSearchByClassService;
import com.kun.broccoli.network.RecipeSearchByDetail;
import com.kun.broccoli.network.RecipeSearchByNameService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private EditText searchEditText;
    private TextView cancelTextView;
    private RecyclerView searchRecyclerView,recipeClassRV,recipeSubClassRV,recipeCommendRV;
    private CardView detailRecipeCardView;
    private LinearLayout home_pageLL;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        searchEditText = rootView.findViewById(R.id.et_search);
        cancelTextView = rootView.findViewById(R.id.home_tv_cancel);

        searchRecyclerView = rootView.findViewById(R.id.rv_search_page);

        detailRecipeCardView = rootView.findViewById(R.id.detail_recipe_page);

        recipeClassRV = rootView.findViewById(R.id.rv_class);
        recipeSubClassRV = rootView.findViewById(R.id.rv_sub_class);
        recipeCommendRV = rootView.findViewById(R.id.rv_recommend_recipe);
        home_pageLL = rootView.findViewById(R.id.home_page);
        recipeClassRV.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeClassRV.setAdapter(new RecipeClassAdapter(getContext()));
        recipeSubClassRV.setLayoutManager(new GridLayoutManager(getContext(),3));

        recipeCommendRV.setLayoutManager(new LinearLayoutManager(getContext()));
        showRandomRecipe();

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(textView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        startSearch();
                    }
                    return true;
                }
                return false;
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTextView.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.GONE);

                home_pageLL.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

    private void startSearch() {
        CookBookApplication.retrofit.create(RecipeSearchByNameService.class).RecipeSearchByName(searchEditText.getText().toString()).enqueue(new Callback<CookBooksSearchResponse>() {
            @Override
            public void onResponse(Call<CookBooksSearchResponse> call, Response<CookBooksSearchResponse> response) {
                if (response.body().getResult() != null) {
                    List<CookBook> cookBookList = response.body().getResult().getResult().getList();
                    searchRecyclerView.setAdapter(new RecipeAdapter(cookBookList,getContext(),RecipeAdapter.HOMEFRAGMENT,RecipeAdapter.FROMAPI));
                } else {
                    Toast.makeText(getContext(),"每天请求次数超时",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CookBooksSearchResponse> call, Throwable t) {
                Toast.makeText(getContext(),"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
        home_pageLL.setVisibility(View.GONE);

        searchRecyclerView.setVisibility(View.VISIBLE);
        searchEditText.setText("");
        cancelTextView.setVisibility(View.VISIBLE);
    }



    private void showRandomRecipe() {

        CookBookApplication.retrofit.create(RecipeSearchByClassService.class).getRecipeByClass("10").enqueue(new Callback<CookBooksSearchResponse>() {
            @Override
            public void onResponse(Call<CookBooksSearchResponse> call, Response<CookBooksSearchResponse> response) {
                if (response.body().getResult() != null)
                    recipeCommendRV.setAdapter(new RecipeAdapter(response.body().getResult().getResult().getList(), getContext(),RecipeAdapter.HOMEFRAGMENT,RecipeAdapter.FROMAPI));
                else
                    Toast.makeText(getContext(),"请求次数超时",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CookBooksSearchResponse> call, Throwable t) {

            }
        });
    }
}
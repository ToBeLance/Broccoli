package com.kun.broccoli.collectionFragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kun.broccoli.CookBookApplication;
import com.kun.broccoli.R;
import com.kun.broccoli.homeFragment.RecipeAdapter;
import com.kun.broccoli.model.cookbook.CookBook;
import com.kun.broccoli.model.cookbook.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private RecyclerView recipeManagerRV;
    private TextView otherRecipeTV,mineRecipeTV,addMineRecipeTV;
    private CardView detailRecipePage;
    private FrameLayout addRecipeFragment;

    public CollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionFragment newInstance(String param1, String param2) {
        CollectionFragment fragment = new CollectionFragment();
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
        rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        recipeManagerRV = rootView.findViewById(R.id.other_recipe_rv);
        otherRecipeTV = rootView.findViewById(R.id.other_recipe_tv);
        mineRecipeTV = rootView.findViewById(R.id.mine_recipe_tv);
        addMineRecipeTV = rootView.findViewById(R.id.add_mine_recipe_tv);
        detailRecipePage = rootView.findViewById(R.id.detail_recipe_page);
        addRecipeFragment = rootView.findViewById(R.id.add_recipe_fragment);


        recipeManagerRV.setLayoutManager(new LinearLayoutManager(getContext()));
        getOtherRecipe();


        otherRecipeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOtherRecipe();
                recipeManagerRV.setVisibility(View.VISIBLE);
                detailRecipePage.setVisibility(View.GONE);
                addRecipeFragment.setVisibility(View.GONE);
            }
        });
        mineRecipeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMineRecipe();
                recipeManagerRV.setVisibility(View.VISIBLE);
                detailRecipePage.setVisibility(View.GONE);
                addRecipeFragment.setVisibility(View.GONE);
            }
        });
        addMineRecipeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecipeFragment.setVisibility(View.VISIBLE);
                recipeManagerRV.setVisibility(View.GONE);
                detailRecipePage.setVisibility(View.GONE);
                FragmentManager childFragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.add_recipe_fragment,AddRecipeFragment.newInstance("1","1"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOtherRecipe();
    }

    private void getMineRecipe() {
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

    private void getOtherRecipe() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CookBook> allCookBook = CookBookApplication.cookBookDao.getAllCookBook();
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recipeManagerRV.setAdapter(new RecipeAdapter(allCookBook,getContext(),RecipeAdapter.COLLECTFRAGMENT,RecipeAdapter.FROMAPI));
                    }
                });
            }
        }).start();
    }
}
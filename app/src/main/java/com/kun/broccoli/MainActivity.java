package com.kun.broccoli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kun.broccoli.collectionFragment.CollectionFragment;
import com.kun.broccoli.homeFragment.HomeFragment;
import com.kun.broccoli.personFragment.PersonFragment;
import com.kun.broccoli.videoFragment.VideoFragment;

import java.nio.channels.Selector;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager2 viewPager2;
    ImageView ivCurrent,ivHome,ivVideo,ivCollect,ivPerson;
    LinearLayout llHome,llVideo,llCollect,llPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        ivCurrent = ivHome;
        ivCurrent.setSelected(true);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("1","1"));
        fragments.add(VideoFragment.newInstance("1","1"));
        fragments.add(CollectionFragment.newInstance("1","1"));
        fragments.add(PersonFragment.newInstance("1","1"));

        viewPager2.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),getLifecycle(),fragments));
        viewPager2.setCurrentItem(0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeTab(position);
            }

        });
    }

    private void initView() {
        viewPager2 = findViewById(R.id.vp2);
        ivHome = findViewById(R.id.tab_iv_home);
        ivVideo = findViewById(R.id.tab_iv_video);
        ivCollect = findViewById(R.id.tab_iv_collect);
        ivPerson = findViewById(R.id.tab_iv_person);
        llHome = findViewById(R.id.id_tab_home);
        llVideo = findViewById(R.id.id_tab_video);
        llCollect = findViewById(R.id.id_tab_collect);
        llPerson = findViewById(R.id.id_tab_person);
        llHome.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llCollect.setOnClickListener(this);
        llPerson.setOnClickListener(this);
    }

    private void changeTab(int position) {
        ivCurrent.setSelected(false);
        switch (position) {
            case R.id.id_tab_home:
            case 0:
                viewPager2.setCurrentItem(0);
                ivHome.setSelected(true);
                ivCurrent = ivHome;
                break;
            case R.id.id_tab_video:
            case 1:
                viewPager2.setCurrentItem(1);
                ivVideo.setSelected(true);
                ivCurrent = ivVideo;
                break;
            case R.id.id_tab_collect:
            case 2:
                viewPager2.setCurrentItem(2);
                ivCollect.setSelected(true);
                ivCurrent = ivCollect;
                break;
            case R.id.id_tab_person:
            case 3:
                viewPager2.setCurrentItem(3);
                ivPerson.setSelected(true);
                ivCurrent = ivPerson;
                break;
        }
    }

    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }
}
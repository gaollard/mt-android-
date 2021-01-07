package com.airtlab.news.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airtlab.news.R;
import com.airtlab.news.adapter.MyPagerAdapter;
import com.airtlab.news.entity.TabEntity;
import com.airtlab.news.fragment.HomeFragment;
import com.airtlab.news.fragment.MyFragment;
import com.airtlab.news.fragment.NewsFragment;
import com.airtlab.news.fragment.ProjectFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {
    // 底部Tab标题配置
    private String[] mTitles = {"首页", "大厅", "资讯", "我的"};
    // 底部按钮未选中状态图标
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect,
            R.mipmap.collect_unselect,
            R.mipmap.collect_unselect,
            R.mipmap.my_unselect
    };
    // 底部Tab选中状态图标
    private int[] mIconSelectIds = {
            R.mipmap.home_selected,
            R.mipmap.collect_selected,
            R.mipmap.collect_selected,
            R.mipmap.my_selected
    };
    // 底部Tab Fragment
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    // 底部Tab 每一项的数据配置
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    // ViewPager 容器
    private ViewPager viewPager;
    // Tab 容器
    private CommonTabLayout commonTabLayout;

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.viewpager);
        commonTabLayout = findViewById(R.id.commonTabLayout);
    }

    @Override
    protected void initData() {
        // mFragments 初始化
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(ProjectFragment.newInstance());
        mFragments.add(NewsFragment.newInstance());
        mFragments.add(MyFragment.newInstance());

        // mTabEntities 初始化
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                // 点击底部按钮
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) { }
        });

        // 设置预先加载 Fragment，防止快速切换BUG
        viewPager.setOffscreenPageLimit(mFragments.size());
        // ViewPager 设置监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 给 ViewPager 对象设置适配器
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
    }
}
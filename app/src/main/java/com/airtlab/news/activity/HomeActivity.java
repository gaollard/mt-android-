package com.airtlab.news.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airtlab.news.R;
import com.airtlab.news.adapter.MyPagerAdapter;
import com.airtlab.news.entity.TabEntity;
import com.airtlab.news.fragment.HomeFragment;
import com.airtlab.news.fragment.MyFragment;
import com.airtlab.news.fragment.NewsFragment;
import com.airtlab.news.fragment.ProjectFragment;
import com.airtlab.news.fragment.PublishFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {
    private LinearLayout l_tab;
    private String selectedColor = "#6200EE";
    private String unSelectedColor = "#888888";
    private String[] mTitles = {"大厅", "发现", "", "消息", "我的"};
    // 底部按钮未选中状态图标
    private int[] mIconUnselectIds = {
            R.mipmap.home_o,
            R.mipmap.discovery_o,
            R.drawable.ic_add,
            R.mipmap.message_o,
            R.mipmap.account_o
    };
    // 底部Tab选中状态图标
    private int[] mIconSelectIds = {
            R.mipmap.home,
            R.mipmap.discovery,
            R.drawable.ic_circle_add,
            R.mipmap.message,
            R.mipmap.account
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
        l_tab = findViewById(R.id.l_tab);
        initTabbar();
    }

    private void clickMTab(int i) {;
        Log.e("hhh", String.valueOf(i));
        for (int p = 0; p < l_tab.getChildCount(); p++) {
            if (p != 2) {
                LinearLayout l = (LinearLayout)l_tab.getChildAt(p);
                ImageView imageView = (ImageView)l.getChildAt(0);
                TextView textView = (TextView)l.getChildAt(1);
                textView.setTextColor(Color.parseColor(unSelectedColor));
                imageView.setImageResource(mIconUnselectIds[p]);
                imageView.setColorFilter(Color.parseColor(unSelectedColor));
                Log.e("text" + String.valueOf(p), textView.getText().toString());
            }
        }
        if (i != 2) {
            LinearLayout l = (LinearLayout)l_tab.getChildAt(i);
            ImageView imageView = (ImageView)l.getChildAt(0);
            TextView textView = (TextView)l.getChildAt(1);
            imageView.setImageResource(mIconSelectIds[i]);
            textView.setTextColor(Color.parseColor(selectedColor));
            imageView.setColorFilter(Color.parseColor(selectedColor));
            Log.e("text" + String.valueOf(i), textView.getText().toString());
        }
    }

    private void initTabbar() {
        clickMTab(0);
        for (int i = 0; i < l_tab.getChildCount(); i++) {
            if (i != 2) {
                LinearLayout l = (LinearLayout)l_tab.getChildAt(i);
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i = l_tab.indexOfChild(view);
                        clickMTab(i);
                        viewPager.setCurrentItem(i);
                    }
                });
            } else {
                LinearLayout l = (LinearLayout)l_tab.getChildAt(i);
//                ImageView imageView = (ImageView)l.getChildAt(0);
//                imageView.setColorFilter(R.color.colorPink);
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(mContext, PublishActivity.class);
                        startActivity(in);
                    }
                });
            }
        }
    }

    @Override
    protected void initData() {
        // mFragments 初始化
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(ProjectFragment.newInstance());
        mFragments.add(PublishFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        mFragments.add(MyFragment.newInstance());

        // mTabEntities 初始化
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        // 设置预先加载 Fragment，防止快速切换BUG
        viewPager.setOffscreenPageLimit(mFragments.size());

        // ViewPager 设置监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                clickMTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 给 ViewPager 对象设置适配器
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
    }
}
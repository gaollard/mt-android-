package com.airtlab.news.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    // 底部Tab标题配置
//    RadioGroup
    private RadioGroup m_tabbar;
    private String[] mTitles = {"大厅", "发现", "", "消息", "我的"};
    // 底部按钮未选中状态图标
    private int[] mIconUnselectIds = {
            R.mipmap.home_o,
            R.mipmap.discovery_o,
            R.drawable.ic_add,
            R.mipmap.message_o,
            R.mipmap.my_unselect
    };
    // 底部Tab选中状态图标
    private int[] mIconSelectIds = {
            R.mipmap.home,
            R.mipmap.discovery,
            R.drawable.ic_circle_add,
            R.mipmap.message,
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
        m_tabbar = findViewById(R.id.m_tabbar);
        initTabbar();
    }

    private void initTabbar() {
        m_tabbar.removeAllViews();
        for (int i = 0; i < mTitles.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(mTitles[i]);
            rb.setTextColor(R.color.black);
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            // Drawable
            Drawable drawableFirst = getResources().getDrawable(R.mipmap.home_o);
            drawableFirst.setBounds(0, 0, 50, 50); //第一0是距左右边距离，第二0是距上下边距离，第三50长度,第四宽度50
            rb.setCompoundDrawables(null, drawableFirst, null, null);//只放上面

            // 布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.weight = 1;
            rb.setLayoutParams(layoutParams);

            m_tabbar.addView(rb);
        }
    }

    @Override
    protected void initData() {
        // mFragments 初始化
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(ProjectFragment.newInstance());
        mFragments.add(PublishFragment.newInstance());
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
                if (position != 2) {
                    viewPager.setCurrentItem(position);
                } else {
                    Intent in = new Intent(mContext, PublishActivity.class);
                    startActivity(in);
                }
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
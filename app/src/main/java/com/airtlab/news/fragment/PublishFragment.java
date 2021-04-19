package com.airtlab.news.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airtlab.news.R;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig2;
import com.airtlab.news.entity.CityEntity;
import com.airtlab.news.entity.CityResponseEntity;
import com.airtlab.news.entity.ProjectCategory;
import com.airtlab.news.entity.ProjectCategoryListResponse;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @desc 发布悬赏项目 or 发布帖子
 */
public class PublishFragment extends BaseFragment {
    private ArrayList<ProjectCategory> projectCategoryList;
    private ArrayList<CityEntity> cityList;
    private int[] selectIndex = new int[3];

    public static PublishFragment newInstance() {
        PublishFragment fragment = new PublishFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_publish;
    }

    @Override
    protected void initView() {
        mRootView.findViewById(R.id.choose_project_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectTypePicker();
            }
        });
        mRootView.findViewById(R.id.choose_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityPicker();
            }
        });
    }

    @Override
    protected void initData() {
        this.getCateData();
        this.getCityData();
    }

    /**
     * @desc 获取项目类型列表
     */
    private void getCateData() {
        HashMap<String, Object> params = new HashMap<>();
        Api2.config(ApiConfig2.project_category, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                ProjectCategoryListResponse response = new Gson().fromJson(res, ProjectCategoryListResponse.class);
                if (response != null && response.errCode.equals("0")) {
                    projectCategoryList = response.data.list;
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e(getActivity().getClass().getName(), e.toString());
            }
        });
    }

    /**
     * @desc 项目类型 picker item data
     */
    class ProjectTypeBean  implements IPickerViewData {
        public int id;
        public String name;
        @Override
        public String getPickerViewText() {
            return name;
        }
    }

    /**
     * @desc city picker item data
     */
    class CityBean implements IPickerViewData {
        public String areaId;
        public String areaName;
        public String getPickerViewText() {
            return areaName;
        }
    }

    /**
     * @desc 显示选择项目类型弹窗
     */
    public void showProjectTypePicker() {
        ArrayList<ProjectTypeBean> projectTypeItems = new ArrayList<>();
        for (int i = 0; i < projectCategoryList.size(); i++) {
            ProjectTypeBean bean = new ProjectTypeBean();
            bean.id = projectCategoryList.get(i).id;
            bean.name = projectCategoryList.get(i).name;
            projectTypeItems.add(bean);
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int p1, int p2, int p3, View v) {
                TextView tv = mRootView.findViewById(R.id.project_type_value);
                tv.setText(projectTypeItems.get(p1).name);
            }
        })
                .setLabels("省", "市", "区")
                .setTitleText("选择项目类型")
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(2)
                .build();
        pvOptions.setPicker(projectTypeItems);
        pvOptions.show();

    }
    /**
     * @desc 显示选择项目类型弹窗
     */
    public void showCityPicker() {
        ArrayList<CityBean> provinceItems = new ArrayList<>();
        ArrayList<ArrayList<CityBean>> citItems = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<CityBean>>> districtItems = new ArrayList<>();

        // province
        for (int i = 0; i < cityList.size(); i++) {
            CityBean bean = new CityBean();
            bean.areaId = cityList.get(i).areaId;
            bean.areaName = cityList.get(i).areaName;
            provinceItems.add(bean);
        }

        // city
        for (int i = 0; i < cityList.size(); i++) {
            ArrayList<CityBean> beans = new ArrayList<>();
            for (int j = 0; j < cityList.get(i).children.size(); j++) {
                CityEntity city = cityList.get(i).children.get(j);
                CityBean bean = new CityBean();
                bean.areaId = city.areaId;
                bean.areaName = city.areaName;
                beans.add(bean);
            }
            citItems.add(beans);
        }

        // district
        for (int i = 0; i < cityList.size(); i++) {
            ArrayList<CityEntity> children_1 = cityList.get(i).children;
            if (children_1 == null) {
                children_1 = new ArrayList<>();
            }
            ArrayList<ArrayList<CityBean>> beans_1 = new ArrayList<>();
            for (int j = 0; j < children_1.size(); j++) {
                ArrayList<CityEntity> children_2 = children_1.get(j).children;
                if (children_2 == null) {
                    children_2 = new ArrayList<>();
                }
                ArrayList<CityBean> beans_2 = new ArrayList<>();
                for (int k = 0; k < children_2.size(); k++) {
                    CityBean bean = new CityBean();
                    bean.areaId = children_2.get(k).areaId;
                    bean.areaName = children_2.get(k).areaName;
                    beans_2.add(bean);
                }
                beans_1.add(beans_2);
            }
            districtItems.add(beans_1);
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int p1, int p2, int p3, View v) {
                selectIndex[0] = p1;
                selectIndex[1] = p2;
                selectIndex[2] = p3;

                TextView tv = mRootView.findViewById(R.id.city_value);
                String province = provinceItems.get(p1).areaName;
                String city = citItems.get(p1).get(p2).areaName;
                String district = "";
                if (districtItems.get(p1).get(p2).size() != 0) {
                    district = districtItems.get(p1).get(p2).get(p3).areaName;
                }
                tv.setText(province + city + district);
            }
        })
                .setTitleText("选择地址")
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(2)
                .build();
        pvOptions.setPicker(provinceItems, citItems, districtItems);
        if (selectIndex.length != 0) {
            pvOptions.setSelectOptions(selectIndex[0], selectIndex[1], selectIndex[2]);
        }
        pvOptions.show();
    }

    /**
     * @desc 获取城市数据
     */
    public void getCityData() {
        HashMap<String, Object> params = new HashMap<>();
        Api2.config(ApiConfig2.sfCity, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                CityResponseEntity response = new Gson().fromJson(res, CityResponseEntity.class);
                if (response != null && response.errCode.equals("0")) {
                    cityList = response.data.list;
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e(getActivity().getClass().getName(), e.toString());
            }
        });
    }
}
package com.airtlab.news.fragment;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.airtlab.news.R;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig2;
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
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PublishFragment extends BaseFragment {
    private ArrayList<ProjectCategory> projectCategoryList;

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

    }

    @Override
    protected void initData() {
        Log.e("PublishFragment", "initData");
        this.getCateData();
//        this.getCityData();
    }

    /**
     * @desc 获取项目类型列表
     */
    private void getCateData() {
        Log.e("start", "111");
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
     * @desc 显示选择项目类型弹窗
     */{
        ArrayList<ProjectTypeBean> projectTypeItems = new ArrayList<>();
        for (int i = 0; i < projectCategoryList.size(); i++) {
            ProjectTypeBean bean = new ProjectTypeBean();
            bean.id = projectCategoryList.get(i).id;
            bean.name = projectCategoryList.get(i).name;
            projectTypeItems.add(bean);
        }
        // 条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
            }
        })
                .setLabels("省", "市", "区")
                .setTitleText("城市选择")
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(2)
                .build();
        pvOptions.setPicker(projectTypeItems);
        pvOptions.show();
    }
    private void showProjectCatePicker()

    /**
     * @desc 获取城市数据
     */
    public void getCityData() {
        HashMap<String, Object> params = new HashMap<>();
        Api2.config(ApiConfig2.sfCity, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                CityResponseEntity response = new Gson().fromJson(res, CityResponseEntity.class);
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
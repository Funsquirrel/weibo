package com.fy.weibo.presenter;

import android.util.Log;

import com.fy.weibo.activity.UserActivity;
import com.fy.weibo.base.BasePresenter;
import com.fy.weibo.bean.UserCounts;
import com.fy.weibo.bean.UserInfo;
import com.fy.weibo.contract.UserCountContract;
import com.fy.weibo.model.UserCountModel;
import java.util.Map;

/**
 * Created by Fan on 2018/8/24.
 * Fighting!!!
 */
public final class UserCountPresenter extends BasePresenter<UserCountContract.UserCountModel, UserActivity> {

    @Override
    public void onFailure(String error) {
        Log.e("TAG", "错误信息:" + error);
        iView.showError(error);
    }




    public void loadUserCount() {

        UserInfo userInfo = iModel.getUserCount("", );
        iView.setUser(userInfo);
    }

    @Override
    protected UserCountContract.UserCountModel getModel() {
        return new UserCountModel();
    }

/*
    @Override
    public void loadUserCount(String baseUrl, Map<String, String> params) {
        iModel.getUserCount(baseUrl, params, this);
    }

    @Override
    public void onSuccess(UserCounts userCounts) {
        iView.setUserCount(userCounts);
    }

    @Override
    protected UserCountContract.UserCountModel getModel() {
        return new UserCountModel();
    }
*/
}

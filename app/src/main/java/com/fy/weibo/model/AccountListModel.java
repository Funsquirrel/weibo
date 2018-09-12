package com.fy.weibo.model;

import android.util.Log;

import com.fy.weibo.bean.SimpleUser;
import com.fy.weibo.contract.AccountContract;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.util.DataBaseHelper;
import com.fy.weibo.util.HttpUtil;
import com.fy.weibo.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by Fan on 2018/9/10.
 * Fighting!!!
 */
public class AccountListModel implements AccountContract.AccountModel {


    @Override
    public void getAccountList(String url, AccountContract.AccountContractPresenter presenter) {

        List<String[]> tokenInfoList = DataBaseHelper.getDataBaseHelper().getTokenInfoList();
        List<Map<String, String>> paramsList = new ArrayList<>();
        List<SimpleUser> userList = new ArrayList<>();
        for (int i = 0; i < tokenInfoList.size(); i++) {
            Map<String, String> params = new HashMap<>();
            params.put("access_token", tokenInfoList.get(i)[0]);
            params.put("uid", tokenInfoList.get(i)[1]);
            Log.e("TAG", "access_token---" + tokenInfoList.get(i)[0] + "     " + "uid----" + tokenInfoList.get(i)[1]);
            paramsList.add(params);
        }

        Runnable runnable = () -> {
            for (int i = 0; i < paramsList.size(); i++) {
                ResponseBody responseBody = HttpUtil.getHttpUtil().SyncGET(Constants.GET_USERS_SHOW, paramsList.get(i));
                try {
                    String json = responseBody.string();
                    Log.e("TAG", "这是用户信息------" + json);
                    SimpleUser user = JsonUtil.getSimpleUserInfo(json);
                    user.setToken(paramsList.get(i).get("access_token"));
                    user.setUid(paramsList.get(i).get("uid"));
                    if (user.getScreen_name() != null && !user.getScreen_name().equals("")) {
                        userList.add(user);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            presenter.onSuccess(userList);
        };

        Thread loadAccountThread = new Thread(runnable);
        loadAccountThread.start();
    }
}


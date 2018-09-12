package com.fy.weibo.presenter;

import android.util.Log;

import com.fy.weibo.base.BasePresenter;
import com.fy.weibo.bean.SimpleUser;
import com.fy.weibo.contract.AccountContract;
import com.fy.weibo.model.AccountListModel;

import java.util.List;

/**
 * Created by Fan on 2018/9/10.
 * Fighting!!!
 */
public class AccountListPresenter extends AccountContract.AccountContractPresenter {



    @Override
    protected AccountContract.AccountModel getModel() {
        return new AccountListModel();
    }

    @Override
    public void onFailure(String e) {
        Log.e("TAG", "出错了");
    }

    @Override
    public void loadAccount(String url) {
        iModel.getAccountList(url, this);
    }

    @Override
    public void onSuccess(List<SimpleUser> userList) {
        iView.setAccountList(userList);
    }
}

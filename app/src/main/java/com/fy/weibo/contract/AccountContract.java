package com.fy.weibo.contract;

import com.fy.weibo.base.BasePresenter;
import com.fy.weibo.bean.SimpleUser;
import com.fy.weibo.bean.UserInfo;
import com.fy.weibo.interfaces.IBaseView;
import com.fy.weibo.interfaces.IModel;

import java.util.List;

/**
 * Created by Fan on 2018/9/9.
 * Fighting!!!
 */
public interface AccountContract {

    abstract class AccountContractPresenter extends BasePresenter<AccountContract.AccountModel, AccountContract.AccountView>{

        public abstract void loadAccount(String url);
        public abstract void onSuccess(List<SimpleUser> userList);
    }

    interface AccountModel extends IModel{
        void getAccountList(String url, AccountContract.AccountContractPresenter presenter);
    }

    interface AccountView extends IBaseView<AccountContractPresenter> {
        void setAccountList(List<SimpleUser> userList);
    }
}

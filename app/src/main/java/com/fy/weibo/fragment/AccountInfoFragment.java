package com.fy.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fy.weibo.APPManager;
import com.fy.weibo.R;
import com.fy.weibo.activity.LoginActivity;
import com.fy.weibo.activity.MainActivity;
import com.fy.weibo.adapter.AccountListAdapter;
import com.fy.weibo.base.BaseMVPFragment;
import com.fy.weibo.bean.SimpleUser;
import com.fy.weibo.contract.AccountContract;
import com.fy.weibo.presenter.AccountListPresenter;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.util.NetStateUtil;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.List;
import java.util.Objects;

/**
 * Created by Fan on 2018/9/9.
 * Fighting!!!
 */
public final class AccountInfoFragment extends BaseMVPFragment<AccountContract.AccountContractPresenter> implements AccountContract.AccountView {


    private List<SimpleUser> userList;
    private Button exitButton;
    private RecyclerView accountRecyclerView;
    private AccountListAdapter accountListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void initPresenter() {
        mPresenter = getPresenter();
        mPresenter.attachMV(this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.account_layout;
    }

    @Override
    public void initAllMembersView(Bundle saveInstanceState) {

        ((MainActivity)mActivity).floatButton.setVisibility(View.INVISIBLE);
        exitButton = mRootView.findViewById(R.id.exit_account_button);
        exitButton.setOnClickListener(view -> {
            AccessTokenKeeper.writeAccessToken(mActivity, new Oauth2AccessToken());
            startActivity(new Intent(mActivity, LoginActivity.class));
            Constants.USER_ACCOUNT = "";
            Constants.USER_PASSWORD = "";
            APPManager.getInstance().finishAllActivity();
        });
        accountRecyclerView = mRootView.findViewById(R.id.account_recycler_view);
        DividerItemDecoration decoration = new DividerItemDecoration(getAttachActivity(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getAttachActivity(), R.drawable.item_decoration)));
        accountRecyclerView.addItemDecoration(decoration);
        LinearLayoutManager lmr = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        accountRecyclerView.setLayoutManager(lmr);
        swipeRefreshLayout = mRootView.findViewById(R.id.account_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.orangered);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void loadData() {
        if (NetStateUtil.checkNet(mActivity)) {
            swipeRefreshLayout.setRefreshing(true);
            mPresenter.loadAccount(Constants.GET_USERS_SHOW);
        } else Toast.makeText(mActivity, "无网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAccountList(List<SimpleUser> userList) {
        mActivity.runOnUiThread(() -> {
            this.userList = userList;
            accountListAdapter = new AccountListAdapter(mActivity, this.userList);
            accountRecyclerView.setAdapter(accountListAdapter);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void showError(String e) {
        Log.e("TAG", "错误信息----" + e);
    }

    @Override
    public AccountContract.AccountContractPresenter getPresenter() {
        return new AccountListPresenter();
    }
}

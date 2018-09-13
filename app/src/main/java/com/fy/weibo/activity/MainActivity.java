package com.fy.weibo.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fy.weibo.APPManager;
import com.fy.weibo.base.BaseMVPActivity;
import com.fy.weibo.R;
import com.fy.weibo.bean.UserInfo;
import com.fy.weibo.contract.UserInfoContract;

import com.fy.weibo.fragment.AccountInfoFragment;
import com.fy.weibo.fragment.MentionViewPagerFragment;
import com.fy.weibo.fragment.CommentViewPagerFragment;
import com.fy.weibo.fragment.WeiBoViewPagerFragment;
import com.fy.weibo.presenter.UserInfoPresenter;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.util.NetStateUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;


public final class MainActivity extends BaseMVPActivity<UserInfoContract.
        UserInfoContractPresenter> implements UserInfoContract.UserInfoView,WbShareCallback {

    private DrawerLayout drawerLayout;
    private CircleImageView userImg;
    private TextView userName;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    public FloatingActionButton floatButton;
    public Toolbar toolbar;
    private WbShareHandler shareHandler;
    private TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.main_layout;
    }

    @Override
    public void initView() {

        APPManager.getInstance().finishBeforeActivity();
        if (!NetStateUtil.checkNet(this))
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
        toolbar = findViewById(R.id.tool_bar);
        textView = findViewById(R.id.tool_bar_title);
        drawerLayout = findViewById(R.id.drawer_layout);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        NavigationView navigationView = findViewById(R.id.design_nav_view);
        floatButton = findViewById(R.id.float_button);
        setSupportActionBar(toolbar);
        userName = findViewById(R.id.nav_user_name);
        userImg = findViewById(R.id.nav_head_img);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setTitle("");
        }
        textView.setText("微 博");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.first_page);
        transaction.replace(R.id.main_frame, new WeiBoViewPagerFragment());
        transaction.commit();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.first_page:
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_frame, new WeiBoViewPagerFragment());
                    transaction.commit();
                    textView.setText("微 博");
                    drawerLayout.closeDrawers();
                    break;
                case R.id.my_comment:
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_frame, new CommentViewPagerFragment());
                    transaction.commit();
                    textView.setText("我 的 评 论");
                    drawerLayout.closeDrawers();
                    break;
                case R.id.message:
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_frame, new MentionViewPagerFragment());
                    transaction.commit();
                    textView.setText("提 到 我");
                    drawerLayout.closeDrawers();
                    break;
                case R.id.account:
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_frame, new AccountInfoFragment());
                    transaction.commit();
                    textView.setText("账 号 管 理");
                    drawerLayout.closeDrawers();
                default:
                    break;
            }
            return true;
        });

        floatButton.setOnClickListener(view -> {
            if (NetStateUtil.checkNet(this)){
                initWeibo();
                sendMultiMessage();
            } else {
                Toast.makeText(this,"请检查网络状态",Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public UserInfoPresenter getPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public void initPresenter() {
        mPresenter = getPresenter();
        mPresenter.attachMV(this);
    }

    @Override
    public void loadData() {
        loadUserInfo();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
            this.finish();
        }
    }

    @Override
    public void loadUserInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", Constants.ACCESS_TOKEN);
        params.put("uid", Constants.UID);
        mPresenter.loadUserInfo(Constants.GET_USERS_SHOW, params);
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        this.runOnUiThread(() -> {

            userName = findViewById(R.id.nav_user_name);
            userImg = findViewById(R.id.nav_head_img);
//            Log.e("TAG", "这是在MainActivity()用户数据" + userInfo.getScreen_name());
            userName.setText(userInfo.getScreen_name());

            RelativeLayout relativeLayout = findViewById(R.id.nav_back_ground);
            RequestOptions options = new RequestOptions()
                    .placeholder(new ColorDrawable(Color.WHITE))
                    .centerCrop();
            Glide.with(this)
                    .load(userInfo.getProfile_image_url())
                    .apply(options)
                    .into(userImg);

            Glide.with(this)
                    .load(userInfo.getCover_image_phone())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            relativeLayout.setBackground(resource);
                        }
                    });

            userImg.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("user", userInfo);
                startActivity(intent);
            });
        });

    }

    /**
     * 微博分享
     *
     *
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
    }


    private void initWeibo() {
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);
    }
    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage() {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.imageObject = new ImageObject();
        weiboMessage.textObject = new TextObject();

        shareHandler.shareMessage(weiboMessage, false);
    }

    @Override
    public void onWbShareSuccess() {
        Toast.makeText(this,"分享成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWbShareFail() {
        Toast.makeText(this,"分享失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWbShareCancel() {
        Toast.makeText(this,"分享取消", Toast.LENGTH_LONG).show();
    }

}

/*
微博展示界面
 */
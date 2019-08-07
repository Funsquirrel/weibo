package com.fy.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fy.weibo.APPManager;
import com.fy.weibo.R;
import com.fy.weibo.base.BaseActivity;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.sdk.Oauth;
import com.fy.weibo.util.NetStateUtil;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;


public final class StartActivity extends BaseActivity {


    private boolean isSign = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {

        new Handler().postDelayed(this::LoginApp, 2000);
//        Log.e("TAG", "loginApp---uid" + AccessTokenKeeper.readAccessToken(this).getUid());
//        Log.e("TAG", "loginApp--token" + AccessTokenKeeper.readAccessToken(this).getToken());
//        Log.e("TAG", "login----" + Thread.currentThread().getName());
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
        Constants.ACCESS_TOKEN = token.getToken();
        Constants.UID = token.getUid();
        if (!Constants.ACCESS_TOKEN.equals("") && NetStateUtil.checkNet(this)) {
            isSign = true;
            Oauth oauth = new Oauth(this);
            oauth.isOauth();
        }

    }



    private void LoginApp() {

        if (!Constants.ACCESS_TOKEN.equals("") && !isSign) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else if (Constants.ACCESS_TOKEN.equals("") && !isSign){
//            Log.e("TAG", NetStateUtil.checkNet(this) + "网络状态");
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

    }


}

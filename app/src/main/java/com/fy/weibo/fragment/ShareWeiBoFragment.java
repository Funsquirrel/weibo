package com.fy.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fy.weibo.APPManager;
import com.fy.weibo.R;
import com.fy.weibo.activity.LoginActivity;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.sdk.Oauth;
import com.fy.weibo.sdk.WeiBoShare;
import com.fy.weibo.util.UserState;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Fan on 2018/8/18.
 * Fighting!!!
 */
public class ShareWeiBoFragment extends Fragment implements View.OnClickListener{


    private WeiBoShare share;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.share_frag_layout, container, false);
        share = new WeiBoShare(getActivity());
        Button textButton = view.findViewById(R.id.send_text);
        Button imgButton = view.findViewById(R.id.send_img);
        Button videoButton = view.findViewById(R.id.send_video);
        Button removeOauthButton = view.findViewById(R.id.sign_out);
        textButton.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        removeOauthButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send_text:
                share.sendWeiBo();
                break;
            case R.id.send_img:
                break;
            case R.id.send_video:
                break;
            case R.id.sign_out:
                Constants.ACCESS_TOKEN = "";
                if (getActivity() != null)
                    UserState.setUserStateNull(getActivity());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                APPManager.getInstance().finishAllActivity();
                break;
            default:
                break;
        }
    }
}

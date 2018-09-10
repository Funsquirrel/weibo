package com.fy.weibo.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.fy.weibo.App;
import com.fy.weibo.activity.ContentActivity;
import com.fy.weibo.bean.TokenInfo;
import com.fy.weibo.interfaces.LoadListener;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.util.HttpUtil;
import com.fy.weibo.util.JsonUtil;
import com.fy.weibo.util.NetStateUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by Fan on 2018/9/9.
 * Fighting!!!
 */
public final class SendCommentModel {

    public static void sendComment(Activity activity, String url, Map<String, String> info) {

        HttpUtil.getHttpUtil().post(url,info,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(() -> {
                    Toast.makeText(activity, "评论失败", Toast.LENGTH_SHORT).show();
                    activity.finish();
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                activity.runOnUiThread(() -> Toast.makeText(activity, "评论成功", Toast.LENGTH_SHORT).show());
            }
        } );
    }
}

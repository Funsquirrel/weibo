package com.fy.weibo.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.fy.weibo.util.HttpUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Fan on 2018/9/9.
 * Fighting!!!
 */
public final class SendCommentModel {

    public static void sendComment(Activity activity, String url, Map<String, String> info) {

        HttpUtil.getHttpUtil().AsyncPost(url,info,new Callback() {
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

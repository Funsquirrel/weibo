package com.fy.weibo.util;

import com.fy.weibo.bean.Comments;
import com.fy.weibo.bean.SimpleUser;
import com.fy.weibo.bean.TokenInfo;
import com.fy.weibo.bean.UserInfo;
import com.fy.weibo.bean.WeiBo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fan on 2018/7/31.
 * Fighting!!!
 */
public final class JsonUtil {


    // 微博
    public static List<WeiBo> getWeiBo(String json) {

        List<WeiBo> lastedWeiBo = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("statuses");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject weiBoContent = jsonArray.getJSONObject(i);
                Gson gson = new Gson();
                WeiBo lastedWeiBoBean = gson.fromJson(weiBoContent.toString(), WeiBo.class);
                lastedWeiBo.add(lastedWeiBoBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lastedWeiBo;
    }
//  评论
    public static List<Comments> getComments(String json) {

        List<Comments> commentList = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            if (jsonArray.length() == 0) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject commentContent = jsonArray.getJSONObject(i);
                Gson gson = new Gson();
                Comments comments = gson.fromJson(commentContent.toString(), Comments.class);
                commentList.add(comments);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentList;
    }

    public static TokenInfo get_token_info(String json) {

        TokenInfo tokenInfo = null;
        Gson gson = new Gson();
        tokenInfo = gson.fromJson(json, TokenInfo.class);
        return tokenInfo;
    }

    public static UserInfo getUserInfo(String json) {

        Gson gson = new Gson();
        return gson.fromJson(json, UserInfo.class);
    }

    public static SimpleUser getSimpleUserInfo(String json) {

        Gson gson = new Gson();
        return gson.fromJson(json, SimpleUser.class);
    }

}

/*
json 工具类
 */
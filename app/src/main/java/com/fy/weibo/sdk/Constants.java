package com.fy.weibo.sdk;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Fan on 2018/7/24.
 * Fighting!!!
 */
public final class Constants {

    public static final String APP_KEY = "1659988100";
    public static  String UID = "";
    public static String USER_ACCOUNT = "";
    public static String USER_PASSWORD = "";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static  String ACCESS_TOKEN = "";//token改动 ybc
    public static final String SCOPE = null;
    public static final String TAG = "TAG";
    // post 请求
//    public static final String ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";
    // post 请求
    public static final String GET_TOKEN_INFO = "https://api.weibo.com/oauth2/get_token_info";
    public static final String GET_PUBLIC_WEI_BO = "https://api.weibo.com/2/statuses/public_timeline.json";
    public static final String GET_ATTENTION_WEIBO = "https://api.weibo.com/2/statuses/home_timeline.json";
    public static final String GET_COMMENT = "https://api.weibo.com/2/comments/show.json";
    public static final String POST_REMOVE_TOKEN = "https://api.weibo.com/oauth2/revokeoauth2";
    public static final String GET_USERS_SHOW = "https://api.weibo.com/2/users/show.json";
    public static final String GET_USERS_COUNTS = "https://api.weibo.com/2/users/counts.json";
    public static final String GET_BY_ME_COMMENTS = "https://api.weibo.com/2/comments/by_me.json";
    public static final String GET_TO_ME_COMMENTS = "https://api.weibo.com/2/comments/to_me.json";
    public static final String GET_COMMENT_MENTION = "https://api.weibo.com/2/comments/mentions.json";
    public static final String GET_WEIBO_MENTION = "https://api.weibo.com/2/statuses/mentions.json";
    public static final String POST_WEIBO_COMMENT = "https://api.weibo.com/2/comments/create.json";
}

/*
常量类 weibo开放的 API  还有应用的注册信息
 */
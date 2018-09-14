package com.fy.weibo.listener;

/**
 * Created by Fan on 2018/8/21.
 * Fighting!!!
 */
public interface LoadListener<T> {

    void onSuccess(T data);
    void onFailure(String e);
}

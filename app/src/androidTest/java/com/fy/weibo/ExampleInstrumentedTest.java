package com.fy.weibo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.fy.weibo.sdk.Constants;
import com.fy.weibo.util.HttpUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.fy.weibo", appContext.getPackageName());
    }

    @Test
    public void UserInfoTest() {

        Map<String, String> params = new HashMap<>();
        params.put("access_token", "2.00YhYe2GcjI2oB4edd81f93bzqRdkD");
        params.put("screen_name", "光合F");
        HttpUtil.getData(Constants.GET_COMMENT_MENTION, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().string();
                Log.e("TAG", json);
            }
        });
    }


}

package com.fy.weibo;

import java.util.List;

/**
 * Created by Fan on 2018/8/18.
 * Fighting!!!
 */
public class JsonTest {


    private List<PicUrlsBean> pic_urls;

    public List<PicUrlsBean> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<PicUrlsBean> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public static class PicUrlsBean {
        /**
         * thumbnail_pic : http://wx2.sinaimg.cn/thumbnail/006BWJAJly1fueaq0o2s9j33me2exb2e.jpg
         */

        private String thumbnail_pic;

        public String getThumbnail_pic() {
            return thumbnail_pic;
        }

        public void setThumbnail_pic(String thumbnail_pic) {
            this.thumbnail_pic = thumbnail_pic;
        }
    }
}

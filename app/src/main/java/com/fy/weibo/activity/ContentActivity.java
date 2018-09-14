package com.fy.weibo.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fy.weibo.R;
import com.fy.weibo.adapter.CommentsAdapter;
import com.fy.weibo.adapter.WeiBoImgAdapter;
import com.fy.weibo.base.BaseMVPActivity;
import com.fy.weibo.bean.Comments;
import com.fy.weibo.bean.PicUrlsBean;
import com.fy.weibo.bean.WeiBo;
import com.fy.weibo.contract.CommentContract;
import com.fy.weibo.listener.HideListener;
import com.fy.weibo.listener.ScrollViewListener;
import com.fy.weibo.model.SendCommentModel;
import com.fy.weibo.presenter.CommentsPresenter;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.util.NetStateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public final class ContentActivity extends BaseMVPActivity<CommentContract.CommentContractPresenter> implements CommentContract.CommentView, View.OnClickListener {

    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comments> commentsList;
    private SwipeRefreshLayout refreshLayout;
    private Button commentBtn;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_layout;
    }

    @Override
    public void initView() {
        ImageView backImage = findViewById(R.id.back_image);
        backImage.setOnClickListener(this);
        refreshLayout = findViewById(R.id.content_refresh);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.orangered);
        refreshLayout.setOnRefreshListener(() -> {
            loadComments();
            if (!NetStateUtil.checkNet(this))
                refreshLayout.setRefreshing(false);
        });
        recyclerView = findViewById(R.id.comment_content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.comment_decoration)));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setNestedScrollingEnabled(false);
        scrollView = findViewById(R.id.content_scroll_view);
        setScrollListener();
    }


    @Override
    public void initPresenter() {
        mPresenter = getPresenter();
        mPresenter.attachMV(this);
    }


    public void initData() {
        WeiBo weiBo = (WeiBo) getIntent().getSerializableExtra("weibo");
        TextView weiBoText = this.findViewById(R.id.wei_bo_content_text);
        CircleImageView userImage = this.findViewById(R.id.user_img);
        TextView weiBoTime = findViewById(R.id.send_time);
        TextView weiBoSource = findViewById(R.id.wei_bo_source);
        TextView userName = findViewById(R.id.user_name);
        TextView commentCounts = findViewById(R.id.comment_counts);
        TextView shareCounts = findViewById(R.id.share_counts);
        TextView thumbUpCounts = findViewById(R.id.thumb_up_counts);
        commentBtn = findViewById(R.id.comment_btn);//评论点击处，
        weiBoText.setMaxLines(100);
        weiBoText.setText(weiBo.getText());
        RequestOptions options = new RequestOptions().placeholder(new ColorDrawable(Color.WHITE));
        Glide.with(this)
                .load(weiBo.getUser().getProfile_image_url())
                .apply(options)
                .into(userImage);
        weiBoTime.setText(weiBo.getCreated_at());
        weiBoSource.setText(weiBo.getSource());
        userName.setText(weiBo.getUser().getScreen_name());
        commentCounts.setText(weiBo.getComments_count());
        shareCounts.setText(weiBo.getReposts_count());
        thumbUpCounts.setText(weiBo.getAttitudes_count());
        List<PicUrlsBean> imgUrls = weiBo.getPic_urls();
        if (imgUrls != null) {
            List<String> urls = new ArrayList<>();
            for (PicUrlsBean url : imgUrls) {
                urls.add(url.getThumbnail_pic().replaceFirst("thumbnail", "bmiddle"));
            }
            RecyclerView imgRecyclerView = findViewById(R.id.wei_bo_img_recycler);
            imgRecyclerView.setNestedScrollingEnabled(false);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            imgRecyclerView.setLayoutManager(gridLayoutManager);
            WeiBoImgAdapter adapter = new WeiBoImgAdapter(this, urls);
            imgRecyclerView.setAdapter(adapter);
        }
        commentBtn.setOnClickListener(this);//评论点击事件
    }


    public void loadData() {
        loadComments();
    }

    @Override
    public void showError(String e) {
        this.runOnUiThread(() -> {
            Toast.makeText(this, e, Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        });
    }

    @Override
    public CommentContract.CommentContractPresenter getPresenter() {
        return new CommentsPresenter();
    }


    @Override
    public void loadComments() {
        refreshLayout.setRefreshing(true);
        if (!NetStateUtil.checkNet(this))
            refreshLayout.setRefreshing(false);
        WeiBo weiBo = (WeiBo) getIntent().getSerializableExtra("weibo");
        String strId = weiBo.getIdstr();
        Map<String, String> params = new HashMap<>();
        params.put("access_token", Constants.ACCESS_TOKEN);
        params.put("id", strId);
        mPresenter.loadComments(Constants.GET_COMMENT, params);

    }

    @Override
    public void setComments(List<Comments> comments) {
        runOnUiThread(() -> {
            this.commentsList = comments;
            commentsAdapter = new CommentsAdapter(ContentActivity.this, commentsList);
            recyclerView.setAdapter(commentsAdapter);
            refreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_btn:
                WeiBo userInfo = (WeiBo) getIntent().getSerializableExtra("weibo");
                String id = userInfo.getIdstr();
                showPopupWindow(ContentActivity.this, R.layout.comment_popupwindow, Constants.ACCESS_TOKEN, id);
                break;
            case R.id.back_image:
                this.finish();
                break;
            default:
                break;
        }
    }


    //评论窗口部分 以详细界面的comment图标进行点击评论
    private void showPopupWindow(Context context, int resource, String access_token, String id) {
        View view = View.inflate(context, resource, null);
        final PopupWindow popupWindow = new PopupWindow(view);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Button cancel = view.findViewById(R.id.btn_cancel);
        Button ok = view.findViewById(R.id.btn_confirm);
        final EditText editText = view.findViewById(R.id.dialog_edit);
        View.OnClickListener listener = view1 -> {
            switch (view1.getId()) {
                case R.id.btn_cancel:
                    popupWindow.dismiss();
                    break;
                case R.id.btn_confirm:
                    String CommentText = String.valueOf(editText.getText());
                    if (CommentText.length() < 140) {
                        Map<String, String> info = new HashMap<>();
                        info.put("access_token", access_token);
                        info.put("id", id);
                        info.put("comment", CommentText);
                        SendCommentModel.sendComment(this, Constants.POST_WEIBO_COMMENT, info);
                    } else {
                        Toast.makeText(ContentActivity.this, "字数超限", Toast.LENGTH_SHORT).show();
                    }
                    popupWindow.dismiss();
                    break;
                default:
                    break;
            }
        };
        cancel.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    private void setScrollListener() {

        scrollView.setOnScrollChangeListener(new ScrollViewListener(new HideListener() {
            @Override
            public void hide() {
                commentBtn.animate().translationY(commentBtn.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator());
            }

            @Override
            public void show() {
                commentBtn.animate().translationY(0).setInterpolator(new DecelerateInterpolator());
            }
        }));
    }


}

/*

详情展示界面
 */
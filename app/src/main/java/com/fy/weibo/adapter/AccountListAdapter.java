package com.fy.weibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fy.weibo.R;
import com.fy.weibo.bean.SimpleUser;
import com.fy.weibo.sdk.Constants;
import com.fy.weibo.sdk.Oauth;
import com.fy.weibo.util.NetStateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fan on 2018/9/10.
 * Fighting!!!
 */
public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {


    private List<SimpleUser> userList;
    private Context context;

    public AccountListAdapter(Context context, List<SimpleUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.account_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SimpleUser account = userList.get(position);
        RequestOptions options = new RequestOptions()
                .placeholder(new ColorDrawable(Color.WHITE))
                .centerCrop();
        Glide.with(context)
                .load(account.getProfile_image_url())
                .apply(options)
                .into(holder.accountImage);

        holder.accountName.setText(account.getScreen_name());
        holder.layout.setOnClickListener(view -> {
            Constants.UID = account.getUid();
            Constants.ACCESS_TOKEN = account.getToken();
            if (NetStateUtil.checkNet(context))
                new Oauth((Activity) context).isOauth();
            else Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView accountImage;
        TextView accountName;
        RelativeLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            accountImage = itemView.findViewById(R.id.account_image);
            accountName = itemView.findViewById(R.id.account_name);
            layout = itemView.findViewById(R.id.account_layout);
        }
    }
}

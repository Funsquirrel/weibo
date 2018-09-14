package com.fy.weibo.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fy.weibo.R;
import com.fy.weibo.util.MD5Helper;
import com.fy.weibo.util.NetStateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


/**
 * Created by Fan on 2018/8/18.
 * Fighting!!!
 */
public final class WeiBoImgAdapter extends RecyclerView.Adapter<WeiBoImgAdapter.ViewHolder> {

    private Context context;
    private Drawable drawable;
    private List<String> imgUrlList;

    public WeiBoImgAdapter(Context context, List<String> imgUrlList) {
        this.context = context;
        this.imgUrlList = imgUrlList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.weibo_img_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imgUrl = imgUrlList.get(position);
        loadImage(holder.imageView, imgUrl);
        String largeUrl = imgUrl.replaceFirst("bmiddle", "large");
        View diaImageLayout = LayoutInflater.from(context).inflate(R.layout.dia_image_layout, null);
        ImageView diaImageView = diaImageLayout.findViewById(R.id.dia_image_view);
        TextView saveText = diaImageLayout.findViewById(R.id.save_text);
        Dialog imageDia = new Dialog(context, R.style.ImageDiaView);
        imageDia.setContentView(diaImageLayout);
        setImageDia(imageDia);
        imageDia.setCanceledOnTouchOutside(true);
        imageDia.setCancelable(true);
//        assert imageDia.getWindow() != null;
//        imageDia.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        holder.imageView.setOnClickListener((view) -> {
            if (NetStateUtil.checkNet(context)) {
                imageDia.show();
            } else Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
        });

        loadImage(diaImageView, largeUrl);

        saveText.setOnClickListener((view) -> {
            Toast.makeText(context, "保存图片", Toast.LENGTH_SHORT).show();
            saveImage(MD5Helper.string2MD5(largeUrl) + ".png", drawable);
        });

        diaImageView.setOnClickListener((v) -> imageDia.cancel());


    }

    @Override
    public int getItemCount() {
        return imgUrlList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.weibo_img_item);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            Display display = ((Activity) context).getWindow().getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            int mWidth = displayMetrics.widthPixels / 3;
            layoutParams.width = mWidth;
            layoutParams.height = mWidth;
        }
    }


    private void loadImage(ImageView imageView, String imageUrl) {

        RequestOptions options = new RequestOptions()
                .placeholder(new ColorDrawable(Color.WHITE));
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                        drawable = resource;
                    }
                });
    }

    private void setImageDia(Dialog dia) {

        Window window = dia.getWindow();
        assert window != null;
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
    }

    private void saveImage(String fileName, Drawable drawable) {
        String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "weibo" + File.separator + "图片";
//        String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Pictures" + File.separator + "weibo";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File imageDir = new File(imagePath);
            if (!imageDir.exists())
                imageDir.mkdirs();
            File imageFile = new File(imagePath, fileName);

            try {
                FileOutputStream fos = new FileOutputStream(imageFile);

                ((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}

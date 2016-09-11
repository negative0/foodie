package com.fireblaze.evento.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fireblaze.evento.R;
import com.fireblaze.evento.models.ImageItem;

/**
 * Created by chait on 8/27/2016.
 */

public class ImageItemHolder extends RecyclerView.ViewHolder {
    private ImageView imageRes;
    public ImageItemHolder(View itemView) {
        super(itemView);
        imageRes = (ImageView) itemView.findViewById(R.id.item_image);
    }
    public void bindToPost(Context context,ImageItem imageItem){
        Glide.with(context).load(imageItem.getResourceURL()).error(R.drawable.logo_black).into(imageRes);
    }
}
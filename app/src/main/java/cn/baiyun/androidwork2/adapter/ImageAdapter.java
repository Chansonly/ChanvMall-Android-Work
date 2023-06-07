package cn.baiyun.androidwork2.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private List<Integer> list;
    private Context context;

    public ImageAdapter(List<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public ImageAdapter() {
        super();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //图片id
        Integer id = list.get(i);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(id);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(800,600));
        return imageView;
    }
}


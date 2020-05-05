package com.example.cozy.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cozy.MainActivity;
import com.example.cozy.R;
import com.example.cozy.model.SliderModel;
import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context mContext;
    private List<SliderModel> sliderModelList;

    public SliderAdapter(Context context, List<SliderModel> sliderModelList) {
        this.mContext = context;
        this.sliderModelList = sliderModelList;
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    private int[] imageSlide = new int[]{
        R.drawable.iklan1, R.drawable.iklan2, R.drawable.banner_5
    };

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout, container, false);
        ImageView imageView = view.findViewById(R.id.bannerSlide);
        imageView.setImageResource(sliderModelList.get(position).getBanner());
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
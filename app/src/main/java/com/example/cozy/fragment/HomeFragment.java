package com.example.cozy.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.cozy.R;
import com.example.cozy.adapter.SliderAdapter;
import com.example.cozy.model.SliderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private ViewPager bannerViewPager;
    private LinearLayout dotsLayout;
    private int currentPosSlider = 0, sizeSlider;
    private Timer timer;
    final private long DELAY_TIME = 1000;
    final private long PERIOD_TIME = 3000;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /* Banner Slider */
        bannerViewPager = view.findViewById(R.id.banner_container);
        dotsLayout = view.findViewById(R.id.dots_container);

        setBannerSlideShow();
        loopBannerSlideShow();

        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int il) { }

            @Override
            public void onPageSelected(int i) {
                currentPosSlider = i;
//                prepareDots(currentPosSlider);
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
        bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stopBannerSlideShow();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    loopBannerSlideShow();
                }
                return false;
            }
        });
        /* End of Banner Slider */

        return view;
    }

    /* Banner Slider */
    private void setBannerSlideShow() {
        List<SliderModel> sliderModelList = new ArrayList<>();

        sliderModelList.add(new SliderModel(R.drawable.iklan1));
        sliderModelList.add(new SliderModel(R.drawable.iklan2));
        sliderModelList.add(new SliderModel(R.drawable.banner_5));

        SliderAdapter sliderAdapter = new SliderAdapter(getActivity(), sliderModelList);
        bannerViewPager.setAdapter(sliderAdapter);

        // Get slider adapter size
        sizeSlider = sliderAdapter.getCount();
    }
    private void loopBannerSlideShow(){
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPosSlider > sizeSlider - 1) {
                    currentPosSlider = 0;
                }
//                prepareDots(currentPosSlider);
                bannerViewPager.setCurrentItem(currentPosSlider++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }
    private void stopBannerSlideShow() {
        timer.cancel();
    }
    private void limitBannerSlideShow(ViewPager bannerSlider) {
        if (currentPosSlider == sizeSlider - 1) {
            currentPosSlider = 0;
            bannerSlider.setCurrentItem(currentPosSlider, false);
        }
        if (currentPosSlider == 0) {
            currentPosSlider = sizeSlider - 1;
            bannerSlider.setCurrentItem(currentPosSlider, false);
        }
    }
    private void prepareDots(int position) {
        if (dotsLayout!=null) {
            dotsLayout.removeAllViews();
        }

        ImageView[] dots = new ImageView[sizeSlider];

        for (int i = 0; i < sizeSlider; i++) {
            dots[i] = new ImageView(getContext());

            if (i == position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_active));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_inactive));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 0, 5, 0);
            dotsLayout.addView(dots[i], layoutParams);
        }
    }
    /* End of Banner Slider */

}

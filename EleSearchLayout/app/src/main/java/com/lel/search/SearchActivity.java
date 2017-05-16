package com.lel.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lel.search.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding mBinding;
    private int mWidth,mCurrWidth,mHeight,mCurrHeight;
    private int mLeftMargin,mCurrLeftMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWidth = getIntent().getIntExtra("width",0);
        mHeight = getIntent().getIntExtra("height",0);
        mLeftMargin = getIntent().getIntExtra("leftMargin",0);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        inputAnimator(mBinding);
        mBinding.btnNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputAnimator(mBinding);
            }
        });
    }



    /**
     * 进场动画
     * @param viewBinding
     */
    public void inputAnimator(final ActivitySearchBinding viewBinding){
        viewBinding.rvList.getBackground().setAlpha(0);
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewBinding.searchContainer.getLayoutParams();
        final LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) viewBinding.layoutSearch.getLayoutParams();
        final LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) viewBinding.superContainer.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat( 0f,1.0f);
        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 0.0~1.0
                float money= (float) animation.getAnimatedValue();
                viewBinding.btnNavBack.setAlpha(money);
                viewBinding.tvSearch.setAlpha(money);
                viewBinding.rvList.getBackground().setAlpha((int) (255 * money));
                // 搜索框宽度从100%到75%
                lp.width = (int)(mWidth - (mWidth * 0.25 * money));
                mCurrWidth = lp.width;
                viewBinding.searchContainer.setLayoutParams(lp);
                // 搜索框父控件高度从100dp到56dp
                lp3.height = (int)(mHeight - ((mHeight - dip2Px(56)) * money));
                mCurrHeight = lp3.height;
                viewBinding.superContainer.setLayoutParams(lp3);
                // 搜索框中间内容向左偏移
                mCurrLeftMargin = (int) (mLeftMargin * money);
                lp2.setMargins(- mCurrLeftMargin,0,0,0);
                viewBinding.layoutSearch.setLayoutParams(lp2);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                KeyboardUtils.openKeyboard(viewBinding.etSearch,SearchActivity.this);
            }
        });
        valueAnimator.start();
    }


    /**
     * 退场动画
     * @param viewBinding
     */
    public void outputAnimator(final ActivitySearchBinding viewBinding){
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewBinding.searchContainer.getLayoutParams();
        final LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) viewBinding.layoutSearch.getLayoutParams();
        final LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) viewBinding.superContainer.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat( 1.0f,0.0f);
        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float money= (float) animation.getAnimatedValue();
                viewBinding.btnNavBack.setAlpha(money);
                viewBinding.tvSearch.setAlpha(money);
                viewBinding.rvList.getBackground().setAlpha((int) (255 * money));
                lp.width = (int)(mCurrWidth + (mWidth * 0.25 * (1- money)));
                viewBinding.searchContainer.setLayoutParams(lp);
                lp3.height = (int)(mCurrHeight + ((mHeight - mCurrHeight) * (1- money)));
                viewBinding.superContainer.setLayoutParams(lp3);
                lp2.setMargins((int) (-mCurrLeftMargin + mLeftMargin * (1- money)),0,0,0);
                viewBinding.layoutSearch.setLayoutParams(lp2);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                KeyboardUtils.closeKeyboard(viewBinding.etSearch,SearchActivity.this);
                finish();
                // 发现在某些手机系统自带的退场动画无法取消，强制使用自定义的退场动画 （其实动画什么也没做）
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        valueAnimator.start();
    }

    /** Dip换Px */
    public int dip2Px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        int px = (int)(dip * density + .5f);
        return px;
    }

}

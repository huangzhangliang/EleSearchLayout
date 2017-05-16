package com.lel.search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lel.search.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mBinding.searchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("width",mBinding.searchContainer.getWidth());
                intent.putExtra("height",mBinding.activityMain.getHeight());
                int leftMargin = mBinding.searchContainer.getWidth() - mBinding.layoutSearch.getWidth();
                intent.putExtra("leftMargin",leftMargin / 2);
                startActivity(intent);
            }
        });
    }

}

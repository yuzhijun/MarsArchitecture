package com.winning.marsarchitecture.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.winning.marsarchitecture.R;
import com.winning.marsarchitecture.model.GirlsData;
import com.winning.marsarchitecture.util.Constants;
import com.winning.marsarchitecture.viewmodel.DynamicGirlsViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private DynamicGirlsViewModel mGirlsViewModel;
    private TextView tvTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTest = findViewById(R.id.tvTest);
        mGirlsViewModel = ViewModelProviders.of(MainActivity.this).get(DynamicGirlsViewModel.class);
        mGirlsViewModel.getELiveObservableData(Constants.GIRLS_URL, "3").observe(MainActivity.this, girlsData -> {
            if (null != girlsData){
                List<GirlsData.ResultsBean> resultsBeans = girlsData.getResults();
            }
        });

        tvTest.setOnClickListener(view -> mGirlsViewModel.getGirlsData("2","3").observe(MainActivity.this, girlsData -> {
            if (null != girlsData){
                List<GirlsData.ResultsBean> resultsBeans = girlsData.getResults();
            }
        }));
    }
}

package com.winning.marsarchitecture.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.winning.marsarchitecture.R;
import com.winning.marsarchitecture.model.GirlsData;
import com.winning.marsarchitecture.util.Constants;
import com.winning.marsarchitecture.viewmodel.DynamicGirlsViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DynamicGirlsViewModel mGirlsViewModel;
    private TextView tvTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTest = findViewById(R.id.tvTest);
        mGirlsViewModel = ViewModelProviders.of(MainActivity.this).get(DynamicGirlsViewModel.class);

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGirlsViewModel.getLiveObservableData(Constants.GIRLS_URL).observe(MainActivity.this, new Observer<GirlsData>() {
                    @Override
                    public void onChanged(@Nullable GirlsData girlsData) {
                        if (null != girlsData){
                            List<GirlsData.ResultsBean> resultsBeans = girlsData.getResults();
                        }
                    }
                });
            }
        });
    }
}

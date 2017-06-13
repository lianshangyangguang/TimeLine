package com.time.view.scaleruler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvNumber;
    private ScaleView scaleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNumber = (TextView)findViewById(R.id.tvNumber);
        scaleview = (ScaleView)findViewById(R.id.scaleview);
        scaleview.setMaxNumber(240);
        scaleview.setMinNumber(0);
        scaleview.setScaleNumber(1);
        scaleview.setAllBlockNum(30);
        scaleview.setTextSize(30);
        scaleview.setCenterNum(100);
        scaleview.setNumberListener(new ScaleView.NumberListener() {
            @Override
            public void onChanged(int mCurrentNum) {
                String hour = "",min ="";
                if (mCurrentNum/10<10){
                    hour = "0"+mCurrentNum/10;
                }else {
                    hour =String.valueOf(mCurrentNum/10);
                }
                if (mCurrentNum%10 == 0){
                    min = "00";
                }else if ((mCurrentNum%10) *6<10){
                    min =  "0"+(mCurrentNum%10) *6;
                }else {
                    min = String.valueOf((mCurrentNum%10) *6);
                }
                tvNumber.setText(hour + ":"+min);
            }
        });
    }
}

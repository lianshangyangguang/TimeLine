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
        scaleview.setTextSize(20);
        scaleview.setCenterNum(100);
        scaleview.setNumberListener(new ScaleView.NumberListener() {
            @Override
            public void onChanged(float time) {

                String smin = "";
                int min  =(int) ( time * 60 )%60;
                if (min < 10 ){
                    smin = "0"+min;
                }else {
                    smin = min+"";
                }
                String sh ="";
                int h  =(int) ( time * 60 )/60;
                if (h < 10 ){
                    sh = "0"+h;
                }else {
                    sh = h+"";
                }
                tvNumber.setText(sh+":"+smin);
            }
        });
    }
}

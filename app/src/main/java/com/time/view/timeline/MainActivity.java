package com.time.view.timeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RulerView  timeAxis = new RulerView(this,null);
//        setContentView(timeAxis);
       setContentView(R.layout.activity_main);

    }
}

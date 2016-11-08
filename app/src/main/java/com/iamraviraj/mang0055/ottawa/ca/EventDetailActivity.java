package com.iamraviraj.mang0055.ottawa.ca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        SeekBar myseek=(SeekBar)findViewById(R.id.myseek);
        myseek.setProgress(myseek.getProgress()-1);
        

    }
}

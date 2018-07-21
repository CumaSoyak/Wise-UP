package com.example.cuma.tinder.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cuma.tinder.R;

public class SkorActivity extends AppCompatActivity {

    TextView time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skor);
        time=(TextView)findViewById(R.id.time);
       // createtoolbar();//sakla timer i
    }
    public void createtoolbar(){
        time.setVisibility(View.GONE);
    }
}

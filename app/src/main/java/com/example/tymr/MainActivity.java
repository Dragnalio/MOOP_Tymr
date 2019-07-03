package com.example.tymr;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mHandlerTask;
    private Button mButton;
    private Button resetButton;
    private Button add;
    private Button sub;
    private Button timeType;
    private int type = 3;
    private long multiplier = 3600;
    private String typeText;
    private TextView timerTextView;
    private String timerText;
    private boolean running;
    private long time = 600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        updateTimer();

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 600000;
                updateTimer();
                Toast.makeText(getBaseContext(),"Reset", Toast.LENGTH_SHORT).show();
            }
        });

        timeType = findViewById(R.id.timeType);
        timeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type--;
                if(type < 1) type = 3;
                multiplier = 1;
                switch (type){
                    case 3:
                        typeText = "HOUR";
                        multiplier*=3600;
                        break;
                    case 2:
                        typeText = "MINUTE";
                        multiplier*=60;
                        break;
                    case 1:
                        typeText = "SECOND";
                }
                timeType.setText(typeText);
                updateTimer();
            }
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time += multiplier * 1000;
                if(time >= 24*60*60000){
                    time = 24*60*60000;
                    Toast.makeText(getBaseContext(),"Maximum time", Toast.LENGTH_SHORT).show();
                }
                updateTimer();
            }
        });

        sub= findViewById(R.id.sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time -= multiplier * 1000;
                if(time <= 0){
                    time = 0;
                    Toast.makeText(getBaseContext(),"Minimum time", Toast.LENGTH_SHORT).show();
                }
                updateTimer();
            }
        });
        mHandler = new Handler();
        mHandlerTask = new Runnable() {
            @Override
            public void run() {
                if(time >= 0){
                    time -= 100;
                    updateTimer();
                }else{
                    mHandler.removeCallbacks(mHandlerTask);
                }
                mHandler.postDelayed(mHandlerTask, 100);
            }
        };

        mButton = findViewById(R.id.mButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    stop();
                }else{
                    start();
                }
                running = !running;
            }
        });
    }

    public void start(){
        mHandlerTask.run();
        mButton.setText("STOP");
        Toast.makeText(this,"Started", Toast.LENGTH_SHORT).show();
        toggleVisible(false);
    }

    public void stop(){
        mHandler.removeCallbacks(mHandlerTask);
        mButton.setText("START");
        Toast.makeText(this,"Paused", Toast.LENGTH_SHORT).show();
        toggleVisible(true);
    }

    public void toggleVisible(boolean visible){
        if(visible){
            resetButton.setVisibility(View.VISIBLE);
            timeType.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            sub.setVisibility(View.VISIBLE);
        }else{
            resetButton.setVisibility(View.GONE);
            timeType.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            sub.setVisibility(View.GONE);
        }
    }

    public void updateTimer(){
        int hour = (int) time/3600000;
        int min = (int) time%3600000 /60000;
        int sec = (int) time%60000 / 1000;
        timerText = "";
        if(hour < 10) timerText += "0";
        timerText += hour + ":";
        if(min < 10) timerText += "0";
        timerText += min + ":";
        if(sec < 10) timerText += "0";
        timerText += sec;

        timerTextView.setText(timerText);
    }


}
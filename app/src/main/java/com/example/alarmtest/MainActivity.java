package com.example.alarmtest;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private MainActivity act;
    private Button btn_set1,btn_update1,btn_cancel1,btn_set2,btn_update2,btn_cancel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        act = this;
        //闹钟1-----------------------------------------------------
        btn_set1 = (Button) findViewById(R.id.btn_set1);
        btn_update1 = (Button) findViewById(R.id.btn_update1);
        btn_cancel1 = (Button) findViewById(R.id.btn_cancel1);
        btn_set1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //api版本号是否大于等于19：
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    Toast.makeText(act, "设置提醒1111成功!"+(new Date()).toString(), Toast.LENGTH_SHORT).show();
//                    AlarmUtils.setAlarmRepeatOnce(act,System.currentTimeMillis()+1000*10,1,"提醒成功1111##"+(new Date()).toString(),2,0);
                    ArrayList<Date> dates = new ArrayList<>();
                    dates.add(new Date(System.currentTimeMillis()+1000*10));
                    dates.add(new Date(System.currentTimeMillis()+1000*30));
                    AlarmUtils.setAlarmDates(act,dates,1,"提醒成功1111##"+(new Date()).toString(),0,0);
                }
            }
        });
        btn_update1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(act, "更新提醒1111++", Toast.LENGTH_SHORT).show();
                AlarmUtils.updateAlarmRepeat(act,System.currentTimeMillis()+1000*5,0,1,"提醒已更改1111",2,1);
            }
        });
        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(act, "更新取消1111--", Toast.LENGTH_SHORT).show();
                AlarmUtils.cancelAlarm(act,1);
            }
        });
        //闹钟2-----------------------------------------------------
        btn_set2 = (Button) findViewById(R.id.btn_set2);
        btn_update2 = (Button) findViewById(R.id.btn_update2);
        btn_cancel2 = (Button) findViewById(R.id.btn_cancel2);
        btn_set2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //api版本号是否大于等于19：
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    Toast.makeText(act, "设置提醒2222成功!"+(new Date()).toString(), Toast.LENGTH_SHORT).show();
                    AlarmUtils.setAlarmRepeatOnce(act,System.currentTimeMillis()+1000*5,2,"提醒成功2222##"+(new Date()).toString(),2,0);
                }
            }
        });
        btn_update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(act, "更新提醒2222++", Toast.LENGTH_SHORT).show();
                AlarmUtils.updateAlarmRepeat(act,System.currentTimeMillis()+1000*15,0,2,"提醒已更改2222",2,0);
            }
        });
        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(act, "更新取消2222--", Toast.LENGTH_SHORT).show();
                AlarmUtils.cancelAlarm(act,2);
            }
        });
    }

}

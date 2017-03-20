package com.example.alarmtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by weihan on 2017/3/16.
 */
public class AlarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
    /*
    如果在传递给 startActivity() 的 Intent 对象里包含了 FLAG_ACTION_NEW_TASK，情况将发生变化，
    –系统将为新的 Activity “寻找”一个不同于调用者的 Task。不过要找的 Task 是不是一定就是 NEW 呢？
    如果是第一次执行，则这个设想成立，如果说不是，也就是说已经有一个包含此 Activity 的Task 存在，则不会再启动 Activity。
     */
        //
        long remind = intent.getLongExtra("remind",-1);
        int soundAndvibrate = intent.getIntExtra("sav",-1);//没取到,则-1
        String msg = intent.getStringExtra("msg");
        int ringtoneId = intent.getIntExtra("ringtoneId",-1);
        int id = intent.getIntExtra("id",-1);
        int flag = intent.getIntExtra("flag",-1);
        //
        Intent clockIntent = new Intent(context, ClockAlarmActivity.class);
        clockIntent.putExtra("remind",remind);
        clockIntent.putExtra("sav", soundAndvibrate);
        clockIntent.putExtra("msg", msg);
        clockIntent.putExtra("ringtoneId",ringtoneId);
        clockIntent.putExtra("id",id);
        clockIntent.putExtra("flag",flag);
        //
        switch(flag){
            case AlarmUtils.ALARM_FLAG_EVERY:
                long repeat = intent.getLongExtra("repeat",-1);
                clockIntent.putExtra("repeat", repeat);
                break;
            case AlarmUtils.ALARM_FLAG_WEEK:
                ArrayList<Integer> week = intent.getIntegerArrayListExtra("week");
                clockIntent.putIntegerArrayListExtra("week",week);
                break;
            case AlarmUtils.ALARM_FLAG_IRREGULAR:
                ArrayList<Date> dates = (ArrayList<Date>) intent.getSerializableExtra("dates");
                clockIntent.putExtra("dates",(Serializable)dates);
                break;
        }


        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//重用ClockAlarmActivity.class?
        context.startActivity(clockIntent);
    }
}

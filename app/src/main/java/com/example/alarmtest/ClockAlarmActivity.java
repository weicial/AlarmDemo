package com.example.alarmtest;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ClockAlarmActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;//音乐
    private Vibrator vibrator;//震动
    private int[] ringtone = {R.raw.beep,R.raw.in_call_alarm};//默认铃声的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_alarm);
        //获取Intent里传递过来的数据
        Intent intent = getIntent();
        long remind = intent.getLongExtra("remind",-2);
        long repeat = intent.getLongExtra("repeat",-2);
        int soundAndvibrate = intent.getIntExtra("sav",-2);
        String msg = intent.getStringExtra("msg");
        int ringtoneId = intent.getIntExtra("ringtoneId",-2);
        int id = intent.getIntExtra("id",-2);
        int flag = intent.getIntExtra("flag",-2);
        showAlarmDialog(remind,flag,intent,id,msg,soundAndvibrate,ringtoneId);
    }



    //显示提醒
    private void showAlarmDialog(final long remind, final int flag, final Intent intent, final int id, final String msg, final int soundAndvibrate, final int ringtoneId) {
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);

        if(soundAndvibrate == 0){
            vibrator.vibrate(new long[]{1000,100,1000,100}, 0);
        }else if(soundAndvibrate == 1){
            mediaPlayer = MediaPlayer.create(this, ringtone[ringtoneId]);
            mediaPlayer.setLooping(true);
        }else if(soundAndvibrate == 2){

            vibrator.vibrate(new long[]{1000,100,1000,100}, 0);
            mediaPlayer = MediaPlayer.create(this, ringtone[ringtoneId]);
            mediaPlayer.setLooping(true);
        }else{
            Log.d("ClockAlarmActivity", "showAlarmDialog: ");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒!").setMessage(msg+(new Date()).toString())
                .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(vibrator.hasVibrator()) {
                            vibrator.cancel();
                        }if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        switch (flag){
                            case AlarmUtils.ALARM_FLAG_EVERY:
                                long repeat = intent.getLongExtra("repeat",-2);
                                if(repeat > 0){
                                    setRepeat(remind+repeat,repeat,id,msg,soundAndvibrate,ringtoneId);
                                }
                                break;
                            case AlarmUtils.ALARM_FLAG_WEEK:
                                ArrayList<Integer> week = intent.getIntegerArrayListExtra("week");
                                setRepeatWeek(setNextDayWeek(remind,week),week,id,msg,soundAndvibrate,ringtoneId);
                                break;
                            case AlarmUtils.ALARM_FLAG_IRREGULAR:
                                ArrayList<Date> dates = (ArrayList<Date>) intent.getSerializableExtra("dates");
                                setRepeatDates(dates.get(0).getTime(),dates,id,msg,soundAndvibrate,ringtoneId);
                                break;
                        }

                        finish();
                    }
                }).create().show();
    }






    //计算下次提醒时间
    private long setNextDayWeek(long remind,ArrayList<Integer> week) {
        long next = 0;
        Date currentDate = new Date(remind);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        //Calendar.DAY_OF_WEEK,第一天是星期日,下标为1,sun=1,mon=2,tue=3
        int dow = calendar.get(Calendar.DAY_OF_WEEK)-1;//获取此次提醒日期是周几
        if(dow == 0){//sun=7,mon=1
            dow+=7;
        }
        for(int i= 0; i < week.size();i++){
            if(week.get(i)>dow){
                next = remind+(week.get(i)-dow)*1000*3600*24;
                break;
            }else if(i==week.size()-1){
                next = remind+(week.get(0)+7-dow)*1000*3600*24;
                break;
            }
        }
        return next;
    }

    //计算下次提醒时间
//    private long setNextDayDates(long remind, ArrayList<Date> dates) {
//        long next = 0;
//        Date currentDate = new Date(remind);
//        return next;
//    }

    //设置下一次闹钟的触发
    private void setRepeat(long next,long repeat,int id,String msg,
                           int soundAndvibrate, int ringtoneId) {
        if(next > 0){
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(AlarmUtils.ALARM_ACTION);
            intent.setClass(this,AlarmReceiver.class);
            intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));

            intent.putExtra("repeat", repeat);
            
            intent.putExtra("flag",AlarmUtils.ALARM_FLAG_EVERY);
            intent.putExtra("sav", soundAndvibrate);
            intent.putExtra("msg", msg);
            intent.putExtra("ringtoneId",ringtoneId);
            intent.putExtra("id",id);
            PendingIntent pi  =  PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,next,pi);
        }else {
            Log.d("ClockAlarmActivity", "setRepeat: "+(new Date().toString())+"没有下次闹钟");
        }
    }

    //设置下一次闹钟的触发
    private void setRepeatWeek(long next, ArrayList<Integer> week, int id, String msg, int soundAndvibrate, int ringtoneId) {
        if(next > 0){
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(AlarmUtils.ALARM_ACTION);
            intent.setClass(this,AlarmReceiver.class);
            intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));

            intent.putIntegerArrayListExtra("week",week);

            intent.putExtra("flag",AlarmUtils.ALARM_FLAG_WEEK);
            intent.putExtra("sav", soundAndvibrate);
            intent.putExtra("msg", msg);
            intent.putExtra("ringtoneId",ringtoneId);
            intent.putExtra("id",id);
            PendingIntent pi  =  PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,next,pi);
        }else {
            Log.d("ClockAlarmActivity", "setRepeatWeek: "+(new Date().toString())+"没有下次闹钟");
        }
    }

    //设置下一次闹钟的触发
    private void setRepeatDates(long next, ArrayList<Date> dates, int id, String msg, int soundAndvibrate, int ringtoneId) {
        if(next > 0){
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(AlarmUtils.ALARM_ACTION);
            intent.setClass(this,AlarmReceiver.class);
            intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));

            dates.remove(0);
            intent.putExtra("dates",(Serializable)dates);

            intent.putExtra("flag",AlarmUtils.ALARM_FLAG_IRREGULAR);
            intent.putExtra("sav", soundAndvibrate);
            intent.putExtra("msg", msg);
            intent.putExtra("ringtoneId",ringtoneId);
            intent.putExtra("id",id);
            PendingIntent pi  =  PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,next,pi);
        }else {
            Log.d("ClockAlarmActivity", "setRepeatDates: "+(new Date().toString())+"没有下次闹钟");
        }
    }
}

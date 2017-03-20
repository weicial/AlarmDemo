package com.example.alarmtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by weihan on 2017/3/16.
 */

public class AlarmUtils {
    public static final String ALARM_ACTION = "com.example.alarm";

    public static final int ALARM_FLAG_EVERY = 1;//每x天,每天,每周的某一天,固定天数间隔
    public static final int ALARM_FLAG_WEEK = 2;//每周中的某几天
    public static final int ALARM_FLAG_IRREGULAR = 3;//不规律的天数,如3月1号,4月3号,5月4号
    /**
     * @param context         上下文
     * @param remind     下一次提醒的时间
     * @param repeat          重复时间,每隔多长重复一次,为0则不重复
     * @param id              更新提醒,取消提醒
     * @param msg             提醒时显示的文本
     * @param soundAndvibrate 开启震动和铃声,0为震动,1为铃声,2为震动和铃声
     * @param ringtoneId      默认铃声的id
     */
    public static void setAlarmRepeat(Context context, long remind, long repeat, int id, String msg,
                                      int soundAndvibrate, int ringtoneId) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.setClass(context, AlarmReceiver.class);
        intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));
        intent.putExtra("repeat", repeat);
        intent.putExtra("remind",remind);
        intent.putExtra("flag",ALARM_FLAG_EVERY);
        intent.putExtra("sav", soundAndvibrate);
        intent.putExtra("msg", msg);
        intent.putExtra("ringtoneId",ringtoneId);
        intent.putExtra("id",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, remind, pi);
    }

    /**
     * @param context         上下文
     * @param remind          下一次提醒的时间
     * @param week            一周的哪几天提醒,按从小到大的顺序添加,周一就week.add(1)
     * @param id              更新提醒,取消提醒
     * @param msg             提醒时显示的文本
     * @param soundAndvibrate 开启震动和铃声,0为震动,1为铃声,2为震动和铃声
     * @param ringtoneId      默认铃声的id
     */
    public static void setAlarmWeek(Context context, long remind, ArrayList<Integer> week, int id, String msg,
                                    int soundAndvibrate, int ringtoneId){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.setClass(context, AlarmReceiver.class);
        intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));
        intent.putExtra("flag",ALARM_FLAG_WEEK);
        intent.putExtra("remind",remind);
        intent.putIntegerArrayListExtra("week",week);
        intent.putExtra("sav", soundAndvibrate);
        intent.putExtra("msg", msg);
        intent.putExtra("ringtoneId",ringtoneId);
        intent.putExtra("id",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, remind, pi);
    }

    /**
     * @param context         上下文
     * @param dates           提醒日期的list,从小到大添加
     * @param id              更新提醒,取消提醒
     * @param msg             提醒时显示的文本
     * @param soundAndvibrate 开启震动和铃声,0为震动,1为铃声,2为震动和铃声
     * @param ringtoneId      默认铃声的id
     */
    public static void setAlarmDates(Context context,ArrayList<Date> dates, int id, String msg,
                                     int soundAndvibrate, int ringtoneId){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.setClass(context, AlarmReceiver.class);
        intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));
        intent.putExtra("flag",ALARM_FLAG_IRREGULAR);
        intent.putExtra("dates",(Serializable)dates);//转换为Serializable
        intent.putExtra("remind",dates.get(0).getTime());
        intent.putExtra("sav", soundAndvibrate);
        intent.putExtra("msg", msg);
        intent.putExtra("ringtoneId",ringtoneId);
        intent.putExtra("id",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, dates.get(0).getTime(), pi);
    }



    //更新(更改)闹钟
    public static void updateAlarmRepeat(Context context, long remind, long repeat, int id, String msg,
                                         int soundAndvibrate, int ringtoneId){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.setClass(context, AlarmReceiver.class);
        intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));
        intent.putExtra("flag",ALARM_FLAG_EVERY);
        intent.putExtra("remind",remind);
        intent.putExtra("repeat", repeat);
        intent.putExtra("sav", soundAndvibrate);
        intent.putExtra("msg", msg);
        intent.putExtra("ringtoneId",ringtoneId);
        intent.putExtra("id",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, remind, pi);
    }

    //更新(更改)闹钟
    public static void updateAlarmWeek(Context context, long remind, ArrayList<Integer> week, int id, String msg,
                                         int soundAndvibrate, int ringtoneId){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.setClass(context, AlarmReceiver.class);
        intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));
        intent.putExtra("flag",ALARM_FLAG_WEEK);
        intent.putExtra("remind",remind);
        intent.putExtra("week", week);
        intent.putExtra("sav", soundAndvibrate);
        intent.putExtra("msg", msg);
        intent.putExtra("ringtoneId",ringtoneId);
        intent.putExtra("id",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, remind, pi);
    }
    //更新(更改)闹钟
    public static void updateAlarmDates(Context context, ArrayList<Date> dates, int id, String msg,
                                       int soundAndvibrate, int ringtoneId){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.setClass(context, AlarmReceiver.class);
        intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));
        intent.putExtra("flag",ALARM_FLAG_IRREGULAR);
        intent.putExtra("remind",dates.get(0).getTime());
        intent.putExtra("dates",(Serializable)dates);
        intent.putExtra("sav", soundAndvibrate);
        intent.putExtra("msg", msg);
        intent.putExtra("ringtoneId",ringtoneId);
        intent.putExtra("id",id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, dates.get(0).getTime(), pi);
    }

    //取消提醒
    public static void cancelAlarm(Context context,int id){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.setData(Uri.parse("content://calendar/calendar_alerts/"+id));
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_NO_CREATE);
        am.cancel(pi);
    }

    //每天重复
    public static void setAlarmRepeatEveryDay(Context context, long remindTime, int id, String msg,
                                              int soundAndvibrate, int ringtoneId) {
        setAlarmRepeat(context,remindTime,1000*3600*24,id,msg,soundAndvibrate,ringtoneId);
    }

    //只设置一次提醒
    public static void setAlarmRepeatOnce(Context context, long remindTime, int id, String msg,
                                          int soundAndvibrate, int ringtoneId) {
        setAlarmRepeat(context,remindTime,0,id,msg,soundAndvibrate,ringtoneId);
    }

}

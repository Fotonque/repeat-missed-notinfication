package com.example.nitifier2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;


import androidx.annotation.RequiresApi;

import java.io.File;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

public class NLService extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    private static PowerManager pm = null;
    private static PowerManager.WakeLock wl = null;
    private static boolean stopRunning = false;
    private static boolean isRunning = false;

    public Context context = this;
    public  Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!stopRunning){
                try{
                    isRunning = true;
                    int i = 0;
                    for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                        if (!sbn.getPackageName().equals("android")){
                            if (!sbn.getPackageName().equals("com.example.nitifier2")) {
                                System.out.println(sbn.getPackageName() + "\n");
                                i++;
                            }
                        }
                    }
                    if (i > 0){
                        try{
                            task();
                        }catch (Exception e){
                            System.out.println("task error");
                        }
                    }
                    handler.postDelayed(runnable, 90 * 1000L);
                }catch (Exception e){
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        }

    };
    public MediaPlayer player = null;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String NOTIFICATION_CHANNEL_ID = "com.example.andy.myapplication";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName,  NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new Notification.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("still not dead")
                .setContentIntent(pendingIntent).build();
        startForeground(1337, notification);
        return START_STICKY;
    }
    @Override
    public void onCreate() {
        // notifications
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.nitifier2.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        //unregisterReceiver(nlservicereciver);

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try{
            wl.release();
        }catch (Exception e){

        }
//        System.out.println("EVENT: onNotificationPosted");
        //stopRunning = false;
        int i = 0;
        for (StatusBarNotification sb : NLService.this.getActiveNotifications()) {
            if (!sb.getPackageName().equals("android")){
                if (!sb.getPackageName().equals("com.example.nitifier2")) {
                    System.out.println(sb.getPackageName() + "\n");
                    i++;
                }
            }
        }
        if (i > 0){
            pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, "app:wake");
            wl.acquire(24*60*60*1000);
//            System.out.println("WAKE_LOCK ACQUIRED");
            if (!isRunning){
                runnable.run();
            }
        }



//        System.out.println("wtfssssssssssssssss");
//        //audioPlayer("/storage/emulated/0/documents/Chiara/", "Chiara_kill.wav");
//        Log.i(TAG,"**********  onNotificationPosted");
//        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
//        Intent i = new  Intent("com.example.nitifier2.NOTIFICATION_LISTENER_EXAMPLE");
//        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
//        sendBroadcast(i);
//        System.out.println("ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        //turnOnScreen();
    }

//    public void repeatingAction(){
//        // repeating action
//        runnable = new Runnable() {
//
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            public void run() {
//                int i = 0;
//                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
//                    if (!sbn.getPackageName().equals("android")){
//                        if (!sbn.getPackageName().equals("com.example.nitifier2")) {
//                            System.out.println(sbn.getPackageName() + "\n");
//                            i++;
//                        }
//                    }
//                }
//                if (i > 0){
//                    try{
//                        task();
//                    }catch (Exception e){
//                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                        LocalDateTime now = LocalDateTime.now();
//                        System.out.println(dtf.format(now));
//                        System.out.println("task error");
//                    }
//                }
//                handler.postDelayed(runnable, 90 * 60 * 1000L);
//            }
//        };
//
//        handler.postDelayed(runnable, 90 * 60 * 1000L);
//    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        System.out.println("EVENT: onNotificationRemoved");
//        try {
//            //stopRunning = true;
//            wl.release();
//            System.out.println("WAKE_LOCK RELEASE");
//        }catch (Exception ignored){
//            System.out.println(ignored.getCause());
//        }
//        Log.i(TAG,"********** onNOtificationRemoved");
//        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
//        Intent i = new  Intent("com.example.nitifier2.NOTIFICATION_LISTENER_EXAMPLE");
//        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
//
//        sendBroadcast(i);
//        System.out.println("ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
//            System.out.println(intent);

            //audioPlayer("/storage/emulated/0/documents/Chiara/", "Chiara_kill.wav");
            if(intent.getStringExtra("command").equals("clearall")){
                //NLService.this.cancelAllNotifications();
                try{
                    wl.release();
                }catch (Exception e){

                }
            }


//            else if(intent.getStringExtra("command").equals("list")){
//                Intent i1 = new  Intent("com.example.nitifier2.NOTIFICATION_LISTENER_EXAMPLE");
//                i1.putExtra("notification_event","=====================");
//                sendBroadcast(i1);
//                int i=1;
//                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
//                    Intent i2 = new  Intent("com.example.nitifier2.NOTIFICATION_LISTENER_EXAMPLE");
//                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
//                    sendBroadcast(i2);
//                    i++;
//                }
//                Intent i3 = new  Intent("com.example.nitifier2.NOTIFICATION_LISTENER_EXAMPLE");
//                i3.putExtra("notification_event","===== Notification List ====");
//                sendBroadcast(i3);
//
//            }

        }
    }

    public void task(){
        int min = 1;
        int max = 125;
        File dir = new File("/storage/emulated/0/documents/Chiara/");
        String filename = "";
        Random rand = new Random();
        int voiceN = rand.nextInt((max - min) + 1) + min;
        int i = 0;
        for (File f : Objects.requireNonNull(dir.listFiles())){
            if (f.isFile()){
                if (i == voiceN){ break; }
                filename = f.getName();
                i++;
            }
        }
        audioPlayer("/storage/emulated/0/documents/Chiara/",filename);
    }

    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(path + File.separator + fileName);
            mp.prepare();
            mp.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


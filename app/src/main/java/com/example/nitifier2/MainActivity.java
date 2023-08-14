package com.example.nitifier2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "qwe";
    private TextView txtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, NLService.class));

        Button button = (Button) findViewById(R.id.btnCreateNotify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);

            }
        });
        Button button2 = (Button) findViewById(R.id.btnClearNotify);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });
        Button button3 = (Button) findViewById(R.id.btnListNotify);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void buttonClicked(View v){
        System.out.println(v.getId());
        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId = new String("asd");
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this, CHANNEL_ID);

            ncomp.setContentTitle("My Notification");
            ncomp.setContentText("Notification Listener Service Example");
            ncomp.setTicker("Notification Listener Service Example");
            ncomp.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            ncomp.setSmallIcon(R.drawable.ic_launcher_foreground);

            nManager.notify((int)System.currentTimeMillis(),ncomp.build());

        }
        if(v.getId() == R.id.btnClearNotify){
            System.out.println("clear");
            Intent i = new Intent("com.example.nitifier2.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","clearall");
            sendBroadcast(i);
        }
        if(v.getId() == R.id.btnListNotify){
            System.out.println("list");
            Intent i = new Intent("com.example.nitifier2.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","list");
            sendBroadcast(i);
        }
    }
}

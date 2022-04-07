package org.techtown.sns_project.pushAlarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.techtown.sns_project.InitialActivity;
import org.techtown.sns_project.R;

import java.util.Calendar;
import java.util.Date;

public class BackgroundAlarmService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(  handler );
        thread.start();
//        thread.stopForever();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    public void onDestroy() {
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread( handler );
        thread.start();
    }

    public void start() {
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread( handler );
        thread.start();
    }

    public void stop() {
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread( handler );
        thread.stopForever();
    }

    public class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            Intent intent = new Intent( BackgroundAlarmService.this, InitialActivity.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            PendingIntent pendingIntent = PendingIntent.getActivity( BackgroundAlarmService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT );
            Uri soundUri = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel( "my_notification", "n_channel", NotificationManager.IMPORTANCE_MAX );
                notificationChannel.setDescription( "description" );
                notificationChannel.setName( "Channel Name" );
                assert notificationManager != null;
                notificationManager.createNotificationChannel( notificationChannel );
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( BackgroundAlarmService.this )
                    .setSmallIcon( R.drawable.appicon )
                    .setLargeIcon( BitmapFactory.decodeResource( getResources(), R.drawable.appicon ) )
                    .setContentTitle( "Title" )
                    .setContentText( "ContentText" )
                    .setAutoCancel( true )
                    .setSound( soundUri )
                    .setContentIntent( pendingIntent )
                    .setDefaults( Notification.DEFAULT_ALL )
                    .setOnlyAlertOnce( true )
                    .setChannelId( "my_notification" )
                    .setColor( Color.parseColor( "#ffffff" ) );
            //.setProgress(100,50,false);
            assert notificationManager != null;
//            int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
//            Calendar cal = Calendar.getInstance();
//            int hour = cal.get( Calendar.HOUR_OF_DAY );
//            if (hour == 18) {
//                notificationManager.notify( m, notificationBuilder.build() );
//                thread.stopForever();
//            } else if (hour == 22) {
//                notificationManager.notify( m, notificationBuilder.build() );
//                thread.stopForever();
//            }
            int counter = 1;
            if(counter == 1) {
                notificationManager.notify(counter, notificationBuilder.build());
                counter++;
            }
        }
    }

}
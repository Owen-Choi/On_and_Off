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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.InitialActivity;
import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class BackgroundAlarmService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    ArrayList<String> Original;
    ArrayList<String> Updated;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        myServiceHandler handler = new myServiceHandler();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Original = new ArrayList<>();
        Updated = new ArrayList<>();
        Original_data_crawl();
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
                    .setContentText( "좋아요 수에 변화가 생겼습니다." )
                    .setAutoCancel( true )
                    .setSound( soundUri )
                    .setContentIntent( pendingIntent )
                    .setDefaults( Notification.DEFAULT_ALL )
                    .setOnlyAlertOnce( true )
                    .setChannelId( "my_notification" )
                    .setColor( Color.parseColor( "#ffffff" ) );
            assert notificationManager != null;

            // 아래의 조건을 db에 가는걸로 바꾸자. 됐으면 좋겠다....

//            if(counter == 1) {
//                Log.e("Thread", "들어왔다.");
//                notificationManager.notify(counter, notificationBuilder.build());
//                counter++;
//            }

            db.collection("users")
                    .document(firebaseUser.getUid()).collection("board_likes").get().addOnCompleteListener(task -> {
                        // 어떤 형태로든 변화가 생기면 알람을 주고싶은데...
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String temp = (String)documentSnapshot.getData().get("user");
                        Updated.add(temp);
                    }
                } });
            if(Updated.size() != Original.size()) {
                notificationManager.notify(1, notificationBuilder.build());
                Original_data_crawl();  // Original 리스트를 다시 변화된 값으로 최신화.
            }
        }
    }

    private void Original_data_crawl() {
        db.collection("users")
                .document(firebaseUser.getUid()).collection("board_likes").get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            String temp = (String)documentSnapshot.getData().get("user");
                            Log.e("woong", "" + temp);
                            Original.add(temp);
                        }
                        Log.e("test", " " + Original.size());
                    }
                });
    }
}
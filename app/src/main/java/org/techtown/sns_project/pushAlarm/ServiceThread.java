package org.techtown.sns_project.pushAlarm;

import android.os.Handler;
import android.util.Log;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;
    public ServiceThread(Handler handler) {
        this.handler = handler;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }
    public void run() {
        //반복적으로 수행할 작업을 한다.
        while (isRun) {
            handler.sendEmptyMessage( 0 );
        //쓰레드에 있는 핸들러에게 메세지를 보냄
            try { Thread.sleep( 2000 );
                // 쓰레드는 2초를 주기로 돌아간다.
                // 알람은 캐시로 쓰라던데
                } catch (Exception e) { } } }

}

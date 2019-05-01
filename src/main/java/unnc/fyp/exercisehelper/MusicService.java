package unnc.fyp.exercisehelper;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Yangsha XIE 16518816 on 2019/4/3.
 * Final year project
 */


public class MusicService extends Service {

    public StreamingPlayer streamingPlayer;
    public static boolean started;

    public MusicService() {
        streamingPlayer = new StreamingPlayer();
    }

    //  Keep Activity and Service communication through Binder
    public MyBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }

        public void playMusic() {
            streamingPlayer.play();
        }

        public void pauseMusic() {
            streamingPlayer.pause();
        }

        public void loadMusic(String str) {
            streamingPlayer.load(str);
        }

        public void stopMusic() {
            streamingPlayer.stop();
        }

    }

    public void loadMusic(String str) {
        streamingPlayer.load(str);
    }

    public void stopMusic() {
        streamingPlayer.stop();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate executed");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Exercise Helper is running")
                .setContentText("Click to go back")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setAutoCancel(false)
                .build();
        startForeground(1, notification);
        started = true;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //streamingPlayer.stop();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        streamingPlayer.stop();
    }
}

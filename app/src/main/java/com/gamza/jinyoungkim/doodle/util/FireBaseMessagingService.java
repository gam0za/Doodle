package com.gamza.jinyoungkim.doodle.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.view.doodle_detail.DetailActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseMessagingService extends FirebaseMessagingService {
    private int alarmType = 1000;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // remoteMessage: 서버에서 준 메세지
        if (Integer.parseInt(remoteMessage.getData().get("type")) == alarmType) {
            sendNotification(remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("body"),
                    Integer.parseInt(remoteMessage.getData().get("idx")));
        }
////


    }

    //badge
    public void count() {

    }


    public void sendNotification(String title, String body, int idx){
        Intent i= new Intent(getApplicationContext(),DetailActivity.class);
        i.putExtra("detail_idx",idx);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        // push 알람 오게 하는 부분
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP ? R.drawable.noticon : R.drawable.doodle_logo)
                .setColor(0xff464D56)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

    }
}

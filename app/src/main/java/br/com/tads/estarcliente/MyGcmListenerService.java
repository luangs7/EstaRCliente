package br.com.tads.estarcliente;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;


public class MyGcmListenerService extends GcmListenerService {
    public static final String TAG = "LOG";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, "--> " + data);



           Intent intent = new Intent(this, MainActivity.class);
           intent.putExtra("notificacao", 1000);
           intent.putExtra("position", 2);

           NotificationCompat.InboxStyle inboxStyle =
                   new NotificationCompat.InboxStyle();

           String body = "";
           String url = "";
           try {
               body = data.getString("gcm.notification.title");
               url = data.getString("gcm.notification.url","");
           }catch (Exception ex){
               body = "Novo push!";
           }


            intent.putExtra("url",url);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
           inboxStyle.setSummaryText(body);

           if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
               final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                       .setContentText(body)
                       .setSmallIcon(R.mipmap.ic_launcher)
                       .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                       .setColor(getResources().getColor(R.color.colorPrimary))
                       .setDefaults(Notification.DEFAULT_ALL)
                       .setContentIntent(pendingIntent)
                       //.setStyle(inboxStyle)
                       .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                       .setContentTitle("EstaR");


               NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
             //  nm.notify(9999, builder.build());

           }else{
               final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                       .setContentText(body)
                       .setSmallIcon(R.mipmap.ic_launcher)
                       //.setStyle(inboxStyle)
                       .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                       .setDefaults(Notification.DEFAULT_ALL)
                       .setContentIntent(pendingIntent)
                       .setContentTitle("EstaR");


               NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
              // nm.notify(9999, builder.build());
           }
       }
    }


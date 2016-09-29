package br.com.tads.estarcliente.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import br.com.tads.estarcliente.MainActivity;
import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.TimeActivity;

/**
 * Created by Dev_Maker on 12/04/2016.
 */
public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        qualquer();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate(){

    }

    public void qualquer(){ Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), TimeActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Intent intent = new Intent(this, TimeActivity.class);
        intent.putExtra("notificacao", 1000);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        String teste = " Seu tempo está se esgotando.";
        inboxStyle.setSummaryText(teste);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentText(teste)
                     .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setSound(sound)
                            //.setStyle(inboxStyle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(teste))
                    .setContentTitle("ATENÇÃO!");


            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(9999, builder.build());

            //CancelAlarm(getBaseContext());

        }else{
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentText(teste)
                    .setSmallIcon(R.mipmap.ic_launcher)
                            //.setStyle(inboxStyle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(teste))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setSound(sound)
                    .setContentTitle("ATENÇÃO!");


            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(9999, builder.build());

            //CancelAlarm(getBaseContext());

        }
    }

//    public void CancelAlarm(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
}

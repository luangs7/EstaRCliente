package br.com.tads.estarcliente.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.tads.estarcliente.TimeActivity;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.model.Timer;


/**
 * Created by Dev_Maker on 29/09/2016.
 */
public class TimerService extends Service {
    static Context context;
    private static CountDown countDown;
    private static NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    private static NotificationManager mNotifyMgr;
    private static AlarmManager alarmManager;
    private static PendingIntent pendingIntent;

    //    Atributos do tempo
    public static long TIME;
    public static long ALARM = 20;
    public static Date dateclose;
    public static long MILLIS;
    public static long secondsLeft;
    public static String timetoprint;
    Timer timer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("Script", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.context = this;
        timer = new LocalDbImplement<Timer>(context).getDefault(Timer.class);
        Log.i("Script", "flags: "+flags);
        Log.i("Script", "startId: "+startId);
        Log.e("active","active");
        try {
            TIME = getDateDifference(timer.getDateFinish());
            countDown = new CountDown(TIME);
            alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            createNotification();
            countDown.start();


            //Cria e atribui um CountDownBehavior ao contador
            countDown.setCountDownListener(new CountDownBehavior(ALARM, "mm:ss") {
                @Override
                public void onEnd() {
                    mBuilder.mActions.clear();
                    mBuilder.setOngoing(false);
                    mBuilder.setContentText("Seu tempo acabou.");
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                    stopSelf();
                    //mNotifyMgr.cancel(mNotificationId);
                }

                @Override
                protected void onAlarm() {
                    alarmMethodwithCount();
                }

                @Override
                protected void displayTimeLeft(String timeLeft) {
                    timetoprint = timeLeft;
                    mBuilder.setContentText(timeLeft + " restante");
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
//                    textView.setText(timeLeft);
                }
            });

            Toast.makeText(TimerService.this, "ative", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            String error = e.getMessage();
            Log.e("error",error);
        }

//        return super.onStartCommand(intent, START_STICKY, startId);
        return START_STICKY;
    }

    private static void alarmMethodwithCount(){
        Intent myIntent = new Intent(context , NotifyService.class);
        pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);

        Date data = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(data);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(context, "Start Alarm2", Toast.LENGTH_LONG).show();
    }

    public void createNotification(){

        Intent resultIntent = new Intent(this, TimeActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_lock_power_off)
                        .setContentTitle("EstaR")
//                        .setContentText("1 minuto restante")
//                        .addAction(android.R.drawable.btn_plus,"Renovar",pendingIntentYes)
//                        .addAction(android.R.drawable.ic_menu_close_clear_cancel,"Cancelar",null)
                        .setOngoing(true);




        // Gets an instance of the NotificationManager service
        mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.

        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


    public static void addIncrease(){
        secondsLeft = countDown.increaseBy(60);
        TIME = TIME + (1*60);
    }

    public static String printTime(){
        return timetoprint;
    }

    //    comparar o tempo que foi aberto a activity, com o tempo para finalizar o periodo
    public long getDateDifference(String finishDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String actualDate = dateFormat.format(Calendar.getInstance().getTime());

        try {
            Date dateFinish = dateFormat.parse(finishDate);

            Date currentDate = dateFormat.parse(actualDate);

            long diff = dateFinish.getTime() - currentDate.getTime();

            long days = diff / (24 * 60 * 60 * 1000);
            diff -= days * (24 * 60 * 60 * 1000);

            long hours = diff / (60 * 60 * 1000);
            diff -= hours * (60 * 60 * 1000);

            long minutes = diff / (60 * 1000);
            diff -= minutes * (60 * 1000);

            long seconds = diff / 1000;


            seconds = seconds + 60*minutes;

            return  seconds;

//            counterDaysTV.setText(days + "");
//            counterMinsTV.setText(minutes + "");
//            counterHoursTV.setText(hours + "");
//            counterSecTV.setText(seconds + "");

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public void onDestroy() {
        startService(new Intent(context, TimerService.class));
        //sendBroadcast(new Intent("IWillStartAuto"));
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        //sendBroadcast(new Intent("IWillStartAuto"));

        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +1000, restartServicePI);
        startService(new Intent(context, TimerService.class));
    }
}

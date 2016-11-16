package br.com.tads.estarcliente.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.TimeActivity;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.model.Estar;
import br.com.tads.estarcliente.model.Timer;

public class MyServiceTempo extends Service {
    private static final String TAG = "";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = 10f;

    static Context context;
    private static CountDown countDown;
    private static NotificationCompat.Builder mBuilder;
    static int mNotificationId = 005;
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
    public static Timer timer;
    public static long timetoleft;

    private class CountDownListener extends CountDownBehavior
    {
        public Context mContext;
        public CountDownListener(Context context, long alarmTime, String displayFormat) {
            super(alarmTime, displayFormat);
            this.mContext = context;
        }

        @Override
        protected void onAlarm() {
            alarmMethodwithCount();
        }

        @Override
        protected void displayTimeLeft(String timeLeft) {
            Estar estarValidation = new LocalDbImplement<Estar>(context).getDefault(Estar.class);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            boolean renovado = preferences.getBoolean("renovado", false);

            if(estarValidation != null) {
                Log.e("status", String.valueOf(renovado));
                if(renovado) {
                    new LocalDbImplement<Estar>(context).clearObject(Estar.class);
                    countDown.increaseBy(60);
                    estarValidation.setRenovado(false);
                    new LocalDbImplement<Estar>(context).save(estarValidation);
                   // Toast.makeText(context, "Time" + timetoleft, Toast.LENGTH_SHORT).show();
                    preferences.edit().putBoolean("renovado", false).apply();
                }

                timetoleft--;
                timetoprint = timeLeft;
                mBuilder.setContentText(timeLeft + " restante");
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                //Toast.makeText(context, "Tempo" + timetoleft, Toast.LENGTH_SHORT).show();

            }else{
                countDown.stop();
                mNotifyMgr.cancel(mNotificationId);
                stopSelf();
            }

            Log.e(TAG, "displaytime");
        }

        @Override
        public void onEnd() {
            alarmMethodwithCount();
            //mBuilder.mActions.clear();
            mBuilder.setOngoing(false);
            mBuilder.setContentText("Seu tempo acabou.");
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        context = this;
        Estar estar = new LocalDbImplement<Estar>(context).getDefault(Estar.class);
        if(estar != null) {
            createService();
            Log.e(TAG, "onStartCommandCreate");
            super.onStartCommand(intent, flags, startId);
            return START_STICKY;

        }else{
            Log.e(TAG, "onStartCommand");
            super.onStartCommand(intent, flags, startId);
            countDown.stop();

            mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancel(mNotificationId);
            stopSelf();
            return START_NOT_STICKY;
        }
    }


    @Override
    public void onCreate()
    {
        context = this;

    }


    public  boolean createService(){
        MyServiceTempo myServiceTempo = new MyServiceTempo();

        Estar estar = new LocalDbImplement<Estar>(MyServiceTempo.this).getDefault(Estar.class);
        if(estar != null) {

            timer = new LocalDbImplement<Timer>(MyServiceTempo.this).getDefault(Timer.class);

            try {
                TIME = getDateDifference(timer.getDateFinish());
                if (TIME < 0) {
                    countDown = new CountDown(0);
                } else {
                    countDown = new CountDown(TIME);
                }

                timetoleft = TIME;
                alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                createNotification(context);
                countDown.start();
                //alarmAgendado();
                ALARM = (long) estar.getAlert();

                //Cria e atribui um CountDownBehavior ao contador

                myServiceTempo.setListener();

//                countDown.setCountDownListener(new CountDownListener(ALARM, "mm:ss"));

                Log.e("active", "active");
                return true;

            } catch (Exception e) {
                String error = e.getMessage();
                Log.e("error", error);
                // return false;
            }
            return true;

        }else{
            myServiceTempo.stopSelf();
            return false;
        }
    }


    public void setListener(){
        countDown.setCountDownListener(new CountDownListener(context,ALARM, "mm:ss"));
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
//        countDown.stop();
        //mNotifyMgr.cancel(mNotificationId);
    }


//    private static void alarmAgendado(){
//
//        long time = secondsLeft/60;
//
//        Intent myIntent = new Intent(context , NotifyService.class);
//        pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
//
//        Date data = new Date();
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.setTime(data);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time*1000, pendingIntent);
//        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time*1000, pendingIntent);
//        else
//            alarmManager.set(AlarmManager.RTC_WAKEUP, time*1000, pendingIntent);
//
//        //    Toast.makeText(context, "Start Alarm2", Toast.LENGTH_LONG).show();
//    }

    private void alarmMethodwithCount(){
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

        // Toast.makeText(context, "Start Alarm2", Toast.LENGTH_LONG).show();
    }

    public static void createNotification(Context context){

        Intent resultIntent = new Intent(context, TimeActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.traffic_sign)
                        .setContentTitle("EstaR")
                        .setColor(Color.parseColor("#218328"))
//                        .setContentText("1 minuto restante")
//                        .addAction(android.R.drawable.btn_plus,"Renovar",pendingIntentYes)
//                        .addAction(android.R.drawable.ic_menu_close_clear_cancel,"Cancelar",null)
                        .setOngoing(true);




        // Gets an instance of the NotificationManager service
        mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.cancel(mNotificationId);
        mNotifyMgr.cancelAll();
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void updNotification(){
       // mBuilder.setContentText("Seu período está ativo.");
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }



    public static void addIncrease(){

        try {
            countDown.increaseBy(60);

        }catch (Exception e){

           Log.d("teste",e.getMessage());
        }



    }

    public static String printTime(){
        return timetoprint;
    }

    public static long getDateDifference(String finishDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
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

    public static void finish(){
       // new LocalDbImplement<Estar>(context).clearObject(Estar.class);

//        countDown.stop();
//        countDown.finish();
        //mNotifyMgr.cancelAll();
    }



}

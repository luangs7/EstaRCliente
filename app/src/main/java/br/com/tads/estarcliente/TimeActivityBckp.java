package br.com.tads.estarcliente;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.tads.estarcliente.alarm.CountDown;
import br.com.tads.estarcliente.alarm.CountDownBehavior;
import br.com.tads.estarcliente.alarm.TimerJobSchedulerService;
import br.com.tads.estarcliente.alarm.TimerService;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.OnDialogButtonClick;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.Estar;
import br.com.tads.estarcliente.model.Timer;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.request.BaseRequest;
import br.com.tads.estarcliente.model.request.EstarRequest;

public class TimeActivityBckp extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    private TextView textView;
    private CountDown countDown;
    private NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    private NotificationManager mNotifyMgr;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Button btnRenovar,btnFim;

    //    Atributos do tempo
    public static long TIME = 0;
    public static long ALARM = 0;
    public static Date dateclose;
    public static long MILLIS;
    public static long secondsLeft;
    Timer timer;
    Estar estar;

    //    for maps
    private GoogleMap mMap;
    public static final int  REQUEST_PERMISSIONS_CODE = 128;
    private GoogleApiClient mGoogleApiClient;
    Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent it = getIntent();
        estar =  new LocalDbImplement<Estar>(TimeActivityBckp.this).getDefault(Estar.class);
        if(estar != null){
            //TIME = estar.getHoras() * 60;
            ALARM = estar.getAlert();
            Log.e("values",String.valueOf(TIME)+ " / " +String.valueOf(ALARM) );
        }else{
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Tempo de EstaR");
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            // toolbar.setNavigationIcon(R.drawable.ic_action_name);
            setSupportActionBar(toolbar);
        }

        btnRenovar = (Button) findViewById(R.id.btnRenovar);
        btnFim = (Button) findViewById(R.id.btnFim);
        textView = (TextView)findViewById(R.id.txtTimer);

        timer = new LocalDbImplement<Timer>(TimeActivityBckp.this).getDefault(Timer.class);


        try {
            if (estar.getHoras() == 3) {
                btnRenovar.setVisibility(View.INVISIBLE);
            }
            if(timer.getSecondsTofinish() > 220){
                btnRenovar.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            if(timer.getSecondsTofinish() > 220){
                btnRenovar.setVisibility(View.INVISIBLE);
            }
        }



        TIME = getDateDifference(timer.getDateFinish());
        if(TIME < 0){
            TIME = 0;
            countDown = new CountDown(TIME);
            Log.e("timer1",String.valueOf(TIME));
        }else{
            countDown = new CountDown(TIME);
            Log.e("timer2",String.valueOf(TIME));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // wrap your stuff in a componentName
            ComponentName mServiceComponent = new ComponentName(this, TimerJobSchedulerService.class);
            // set up conditions for the job
            android.app.job.JobInfo.Builder builder = new android.app.job.JobInfo.Builder(1, mServiceComponent);

            android.app.job.JobInfo task = builder.setRequiredNetworkType(android.app.job.JobInfo.NETWORK_TYPE_UNMETERED)
                    .setOverrideDeadline(100)
                    .setPersisted(true)
                    .build();
            // inform the system of the job
            android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(task);

        }else{
            startService(new Intent(this, TimerService.class));
        }







//        createNotification();
//
        //Cria o contador com 1 minuto
        countDown = new CountDown(TIME);

        //Cria e atribui um CountDownBehavior ao contador
        countDown.setCountDownListener(new CountDownBehavior(0, "mm:ss") {
            @Override
            public void onEnd() {
//                mBuilder.mActions.clear();
//                mBuilder.setOngoing(false);
//                mBuilder.setContentText("Seu tempo acabou.");
//                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                //mNotifyMgr.cancel(mNotificationId);
            }

            @Override
            protected void onAlarm() {
                //alarmMethodwithCount();
            }

            @Override
            protected void displayTimeLeft(String timeLeft) {
//                mBuilder.setContentText(timeLeft + " restante");
//                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                textView.setText(timeLeft);
            }
        });

//        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        countDown.start();
//        alarmMethod();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdata(estar);
            }
        });


        btnRenovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getdata(estar);
            }
        });
    }

    protected void addClick(View v){
        if(secondsLeft == 0){
            TIME = 1*60;
            countDown.start();
        }
        btnRenovar.setVisibility(View.INVISIBLE);
        countDown.increaseBy(60);
        TimerJobSchedulerService.addIncrease();
        getdata(estar);
    }


    @Override
    protected void onDestroy() {

        TimerJobSchedulerService.updNotification();
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    public void createNotification(){

        Intent resultIntent = new Intent(this, TimeActivityBckp.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
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



    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void getdata(final Estar estar){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                getdata(estar);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<EstarRequest> callListener = new CallListener<EstarRequest>(this, "Buscando dados", onDialogButtonClick) {
            @Override
            public void onResponse(EstarRequest request) {
                super.onResponse(request);
                if (request.success()) {
//                    TIME = secondsLeft + (1*60);
//                    if(secondsLeft == 0){
//                        TIME = 1*60;
//                        countDown.start();
//                    }
                    if(secondsLeft == 0){
                        TIME = 1*60;
                        countDown.start();
                    }
                    btnRenovar.setVisibility(View.INVISIBLE);
                    countDown.increaseBy(60);
                    TimerJobSchedulerService.addIncrease();

                    Usuario usuario = new LocalDbImplement<Usuario>(TimeActivityBckp.this).getDefault(Usuario.class);
                    new LocalDbImplement<Usuario>(TimeActivityBckp.this).clearObject(Usuario.class);
                    usuario.setSaldo(usuario.getSaldo(),1);
                    new LocalDbImplement<Usuario>(getBaseContext()).save(usuario);

                    Toast.makeText(TimeActivityBckp.this,"Renovado com sucesso.", Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(TimeActivityBckp.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).addRenovar(callListener,estar);
    }

    public void setdata(final Estar estar){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                setdata(estar);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<BaseRequest> callListener = new CallListener<BaseRequest>(this, "Buscando dados", onDialogButtonClick) {
            @Override
            public void onResponse(BaseRequest request) {
                super.onResponse(request);
                if (request.success()) {

                  new LocalDbImplement<Estar>(TimeActivityBckp.this).clearObject(Estar.class);
                    TimerJobSchedulerService.finish();
                    startActivity(new Intent(getBaseContext(),MainActivity.class));

                }else{
                    Toast.makeText(TimeActivityBckp.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).updStatus(callListener,estar);
    }


    private synchronized void callConnection(){
        Log.i("LOG", "LastLocationActivity.callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );

            }else{
                lastLocation = LocationServices
                        .FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
        }else{
            lastLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        if(estar != null) {
            LatLng location = new LatLng(estar.getLatitude(), estar.getLongitude());
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions().position(location).title("Essa é a posição do carro"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));

//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//
//
//                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", lastLocation.getLatitude(), lastLocation.getLongitude(), "Localização atual", Double.parseDouble(street.getLatitude()), Double.parseDouble(street.getLongitude()), "Seu destino");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                    startActivity(intent);
//
//                }
//            });

        }else{
            Toast.makeText(this, "Sua localização não foi encontrada. Verifique se seu celular está funcionando corretamente.", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        callConnection();
    }




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
}
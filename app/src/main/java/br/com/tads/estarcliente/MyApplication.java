package br.com.tads.estarcliente;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.InetAddress;


/**
 * Created by devmaker on 1/15/15.
 */
public class MyApplication extends Application {

    /*Location mlocation;
    Date lastupdate;
    int lastlocationstatus = LocationProvider.AVAILABLE;
    public static int REQUEST_PERMISSIONS_CODE = 128;
    public Location getLocation() {
        return mlocation;
    }

    public void setLocation(Location location) {
        this.mlocation = location;
    }

    @Override
    public void onLocationChanged(Location location) {
        //BuildConfig.DEBUG
        if(false){
            if (location != null) {
                if(mlocation == null
                        || location.getProvider().equalsIgnoreCase(LocationManager.GPS_PROVIDER)
                        || mlocation.getProvider().equalsIgnoreCase(LocationManager.NETWORK_PROVIDER)
                        || lastlocationstatus != LocationProvider.AVAILABLE){
                    mlocation = new Location(location);
                    lastupdate = Calendar.getInstance().getTime();
                    saveLocation(String.valueOf(mlocation.getLatitude()),String.valueOf(mlocation.getLongitude()));
                    //if(BuildConfig.DEBUG)
                    Toast.makeText(this, "latitude: " + mlocation.getLatitude() + ", longitude: " + mlocation.getLongitude() + " " +mlocation.getProvider(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(lastupdate);
                    calendar.add(Calendar.SECOND,60);
                    Calendar calendarnow = Calendar.getInstance();
                    if(calendarnow.after(calendar)){
                        mlocation = new Location(location);
                        lastupdate = Calendar.getInstance().getTime();
                        Toast.makeText(this, "latitude: " + mlocation.getLatitude() + ", longitude: " + mlocation.getLongitude() + " " +mlocation.getProvider(), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("onLocationChanged","latitude: " + mlocation.getLatitude() + ", longitude: " + mlocation.getLongitude() + " " +mlocation.getProvider());
            }
        }else{
            if (location != null) {
                mlocation = new Location(location);
                lastupdate = Calendar.getInstance().getTime();
                saveLocation(String.valueOf(mlocation.getLatitude()),String.valueOf(mlocation.getLongitude()));
                Toast.makeText(this, "latitude: " + mlocation.getLatitude() + ", longitude: " + mlocation.getLongitude() + " " +mlocation.getProvider(), Toast.LENGTH_SHORT).show();
                Log.d("onLocationChanged","latitude: " + mlocation.getLatitude() + ", longitude: " + mlocation.getLongitude() + " " +mlocation.getProvider());
            }
        }

    }

    public void saveLocation(String latitude,String longitude){
        Corrida corrida = new LocalDbImplement<Corrida>(getBaseContext()).getDefault(Corrida.class);
        if(corrida != null)
        {
            corrida.getCoordenadas().add(new Coordenada(latitude,longitude));
            new LocalDbImplement<Corrida>(getBaseContext()).save(corrida);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("onStatusChanged",provider);
        //if(BuildConfig.DEBUG)
        if(LocationProvider.AVAILABLE != status)
            Toast.makeText(this, "Status GPS: " + (LocationProvider.OUT_OF_SERVICE==status ? "Fora de serviço" : "Indisponível") + " - " + provider, Toast.LENGTH_SHORT).show();
        lastlocationstatus = status;
    }

    @Override
    public void onProviderEnabled(String provider) {
        if(BuildConfig.DEBUG)
            Toast.makeText(this, "enable: " +provider, Toast.LENGTH_SHORT).show();
        Log.d("onProviderEnabled",provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "localização desabilitada: " +provider, Toast.LENGTH_SHORT).show();
        Log.d("onProviderDisabled",provider);
        mlocation = null;
    }
*/
    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static MyApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        FirebaseApp.initializeApp(this,new FirebaseOptions.Builder().build());

        //lastupdate = Calendar.getInstance().getTime();
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //boolean run = sp.getBoolean(SincronizarIntentService.TAG, false);
        // initialize the singleton

        FirebaseMessaging.getInstance().subscribeToTopic("general");

        sInstance = this;
    }

    /*public void ativalocalicacao(AppCompatActivity activity){
        try {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
           // if(BuildConfig.DEBUG)
            if(false)
               locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);
        }catch (SecurityException ex){
            Toast.makeText(this, "Erro ao buscar localização",Toast.LENGTH_SHORT).show();
        }

    }*/

    /**
     * @return MyApplication singleton instance
     */
    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public Context getContext() {
        return this;
    }


    /*public void downloadImage(Context context, String url, ImageView imageView) {
            Picasso.with(context).load(url).placeholder(R.drawable.img_placeholder_custom)
                    .error(R.drawable.img_placeholder_custom).into(imageView);
    }

    public void downloadImage(Context context, String url, ImageView imageView, Callback callback) {
        Picasso.with(context).load(url).placeholder(R.drawable.img_placeholder_custom)
                .error(R.drawable.img_placeholder_custom).into(imageView, callback);
    }*/


    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("https://www.google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

  /*  public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni == null ? false : true;
    } */

    public boolean isNetworkConnected() {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();

            ConnectivityManager cm = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public void changeActionBarTitle(Activity activity, String title) {
        activity.getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }


    public String applicationName(){
        try{

            Resources appR = getContext().getResources();
            CharSequence txt = appR.getText(appR.getIdentifier("app_name","string", getContext().getPackageName()));
            return txt.toString();
        }catch (Exception ex){
            Log.e("applicationColor",ex.getMessage());
            return MyApplication.class.getPackage().getName().replace("br.com.tads.","");
        }


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

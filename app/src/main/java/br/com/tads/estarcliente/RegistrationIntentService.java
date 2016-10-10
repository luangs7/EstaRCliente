package br.com.tads.estarcliente;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.request.BaseRequest;
import br.com.tads.estarcliente.push.Configuration;


public class RegistrationIntentService extends IntentService {
    public static final String LOG = "LOG";


    public RegistrationIntentService() {
        super(LOG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = sp.getBoolean("status", false);

        synchronized (LOG) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try {

                if (!status) {
                    String token = instanceID.getToken(Configuration.SENDER_ID,
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                            null);

                    if(token.equalsIgnoreCase("")){
                        token = instanceID.getToken(Configuration.SENDER_ID,
                                GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                                null);
                    }else {
                        sp.edit().putString("token_push", token).apply();
                    }
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String deviceid = "";
                    try {
                        if (telephonyManager.getDeviceId() == null) {
                            deviceid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                        } else {
                            deviceid = telephonyManager.getDeviceId();
                        }
                        sp.edit().putString("deviceid",deviceid).apply();
                    } catch (Exception ex) {
                        Log.d("RegistrationIntent", ex.getMessage());
                        deviceid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                        sp.edit().putString("deviceid",deviceid).apply();
                    }
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                    CallListener<BaseRequest> callListener = new CallListener<BaseRequest>(getBaseContext(), null, null) {
                        @Override
                        public void onResponse(BaseRequest request) {
                            super.onResponse(request);
                            Log.e("onResponse", request.getResult());
                        }
                    };
                    new BaseDao(getBaseContext()).sendPush(callListener, sp.getString("deviceid", ""), sp.getString("token_push", ""),refreshedToken );
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

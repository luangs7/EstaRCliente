package br.com.tads.estarcliente.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Dev_Maker on 23/09/2016.
 */
public class AlarmBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("shuffTest","I Arrived!!!!");
        //Toast.makeText(context, "Alarm worked!!", Toast.LENGTH_LONG).show();

        String action = intent.getAction();

        if("yes".equals(action)) {
            Log.v("shuffTest","Pressed YES");
        }
        }
}

package br.com.tads.estarcliente.alarm;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Dev_Maker on 28/09/2016.
 */
public class CountDownService extends IntentService {
    public static final String LOG = "LOG";


    public CountDownService() {
        super(LOG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

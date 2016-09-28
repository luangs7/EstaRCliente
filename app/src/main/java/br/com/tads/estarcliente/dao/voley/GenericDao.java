package br.com.tads.estarcliente.dao.voley;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.google.gson.Gson;


public class GenericDao {

	protected Activity activity;
	protected Gson gson;
	protected ProgressDialog dialog;

	public GenericDao(Activity activity) {
		this.activity = activity;
		gson = new Gson();
	}
	
	protected void addRequest(Request request) {
		request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 5, 0.1f));

       // // add the request object to the queue to be executed
		// SplashActivity.addToRequestQueue(request);
	}

}

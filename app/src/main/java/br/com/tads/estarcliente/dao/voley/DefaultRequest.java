package br.com.tads.estarcliente.dao.voley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class DefaultRequest<T> extends Request<T> {

	private Listener<T> listener;
	private Map<String, String> params;
	private Context context;
	private Class<T> type;


	public DefaultRequest(Class<T> type,String url, Map<String, String> params,
						  Listener<T> reponseListener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
		this.type = type;
	}

	public DefaultRequest(Class<T> type,Context context, String url,
						  Map<String, String> params, Listener<T> reponseListener,
						  ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.context = context;
		this.listener = reponseListener;
		this.params = params;
		this.type = type;
	}

	public DefaultRequest(Class<T> type,int method, String url, Map<String, String> params,
						  Listener<T> reponseListener, ErrorListener errorListener) {
		super(method, url, errorListener);
		
		this.listener = reponseListener;
		this.params = params;
		this.type = type;
	}

	public DefaultRequest(Class<T> type,Context context, int method, String url, Map<String, String> params,
						  Listener<T> reponseListener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.context = context;
		this.listener = reponseListener;
		this.params = params;
		this.type = type;
	}

	protected Map<String, String> getParams()
			throws AuthFailureError {
		return params;
	};

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		Log.w(" response", " NetworkResponse response");
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			if(jsonString.length() == 0){
				return Response.error(new ParseError());
			}
		    T myclass = (T) new Gson().fromJson(jsonString, type);
			return  Response.success(myclass, HttpHeaderParser.parseCacheHeaders(response));//Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			SmarterLogMAKER.e("ParseError",e);
			return Response.error(new ParseError(e));
		} catch (Exception je) {
			SmarterLogMAKER.e("ParseError", je);
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(T response) {
		Log.w(" response", " deliverResponse");
		listener.onResponse(response);
	}



}

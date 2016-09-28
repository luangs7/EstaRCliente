package br.com.tads.estarcliente.dao.custom;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import br.com.tads.estarcliente.dao.voley.DefaultRequest;


/**
 * Created by anderson on 05/11/2015.
 */
public class CustomPostResquest<T> extends DefaultRequest {
    public CustomPostResquest(Class<T> type, String url, Map<String, String> params, Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(type,url, params, reponseListener, errorListener);
    }

    public CustomPostResquest(Class<T> type, Context context, String url, Map<String, String> params, Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(type,context, url, params, reponseListener, errorListener);
    }

    public CustomPostResquest(Class<T> type, int method, String url, Map<String, String> params, Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(type,method, url, params, reponseListener, errorListener);
    }

    public CustomPostResquest(Class<T> type, Context context, int method, String url, Map<String, String> params, Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(type,context, method, url, params, reponseListener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        //headers.put("Accept", "application/json");
        headers.put("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        //headers.put("charset", "UTF-8");
        //headers.put("token","djs&&iofjsaiofjsaiofjasdoij90!&*@#8971jdjoidjsoiau");
        return headers;
    }

}

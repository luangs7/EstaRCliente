package br.com.tads.estarcliente.dao.voley;

import android.content.Context;

import com.android.volley.Request;


/**
 * Created by renato on 24/02/2015.
 */
public class GerenicAbstractDaoImp extends GenericAbstractDao {

    public GerenicAbstractDaoImp(Context context) {
        super(context);
    }

    @Override
    protected void addRequest(Request request) {
        SmarterLogMAKER.w("Making request "+ getRequestType(request.getMethod()) +" to: "+request.getUrl());
        super.addRequest(request);
    }

    public void get(CallListener callListener, String url) {
        //url = serverUrl + url;
        //DefaultRequest request = new DefaultRequest(User.class,Request.Method.GET, url, null, callListener, callListener);
        //addRequest(request);
    }


    private String getRequestType(int id) {
        String type = new String();
        switch (id) {
            case 0:
                type = "GET";
                break;
            case 1:
                type = "POST";
                break;
            case 2:
                type = "PUT";
                break;
            case 3:
                type = "DELETE";
                break;
            default:
                type = "UNKNOWN";
        }
        return type;
    }
}


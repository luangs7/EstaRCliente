package br.com.tads.estarcliente.dao.voley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.text.Normalizer;

import br.com.tads.estarcliente.model.request.BaseRequest;


public abstract class CallListener<T extends BaseRequest> implements ErrorListener, Listener<T>, VolleyOnPreExecute {
    public Context context;
    protected Gson gson = new Gson();
    protected ProgressDialog progressDialog;
   // protected FlatProgressDialog progressDialog;

    protected final String defaultErrorMessage = "Houve um erro, por favor tente novamente.";
    protected final String defaultErrorTitle = "Problema de conexão";
    protected final String defaultSuccessButtonMessage = "Tentar novamente";
    protected final String defaultErrorButtonMessage = "Cancelar";
    protected final String defaultMessage = "Baixando dados";
    protected OnDialogButtonClick onDialogButtonClick;
    public DefaultDialog errorDialog;

    public T response;

    public CallListener() {
        onPreExecute();
    }


    public CallListener(Context context) {
        this();
        this.context = context;
    }

    public CallListener(Context context, OnDialogButtonClick onDialogButtonClick) {
        this.onDialogButtonClick = onDialogButtonClick;
        this.context = context;

        if(context instanceof Activity)
            progressDialog = new ProgressDialog(context);
        if(progressDialog != null){
            progressDialog.setMessage(defaultMessage);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }

        onPreExecute();
    }

    public CallListener(Context context, String message) {
        this.context = context;
        if(context instanceof Activity)
            progressDialog = new ProgressDialog(context);
        if(progressDialog != null){
            if (message != null) {
                //dialog = new ProgressDialog(activity);
                progressDialog.setMessage(message);
            }else{
                progressDialog.setMessage(defaultMessage);
            }
            progressDialog.setCancelable(false);
        }
        onPreExecute();
    }


    public CallListener(Context context, String message, OnDialogButtonClick onDialogButtonClick) {
        this.onDialogButtonClick = onDialogButtonClick;
        this.context = context;
        if(context instanceof Activity && message != null && message.length() > 0)
            progressDialog = new ProgressDialog(context);

        if(progressDialog != null){
            if (message != null) {
            /*dialog = new ProgressDialog(activity);*/
                progressDialog.setMessage(message);
            }else{
                progressDialog.setMessage(defaultMessage);
            }
            progressDialog.setCancelable(false);
        }

        onPreExecute();
    }
    String errorMsg = "";
    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            SmarterLogMAKER.w("Error: " + responseBody);
        } catch (Exception e) {
        }

        Log.w(" erro volley", " erro");
        errorMsg = "";
        if (error instanceof ParseError) {
            errorMsg = "ParseError";
        }else
        if (error instanceof TimeoutError) {
            errorMsg = "TimeoutError";
        } else if (error instanceof ServerError) {
            errorMsg = "isServerProblem";
        } else if (error instanceof AuthFailureError) {
            errorMsg = "Authentication error";
        } else if (error instanceof NetworkError || error instanceof NoConnectionError) {
            errorMsg = "isNetworkProblem";
        }
        //Log.w("activity on calllistener", activity.getClass().getSimpleName() + "");
        Log.w(" erro volley", errorMsg);

        try {
            SmarterLogMAKER.w("http error code " + error.networkResponse.statusCode);
        } catch (Exception e) {
        }
        onPostExecute();
    }

    public void onPreExecute() {
        try {
            /*if (dialog != null) {
                dialog.show();
            }*/
            if (progressDialog != null) {
                progressDialog.show();
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();;
        }
    }

    protected void onPostExecute() {
        removeDialog();
        try {
            if ((response == null)) {
                createDefaultDialog();
            }
        }catch (Exception ex){
            createDefaultDialog();
        }

    }

    @Override
    public void onResponse(T object) {
        try {
            response = object;
            onPostExecute();
            SmarterLogMAKER.w("Sucess: ");
        } catch (Exception e) {
            onPostExecute();
            e.printStackTrace();
        }
    }

    public void removeDialog() {
        try {
            //dialog.dismiss();
            if(progressDialog != null)
                progressDialog.dismiss();
        } catch (Exception e) {
        }
    }


    public void createDefaultDialog() {
        try {
            if(onDialogButtonClick != null){
                if(context instanceof Activity){
                    if("ParseError".equalsIgnoreCase(errorMsg)){
                        errorDialog = DefaultDialog.newInstance("Erro de servidor", defaultErrorMessage, defaultSuccessButtonMessage, defaultErrorButtonMessage, onDialogButtonClick);
                        errorDialog.show(((Activity) context).getFragmentManager(), null);
                    }else{
                        errorDialog = DefaultDialog.newInstance(defaultErrorTitle, defaultErrorMessage, defaultSuccessButtonMessage, defaultErrorButtonMessage, onDialogButtonClick);
                        errorDialog.show(((Activity) context).getFragmentManager(), null);
                    }

                }
            }

        } catch (Exception e) {
            SmarterLogMAKER.w("Error trying to create dialog. Please check your activity");
        }
    }

    public static String removerAcentos(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }


}

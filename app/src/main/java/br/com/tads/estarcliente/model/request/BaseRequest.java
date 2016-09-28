package br.com.tads.estarcliente.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.com.tads.estarcliente.dao.voley.DataResponse;


/**
 * Created by anderson on 05/11/2015.
 */
public class BaseRequest extends DataResponse {


    @Expose
    @SerializedName("exception")
    private String exception;

    @Expose
    @SerializedName("result")
    private String result;

    public String getException() {
        if(exception == null)
            return "";
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean success() {
        try {
            return result.equalsIgnoreCase("1");
        }catch (Exception ex) {
            return false;
        }
    }
}

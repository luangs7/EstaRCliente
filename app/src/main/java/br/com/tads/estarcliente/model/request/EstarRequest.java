package br.com.tads.estarcliente.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.tads.estarcliente.model.Usuario;

/**
 * Created by Dev_Maker on 26/09/2016.
 */
public class EstarRequest extends BaseRequest {

    @Expose
    @SerializedName("id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

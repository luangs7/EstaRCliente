package br.com.tads.estarcliente.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.tads.estarcliente.model.Usuario;

/**
 * Created by Dev_Maker on 14/09/2016.
 */
public class UserRequest extends BaseRequest{
    @Expose
    @SerializedName("content")
    public List<Usuario> content;

//    @Expose
//    @SerializedName("monitorias")
//    public List<Usuario> monitorias;


    public List<Usuario> getItens() {
        return content;
    }

    public void setItens(List<Usuario> itens) {
        this.content = itens;
    }
}

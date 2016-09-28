package br.com.tads.estarcliente.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.Veiculo;

/**
 * Created by Dev_Maker on 14/09/2016.
 */
public class VeiculoRequest extends BaseRequest{
    @Expose
    @SerializedName("content")
    public List<Veiculo> content;

//    @Expose
//    @SerializedName("monitorias")
//    public List<Usuario> monitorias;


    public List<Veiculo> getItens() {
        return content;
    }

    public void setItens(List<Veiculo> itens) {
        this.content = itens;
    }
}

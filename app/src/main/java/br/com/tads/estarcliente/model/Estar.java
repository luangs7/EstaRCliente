package br.com.tads.estarcliente.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dev_Maker on 21/09/2016.
 */
public class Estar implements Serializable {

    private String endereco;
    private String placa;
    private int horas;
    @SerializedName("idUser")
    @Expose
    private int idUser;
    private Double latitude;
    private Double longitude;
    private int alert;
    @SerializedName("idEstar")
    @Expose
    private String id;

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        switch (horas){
            case 0:
                this.horas = 1;
                break;
            case 1:
                this.horas = 2;
                break;
            default:
                this.horas = 0;
        }

    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package br.com.tads.estarcliente.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dev_Maker on 21/09/2016.
 */
public class Estar implements Serializable {

    private int alert;
    @SerializedName("idUser")
    @Expose
    private int idUser;
    @SerializedName("idEstar")
    @Expose
    private String id;
    @SerializedName("placa")
    @Expose
    private String placa;
    @SerializedName("inicio")
    @Expose
    private String inicio;
    @SerializedName("horas")
    @Expose
    private int horas;
    @SerializedName("alerta")
    @Expose
    private Object alerta;
    @SerializedName("Usuario_id")
    @Expose
    private String usuarioId;
    @SerializedName("valor")
    @Expose
    private Object valor;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("Endereco_idEndereco")
    @Expose
    private String enderecoIdEndereco;
    @SerializedName("numero")
    @Expose
    private Object numero;
    @SerializedName("address")
    @Expose
    private String endereco;
    @SerializedName("situacao")
    @Expose
    private String situacao;
    @SerializedName("diff")
    @Expose
    private String diff;
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("hours")
    @Expose
    private Integer hours;
    private boolean renovado;

    public boolean isRenovado() {
        return renovado;
    }

    public void setRenovado(boolean renovado) {
        this.renovado = renovado;
    }

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
                this.horas = 2;
                break;
            case 1:
                this.horas = 3;
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

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public Object getAlerta() {
        return alerta;
    }

    public void setAlerta(Object alerta) {
        this.alerta = alerta;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public String getEnderecoIdEndereco() {
        return enderecoIdEndereco;
    }

    public void setEnderecoIdEndereco(String enderecoIdEndereco) {
        this.enderecoIdEndereco = enderecoIdEndereco;
    }

    public Object getNumero() {
        return numero;
    }

    public void setNumero(Object numero) {
        this.numero = numero;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}

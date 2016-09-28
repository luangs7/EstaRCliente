        package br.com.tads.estarcliente.model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Veiculo {

    @SerializedName("placa")
    @Expose
    private String placa;
    @SerializedName("modelo")
    @Expose
    private String modelo;
    @SerializedName("cor")
    @Expose
    private String cor;
    @SerializedName("ano")
    @Expose
    private String ano;
    @SerializedName("Usuario_id")
    @Expose
    private String usuarioId;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    /**
     *
     * @return
     * The placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     *
     * @param placa
     * The placa
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     *
     * @return
     * The modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     *
     * @param modelo
     * The modelo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     *
     * @return
     * The cor
     */
    public String getCor() {
        return cor;
    }

    /**
     *
     * @param cor
     * The cor
     */
    public void setCor(String cor) {
        this.cor = cor;
    }

    /**
     *
     * @return
     * The ano
     */
    public String getAno() {
        return ano;
    }

    /**
     *
     * @param ano
     * The ano
     */
    public void setAno(String ano) {
        this.ano = ano;
    }

    /**
     *
     * @return
     * The usuarioId
     */
    public String getUsuarioId() {
        return usuarioId;
    }

    /**
     *
     * @param usuarioId
     * The Usuario_id
     */
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTipo(int position) {
        switch(position){
            case 0:
                this.tipo = "Carro";
                break;
            case 1:
                this.tipo = "Moto";
                break;
        }
        this.tipo = tipo;
    }
}
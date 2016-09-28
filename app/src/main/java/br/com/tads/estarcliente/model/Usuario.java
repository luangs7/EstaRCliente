
package br.com.tads.estarcliente.model;
import com.google.gson.annotations.SerializedName;


        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("googleId")
    @Expose
    private Object googleId;
    @SerializedName("saldo")
    @Expose
    private String saldo;
    private boolean logado = false;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The nome
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     * The nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The googleId
     */
    public Object getGoogleId() {
        return googleId;
    }

    /**
     *
     * @param googleId
     * The googleId
     */
    public void setGoogleId(Object googleId) {
        this.googleId = googleId;
    }

    /**
     *
     * @return
     * The saldo
     */
    public String getSaldo() {
        return saldo;
    }

    /**
     *
     * @param saldo
     * The saldo
     */
    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public void setSaldo(String saldo,int horas) {
        int saldoatual = Integer.valueOf(saldo);

        this.saldo = String.valueOf(saldoatual - horas);
    }

    public boolean isLogado() {
        return logado;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }
}
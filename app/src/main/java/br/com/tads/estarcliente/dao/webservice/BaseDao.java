package br.com.tads.estarcliente.dao.webservice;


import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.util.HashMap;

import br.com.tads.estarcliente.dao.custom.CustomPostResquest;
import br.com.tads.estarcliente.dao.custom.CustomResquest;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.GerenicAbstractDaoImp;
import br.com.tads.estarcliente.model.Estar;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.Veiculo;
import br.com.tads.estarcliente.model.request.BaseRequest;
import br.com.tads.estarcliente.model.request.EstarRequest;
import br.com.tads.estarcliente.model.request.UserRequest;
import br.com.tads.estarcliente.model.request.VeiculoRequest;


/**
 * Created by luan on 05/04/2016
 */
public class BaseDao extends GerenicAbstractDaoImp {

    public BaseDao(Context context) {
        super(context);
    }

    Usuario user = new LocalDbImplement<Usuario>(context).getDefault(Usuario.class);

    public void getVeiculos(CallListener callListener) {
        String url = serverUrl + "getters/getVeiculos.php?id="+user.getId();
        CustomResquest request = new CustomResquest(VeiculoRequest.class, Request.Method.GET, url, null, callListener, callListener);
        addRequest(request);
    }

//
    public void login(CallListener callListener, String pass, String email) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pass", pass);
        map.put("email", email);

        String url = serverUrl + "safe/loginmobile.php";

        CustomPostResquest request = new CustomPostResquest(UserRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }
    public void loginGoogle(CallListener callListener, String token, String email, String name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("GoogleID", token);
        map.put("Nome",name);
        map.put("email", email);

        String url = serverUrl + "safe/loginGoogle.php";

        CustomPostResquest request = new CustomPostResquest(UserRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }

    public void cadastro(CallListener callListener, String nome, String email, String senha ) {
//        new BaseDao(getBaseContext()).cadastro(callListener,editTextNome.getText().toString(),editTextEnd.getText().toString(),editTextCidade.getText().toString(),editCPF.getText().toString(),editTextTelefone.getText().toString(),editTextEmail.getText().toString(),editTextSenha.getText().toString());

        HashMap<String, String> map = new HashMap<>();
        map.put("Nome", nome);
        map.put("Email", email);
        map.put("Senha", senha);

        String url = serverUrl + "setters/addUser.php";

        CustomPostResquest request = new CustomPostResquest(BaseRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }

    public void addVeiculo(CallListener callListener, Veiculo veiculo) {
//        new BaseDao(getBaseContext()).cadastro(callListener,editTextNome.getText().toString(),editTextEnd.getText().toString(),editTextCidade.getText().toString(),editCPF.getText().toString(),editTextTelefone.getText().toString(),editTextEmail.getText().toString(),editTextSenha.getText().toString());

        HashMap<String, String> map = new HashMap<>();
        map.put("Placa", veiculo.getPlaca());
        map.put("modelo", veiculo.getModelo());
        map.put("tipo", veiculo.getTipo());
        map.put("id", user.getId());


        String url = serverUrl + "setters/addVeiculo.php";

        CustomPostResquest request = new CustomPostResquest(VeiculoRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }
    public void addSaldo(CallListener callListener, String valor) {

        HashMap<String, String> map = new HashMap<>();
        map.put("horas", valor);

        String url = serverUrl + "setters/addSaldo.php?id="+user.getId();

        CustomPostResquest request = new CustomPostResquest(UserRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }

    public void addEstar(CallListener callListener, Estar estar) {
//        new BaseDao(getBaseContext()).cadastro(callListener,editTextNome.getText().toString(),editTextEnd.getText().toString(),editTextCidade.getText().toString(),editCPF.getText().toString(),editTextTelefone.getText().toString(),editTextEmail.getText().toString(),editTextSenha.getText().toString());

        HashMap<String, String> map = new HashMap<>();
        map.put("placa", estar.getPlaca());
        map.put("horas", String.valueOf(estar.getHoras()));
        map.put("address", estar.getEndereco());
        map.put("longitude", String.valueOf(estar.getLongitude()));
        map.put("latitude", String.valueOf(estar.getLatitude()));



        String url = serverUrl + "setters/addAtivar.php?id="+user.getId();

        CustomPostResquest request = new CustomPostResquest(EstarRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }

    public void addRenovar(CallListener callListener, Estar estar) {
//        new BaseDao(getBaseContext()).cadastro(callListener,editTextNome.getText().toString(),editTextEnd.getText().toString(),editTextCidade.getText().toString(),editCPF.getText().toString(),editTextTelefone.getText().toString(),editTextEmail.getText().toString(),editTextSenha.getText().toString());
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<>();
        map.put("estar", gson.toJson(estar));

        String url = serverUrl + "setters/updRenovar.php?id="+user.getId();

        CustomPostResquest request = new CustomPostResquest(EstarRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }

    public void sendPush(CallListener callListener, String deviceid, String token, String firebaseToken){
        String url = serverUrl + "setters/addPush.php?UserId=" + user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("DeviceId",deviceid);
        map.put("Token",firebaseToken);

        CustomResquest request = new CustomResquest(BaseRequest.class, Request.Method.POST, url, map, callListener, callListener);
        addRequest(request);
    }

    public void updStatus(CallListener callListener, Estar estar){
        String url = serverUrl + "setters/updStatus.php?id=" + estar.getId();

        CustomResquest request = new CustomResquest(BaseRequest.class, Request.Method.GET, url, null, callListener, callListener);
        addRequest(request);
    }
}


package br.com.tads.estarcliente;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.tads.estarcliente.adapter.VeiculoAdapter;
import br.com.tads.estarcliente.alarm.CountDown;
import br.com.tads.estarcliente.alarm.CountDownBehavior;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.OnDialogButtonClick;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.Estar;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.Veiculo;
import br.com.tads.estarcliente.model.request.BaseRequest;
import br.com.tads.estarcliente.model.request.EstarRequest;
import br.com.tads.estarcliente.model.request.VeiculoRequest;

public class AtivarActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    Toolbar toolbar;
    RelativeLayout relativeAlert;
    List<Veiculo> listaVeiculos = new ArrayList<Veiculo>();
    Spinner listVeiculos,tempos;
    VeiculoAdapter adapterVeiculo;
    EditText editAlerta;
    Button btnAtivar;
    public static final int  REQUEST_PERMISSIONS_CODE = 128;
    private GoogleApiClient mGoogleApiClient;
    Location lastLocation;
    String error = "";
    ProgressDialog dialog;
    Estar estar;
    int alert;

    private TextView textView;
    private CountDown countDown;
    NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    NotificationManager mNotifyMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativar);
        getdata();

        dialog = new ProgressDialog(this);

        relativeAlert = (RelativeLayout) findViewById(R.id.relativeAlert);
        listVeiculos = (Spinner) findViewById(R.id.spinnerVeiculo);
        tempos = (Spinner) findViewById(R.id.spinnerTempo);
        editAlerta = (EditText) findViewById(R.id.editAlerta);
        btnAtivar = (Button) findViewById(R.id.btnConfirm);

        editAlerta.setClickable(false);
        editAlerta.setEnabled(false);

        List<String> veiculos = new ArrayList<>();
        veiculos.add("1 hora");
        veiculos.add("2 horas");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, veiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tempos.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Ativar EstaR");
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            toolbar.setNavigationIcon(R.drawable.ic_action_name);
            setSupportActionBar(toolbar);
        }


        relativeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AtivarActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_alerts);

                Button btnOk = (Button) dialog.findViewById(R.id.btncloseDialog);
                final CheckBox check15 = (CheckBox) dialog.findViewById(R.id.checkBox15);
                final CheckBox check5 = (CheckBox) dialog.findViewById(R.id.checkBox5);
                final CheckBox checknever = (CheckBox) dialog.findViewById(R.id.checkBoxNot);


                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(check5.isChecked() || check15.isChecked() || checknever.isChecked()) {
                            String option = "";
                            if (check15.isChecked()) {
                                check5.setChecked(false);
                                checknever.setChecked(false);
                                option = "10 minutos antes.";
                                alert = 10;
                            }
                            if (check5.isChecked()) {
                                check15.setChecked(false);
                                checknever.setChecked(false);
                                option = "5 minutos antes.";
                                alert = 5;
                            }
                            if (checknever.isChecked()) {
                                check5.setChecked(false);
                                check15.setChecked(false);
                                option = "Não receber nenhum alerta.";
                                alert = 0;
                            }

                            editAlerta.setText(option);
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(AtivarActivity.this, "Selecione ao menos 1 opção de alerta!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                dialog.show();
            }
        });


        btnAtivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        dialog.setCancelable(false);
        dialog.setMessage("Encontrando sua posição atual...");
        dialog.show();

        callConnection();
            }
        });

    }


    public void getdata(){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                getdata();
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<VeiculoRequest> callListener = new CallListener<VeiculoRequest>(this, "Buscando dados", onDialogButtonClick) {
            @Override
            public void onResponse(VeiculoRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    if (request != null && request.getItens() != null && request.getItens().size() > 0) {
                        listaVeiculos.clear();
                        listaVeiculos.addAll(request.getItens());

                        adapterVeiculo = new VeiculoAdapter(getBaseContext(),listaVeiculos);


                        ArrayAdapter<Veiculo> adapter = new ArrayAdapter<Veiculo>(getBaseContext(),
                                android.R.layout.simple_spinner_item, listaVeiculos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listVeiculos.setAdapter(adapterVeiculo);


                    }else {
                        btnAtivar.setEnabled(false);
                        Toast.makeText(AtivarActivity.this, "Sem veículos cadastrados!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AtivarActivity.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).getVeiculos(callListener);
    }

    private synchronized void callConnection(){
        Log.i("LOG", "LastLocationActivity.callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );

            }else{
                lastLocation = LocationServices
                        .FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
        }else{
            lastLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Add a marker in Sydney and move the camera
        if(lastLocation != null) {
            LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            String address = getEndereco();
            Log.d("address",address);

            estar = new Estar();
            estar.setPlaca(listaVeiculos.get(listVeiculos.getSelectedItemPosition()).getPlaca());
            estar.setEndereco(address);
            estar.setHoras(tempos.getSelectedItemPosition());
            estar.setLatitude(lastLocation.getLatitude());
            estar.setLongitude(lastLocation.getLongitude());
            estar.setAlert(alert);

//            estar.setIdUser();
            dialog.dismiss();
            setdata(estar);
        }else{
            Toast.makeText(getBaseContext(), "Sua localização não foi encontrada. Verifique se seu celular está funcionando corretamente.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finishAffinity();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public String getEndereco(){
        List<Address> list = new ArrayList<Address>();
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        String resultAddress = "";

        try {
            list = (ArrayList<Address>) geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
            //example retorno Address[addressLines=[0:"Rua Voluntários da Pátria, 250 - Centro",1:"Curitiba - PR",2:"Brasil"],feature=250,admin=Paraná,sub-admin=null,locality=Curitiba,thoroughfare=Rua Voluntários da Pátria,postalCode=80020,countryCode=BR,countryName=Brasil,hasLatitude=true,latitude=-25.4327455,hasLongitude=true,longitude=-49.2742322,phone=null,url=null,extras=null]
            if(list != null && list.size() > 0){
                Address a = list.get(0);
                for(int i = 0, tam = a.getMaxAddressLineIndex(); i < tam; i++){
                    resultAddress += a.getAddressLine(i);
                    resultAddress += i < tam - 1 ? ", " : "";
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            //error = "Network problem";
            try {
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                        String.valueOf(lastLocation.getLatitude()) +"," + String.valueOf(lastLocation.getLongitude());
                JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET,url,future,future);
                request.setRetryPolicy(new DefaultRetryPolicy(1000 * 20, 1, 1f));
                Volley.newRequestQueue(getBaseContext()).add(request);
                JSONObject response = future.get();
                if("OK".equalsIgnoreCase(response.getString("status"))){
                    JSONArray array = response.getJSONArray("results");
                    resultAddress = array.getJSONObject(0).getString("formatted_address");
                }else{
                    error = "parse error";
                }
            }catch (Exception ex){
                e.printStackTrace();
                error = "Network problem";
            }
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            error = "Illegal arguments";
        }

        return resultAddress;
    }

    public void setdata(final Estar estar){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                setdata(estar);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<EstarRequest> callListener = new CallListener<EstarRequest>(this, "Buscando dados", onDialogButtonClick) {
            @Override
            public void onResponse(EstarRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    Usuario usuario = new LocalDbImplement<Usuario>(AtivarActivity.this).getDefault(Usuario.class);
                    new LocalDbImplement<Usuario>(AtivarActivity.this).clearObject(Usuario.class);
                    usuario.setSaldo(usuario.getSaldo(),estar.getHoras());
                    new LocalDbImplement<Usuario>(getBaseContext()).save(usuario);
                    Toast.makeText(AtivarActivity.this,"Ativado com sucesso.", Toast.LENGTH_SHORT).show();

                    estar.setId(request.getId());
                    Intent it = new Intent(getBaseContext(),TimeActivity.class);
                    it.putExtra("estar",estar);
                    startActivity(it);

                }else{
                    Toast.makeText(AtivarActivity.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).addEstar(callListener,estar);
    }



//    public void countDownMethod(int horas){
//        mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("EstaR")
//                        .setContentText("Tempo restante")
//                        .setOngoing(true);
//
//
//        // Gets an instance of the NotificationManager service
//        mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // Builds the notification and issues it.
//
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());
//
//
//        long seconds = 0;
//
//        switch (horas){
//            case 1:
//                seconds = 3600;
//                break;
//            case 2:
//                seconds = 7200;
//                break;
//        }
//
//        //Cria o contador com 1 minuto
//        countDown = new CountDown(seconds);
//
//        //Cria e atribui um CountDownBehavior ao contador
//        countDown.setCountDownListener(new CountDownBehavior(15, "mm:ss") {
//            @Override
//            public void onEnd() {
//                Toast.makeText(AtivarActivity.this, "terminou", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            protected void onAlarm() {
//                Toast.makeText(AtivarActivity.this, "alarme", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            protected void displayTimeLeft(String timeLeft) {
//                mBuilder.setContentText(timeLeft + " restante");
//                mNotifyMgr.notify(mNotificationId, mBuilder.build());
//            }
//        });
//
//        countDown.start();
//    }
////
////    protected void startClick(View v){
////        countDown.start();
////    }
////
////    protected void addClick(View v){
////        countDown.increaseBy(60);
////    }
////
////    protected void stopClick(View v){
////        countDown.stop();
////    }
////
////    protected void resumeClick(View v){
////        countDown.resume();
////    }
//
//    @Override
//    protected void onDestroy() {
//        //Antes de sair deve parar o contador caso este esteja a contar
//        if(countDown.isCounting()){
//            countDown.stop();
//        }
//        mBuilder.setOngoing(false);
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());
//
//        super.onDestroy();
//    }
}


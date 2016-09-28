package br.com.tads.estarcliente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.tads.estarcliente.adapter.VeiculoAdapter;
import br.com.tads.estarcliente.dao.EstarDao;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.OnDialogButtonClick;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.Veiculo;
import br.com.tads.estarcliente.model.request.BaseRequest;
import br.com.tads.estarcliente.model.request.VeiculoRequest;

public class VeiculoActivity extends AppCompatActivity {

    Button addVeiculo;
    EditText editPlaca, editModelo;
    Spinner tipoVeiculo;
    ListView listVeiculos;
    VeiculoAdapter adapterVeiculo;
    List<Veiculo> listaVeiculos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo);

        getdata();

        tipoVeiculo = (Spinner) findViewById(R.id.spinnerVeiculo);
        addVeiculo = (Button) findViewById(R.id.buttonAdd);
        listVeiculos = (ListView) findViewById(R.id.listVeiculos);
        editPlaca = (EditText) findViewById(R.id.editTextModelo);
        editModelo = (EditText) findViewById(R.id.editTextPlacas);


        List<String> veiculos = new ArrayList<>();
        veiculos.add("Carro");
        veiculos.add("Moto");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, veiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoVeiculo.setAdapter(adapter);

        addVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPlaca.getText().length() > 0 && editModelo.getText().length() > 0) {
                    Veiculo veiculo = new Veiculo();
                    veiculo.setPlaca(editPlaca.getText().toString());
                    veiculo.setModelo(editModelo.getText().toString());
                    veiculo.setTipo(tipoVeiculo.getSelectedItemPosition());
                    addData(veiculo);
                }else{
                    Toast.makeText(VeiculoActivity.this, "Insira todos os dados.", Toast.LENGTH_SHORT).show();
                }
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
                        listVeiculos.setAdapter(adapterVeiculo);

                    }else {
                        Toast.makeText(VeiculoActivity.this, "Sem veículos cadastrados!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(VeiculoActivity.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).getVeiculos(callListener);
    }

    public void addData(final Veiculo veiculo){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                getdata();
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<VeiculoRequest> callListener = new CallListener<VeiculoRequest>(this, "Enviando dados", onDialogButtonClick) {
            @Override
            public void onResponse(VeiculoRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    if (request != null && request.getItens() != null && request.getItens().size() > 0) {
                        EstarDao estarDAO = new EstarDao(context);
                        listaVeiculos.clear();
                        listaVeiculos.addAll(request.getItens());
                        editPlaca.setText("");
                        editModelo.setText("");
                        adapterVeiculo = new VeiculoAdapter(getBaseContext(),listaVeiculos);
                        adapterVeiculo.notifyDataSetChanged();


//                        Gson gson = new Gson();
//                        String json = gson.toJson(veiculo);
//                        estarDAO.saveVeiculo(json);

                    }else {
                        Toast.makeText(VeiculoActivity.this, "Sem veículos cadastrados!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(VeiculoActivity.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).addVeiculo(callListener,veiculo);
    }
}

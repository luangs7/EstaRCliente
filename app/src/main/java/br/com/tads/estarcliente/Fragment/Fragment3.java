package br.com.tads.estarcliente.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.VeiculoActivity;
import br.com.tads.estarcliente.adapter.VeiculoAdapter;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.OnDialogButtonClick;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.Veiculo;
import br.com.tads.estarcliente.model.request.VeiculoRequest;


public class Fragment3 extends Fragment {
    Button addVeiculo;
    EditText editPlaca, editModelo;
    Spinner tipoVeiculo;
    ListView listVeiculos;
    VeiculoAdapter adapterVeiculo;
    List<Veiculo> listaVeiculos = new ArrayList<>();
    Usuario user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment3, container, false);
        user = new LocalDbImplement<Usuario>(getContext()).getDefault(Usuario.class);



        requesVolleyGet();

        tipoVeiculo = (Spinner) view.findViewById(R.id.spinnerVeiculo);
        addVeiculo = (Button) view.findViewById(R.id.buttonAdd);
        listVeiculos = (ListView) view.findViewById(R.id.listVeiculos);
        editPlaca = (EditText) view.findViewById(R.id.editTextModelo);
        editModelo = (EditText) view.findViewById(R.id.editTextPlacas);



        List<String> veiculos = new ArrayList<>();
        veiculos.add("Carro");
        veiculos.add("Moto");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
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
                    Toast.makeText(getContext(), "Insira todos os dados.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



//    public void getdata(){
//
//        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
//            @Override
//            public void onPositiveClick() {
//                getdata();
//            }
//
//            @Override
//            public void onNegativeClick() {
//
//            }
//        };
//
//        CallListener<VeiculoRequest> callListener = new CallListener<VeiculoRequest>(getContext(), "Buscando dados", onDialogButtonClick) {
//            @Override
//            public void onResponse(VeiculoRequest request) {
//                super.onResponse(request);
//                if (request.success()) {
//                    if (request != null && request.getItens() != null && request.getItens().size() > 0) {
//                        listaVeiculos.clear();
//                        listaVeiculos.addAll(request.getItens());
//
//                        adapterVeiculo = new VeiculoAdapter(getContext(),listaVeiculos);
//                        listVeiculos.setAdapter(adapterVeiculo);
//
//                    }else {
//                        Toast.makeText(getContext(), "Sem veículos cadastrados!", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(getContext(), request.getException(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                super.onErrorResponse(error);
//            }
//        };
//
//
//        new BaseDao(getContext()).getVeiculos(callListener);
//    }

    public void addData(final Veiculo veiculo){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                addData(veiculo);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<VeiculoRequest> callListener = new CallListener<VeiculoRequest>(getContext(), "Enviando dados", onDialogButtonClick) {
            @Override
            public void onResponse(VeiculoRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    if (request != null && request.getItens() != null && request.getItens().size() > 0) {

                        listaVeiculos.clear();
                        listaVeiculos.addAll(request.getItens());

                        //adapterVeiculo = new VeiculoAdapter(getContext(),listaVeiculos);
                        adapterVeiculo.notifyDataSetChanged();

                    }else {
                        Toast.makeText(getContext(), "Sem veículos cadastrados!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getContext()).addVeiculo(callListener,veiculo);
    }

    public void requesVolleyGet(){
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setCancelable(false);
//        dialog.setMessage("Atualizando dados...");
//        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://wsestarapp.esy.es/getters/getVeiculos.php?id=" + user.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listaVeiculos.clear();
                            String json = response.get("content").toString();
                            Gson gson = new Gson();
                            Type type1 = new TypeToken<ArrayList<Veiculo>>() {}.getType();
                            ArrayList<Veiculo> bckp = new ArrayList<>();
                            bckp = gson.fromJson(json,type1);
                            listaVeiculos.addAll(bckp);
                            Log.d("return", response.toString());

                        }catch (Exception e){
                            listaVeiculos.clear();
                        }
                        //listaVeiculos.addAll(response.get());

                        adapterVeiculo = new VeiculoAdapter(getContext(),listaVeiculos);
                        listVeiculos.setAdapter(adapterVeiculo);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("return", "Error: " + error.getMessage());
                // textView.setText(error.getMessage());
                //dialog.dismiss();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(1000 * 15, 0, 1f));
        Volley.newRequestQueue(getContext()).add(request);
    }

}

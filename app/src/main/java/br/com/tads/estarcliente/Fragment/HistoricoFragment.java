package br.com.tads.estarcliente.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.adapter.HistoricoAdapter;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.model.Estar;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.Veiculo;


public class HistoricoFragment extends Fragment  {


    List<Estar> list = new ArrayList<>();
    HistoricoAdapter adapter;
    private android.widget.ListView listView2;
    Usuario user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = new LocalDbImplement<Usuario>(getContext()).getDefault(Usuario.class);

        View view = inflater.inflate(R.layout.fragment_historico, container,false);
        this.listView2 = (ListView) view.findViewById(R.id.listView2);

        requesVolleyGet();


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void requesVolleyGet(){
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setCancelable(false);
//        dialog.setMessage("Atualizando dados...");
//        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://wsestarapp.esy.es/getters/getHistorico.php?idUser=" + user.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            list.clear();
                            String json = response.get("content").toString();
                            Gson gson = new Gson();
                            Type type1 = new TypeToken<ArrayList<Estar>>() {}.getType();
                            ArrayList<Estar> bckp = new ArrayList<>();
                            bckp = gson.fromJson(json,type1);
                            list.addAll(bckp);
                            Log.d("return", response.toString());

                        }catch (Exception e){
                            list.clear();
                        }
                        //listaVeiculos.addAll(response.get());

                        adapter = new HistoricoAdapter(getContext(),list);
                        listView2.setAdapter(adapter);
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

package br.com.tads.estarcliente.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import br.com.tads.estarcliente.LoginActivity;
import br.com.tads.estarcliente.MainActivity;
import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.VeiculoActivity;
import br.com.tads.estarcliente.adapter.VeiculoAdapter;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.OnDialogButtonClick;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.request.UserRequest;
import br.com.tads.estarcliente.model.request.VeiculoRequest;


public class Fragment2 extends Fragment {
    TextView saldo;
    CardView cardsimple,cardmidle,cardhigh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_fragment2, container, false);
        saldo = (TextView)view.findViewById(R.id.txtSaldo);
        atualizaSaldo();

        cardsimple = (CardView) view.findViewById(R.id.cardsimple);
        cardmidle = (CardView) view.findViewById(R.id.cardmedium);
        cardhigh = (CardView) view.findViewById(R.id.cardhigh);

        cardsimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirmar")
                        .setMessage("Deseja confirmar a compra de \n R$2,00?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                getdata("1");
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        cardmidle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirmar")
                        .setMessage("Deseja confirmar a compra de \n R$10,00?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                getdata("5");
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        cardhigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirmar")
                        .setMessage("Deseja confirmar a compra de \n R$20,00?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                getdata("10");
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        return view;
    }

    public void atualizaSaldo(){
        Usuario user = new LocalDbImplement<Usuario>(getContext()).getDefault(Usuario.class);
        String txt = user.getSaldo() + " horas";
        saldo.setText(txt);
    }

    public void getdata(final String horas){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                getdata(horas);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<UserRequest> callListener = new CallListener<UserRequest>(getContext(), "Buscando dados", onDialogButtonClick) {
            @Override
            public void onResponse(UserRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    if (request != null && request.getItens() != null && request.getItens().size() > 0) {
                        Usuario usuario = request.getItens().get(0);
                        new LocalDbImplement<Usuario>(getContext()).clearObject(Usuario.class);
                        new LocalDbImplement<Usuario>(getContext()).save(usuario);

                        atualizaSaldo();

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


       new BaseDao(getContext()).addSaldo(callListener,horas);
    }


    @Override
    public void onResume() {
        try{
            atualizaSaldo();
        }catch (Exception e){

        }
        super.onResume();
    }

    @Override
    public void onPause() {
        try{
            atualizaSaldo();
        }catch (Exception e){

        }        super.onPause();
    }
}

package br.com.tads.estarcliente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.OnDialogButtonClick;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.request.BaseRequest;

public class CadastroActivity extends AppCompatActivity {

    Button btnConfirma;
    EditText editTextNome,editTextEmail,editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSenha = (EditText) findViewById(R.id.editTextSenha);
        btnConfirma = (Button) findViewById(R.id.btnConfirma);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
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

        CallListener<BaseRequest> callListener = new CallListener<BaseRequest>(this, "Buscando dados", onDialogButtonClick) {
            @Override
            public void onResponse(BaseRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                }else{
                    Toast.makeText(CadastroActivity.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).cadastro(callListener,editTextNome.getText().toString(),editTextEmail.getText().toString(),editTextSenha.getText().toString());
    }
}

package br.com.tads.estarcliente;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.model.Estar;
import br.com.tads.estarcliente.model.Usuario;

public class SplashActivity extends AppCompatActivity {
    private final static int REQUEST_PERMISSIONS_CODE = 128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                            android.Manifest.permission.READ_PHONE_STATE
            },
                    REQUEST_PERMISSIONS_CODE);
        } else {
            open();
        }
//    open();
    }

    public void open() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();


//                Regra de, se tiver um estar rodando, sempre manterá logado
                Usuario user = new LocalDbImplement<Usuario>(SplashActivity.this).getDefault(Usuario.class);
                if(user != null){
                    Estar estar = new LocalDbImplement<Estar>(SplashActivity.this).getDefault(Estar.class);
                    if(estar!=null){
                        intent.setClass(SplashActivity.this, TimeActivity.class);
                    }else {
                        if (user.isLogado())
                            intent.setClass(SplashActivity.this, MainActivity.class);
                        else {
                            new LocalDbImplement<Usuario>(SplashActivity.this).clearObject(Usuario.class);
                            intent.setClass(SplashActivity.this, LoginActivity.class);
                        }
                    }
                }else{
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Favor habilitar a permissão para usar o aplicativo!", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        return;
                    } else if (permissions[i].equalsIgnoreCase(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Favor habilitar a permissão para usar o aplicativo!", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        return;
                    } else if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)
                            && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Favor habilitar a permissão para usar o aplicativo!", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        return;
                    } else if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_NETWORK_STATE)
                            && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Favor habilitar a permissão para usar o aplicativo!", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        return;
                    }else if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE)
                            && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Favor habilitar a permissão para usar o aplicativo!", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        return;
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        open();
    }

    }


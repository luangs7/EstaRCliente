package br.com.tads.estarcliente;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.dao.voley.CallListener;
import br.com.tads.estarcliente.dao.voley.OnDialogButtonClick;
import br.com.tads.estarcliente.dao.webservice.BaseDao;
import br.com.tads.estarcliente.model.Usuario;
import br.com.tads.estarcliente.model.request.BaseRequest;
import br.com.tads.estarcliente.model.request.UserRequest;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    EditText email,senha;
    Button buttonLogin,buttonCadastro;
    CheckBox checklogin;
    String token;
    GoogleSignInAccount acct;
    private android.widget.ImageView imageView;
    private android.support.v7.widget.CardView cardview;
    private TextView esquecisenha;
    private CheckBox checkLogado;
    private SignInButton signinbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

;
       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SignInButton button = (SignInButton) findViewById(R.id.sign_in_button);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonCadastro = (Button) findViewById(R.id.buttonCadastro);
        checklogin = (CheckBox) findViewById(R.id.checkLogado);
        setGooglePlusButtonText(button, "Login com Google");
        this.esquecisenha = (TextView) findViewById(R.id.esquecisenha);



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().length() > 0 && senha.getText().length() > 0) {
                    getdata();
                }else{
                    Toast.makeText(LoginActivity.this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),CadastroActivity.class));
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        Intent it = getIntent();
        if(it.hasExtra("logout")){
            //mGoogleApiClient.connect();
            //signOut();
        }

        esquecisenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recupera();
            }
        });

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
//            token = acct.getId();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {
        if(b) {
            getdataGoogle();
        }
        else
            Toast.makeText(LoginActivity.this, "Erro ao logar. Tente novamente", Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
         mGoogleApiClient.connect();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e("logout","logout");
                    }
                });
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
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

        CallListener<UserRequest> callListener = new CallListener<UserRequest>(this, "Autenticando no sistema", onDialogButtonClick) {
            @Override
            public void onResponse(UserRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    if (request != null && request.getItens() != null && request.getItens().size() > 0) {
                        Usuario usuario = request.getItens().get(0);
                        if (checklogin.isChecked()) {
                           usuario.setLogado(true);
                        }
                        new LocalDbImplement<Usuario>(LoginActivity.this).save(usuario);
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        setpush();
                        finishAffinity();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).login(callListener,senha.getText().toString(),email.getText().toString());
    }

    SharedPreferences sp;
    public void setpush(){
        Intent servicegcm = new Intent(getBaseContext(),RegistrationIntentService.class);
        startService(servicegcm);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        CallListener<BaseRequest> callListener = new CallListener<BaseRequest>(getBaseContext(), null, null) {
            @Override
            public void onResponse(BaseRequest request) {
                super.onResponse(request);
                Log.e("onResponse", request.getResult());
            }
        };
        new BaseDao(getBaseContext()).sendPush(callListener, sp.getString("deviceid", ""), sp.getString("token_push", ""), refreshedToken );
    }


    public void getdataGoogle(){

        OnDialogButtonClick onDialogButtonClick = new OnDialogButtonClick() {
            @Override
            public void onPositiveClick() {
                getdataGoogle();
            }

            @Override
            public void onNegativeClick() {

            }
        };

        CallListener<UserRequest> callListener = new CallListener<UserRequest>(this, "Autenticando no sistema", onDialogButtonClick) {
            @Override
            public void onResponse(UserRequest request) {
                super.onResponse(request);
                if (request.success()) {
                    if (request != null && request.getItens() != null && request.getItens().size() > 0) {
                        Usuario usuario = request.getItens().get(0);
                        usuario.setLogado(true);
                        new LocalDbImplement<Usuario>(LoginActivity.this).save(usuario);
                        setpush();
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, request.getException(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };


        new BaseDao(getBaseContext()).loginGoogle(callListener,acct.getId(),acct.getEmail(),acct.getDisplayName());
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }


    public void recupera() {


        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recupera);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView btnSalvar = (TextView) dialog.findViewById(R.id.btnSalvar);
        final EditText email = (EditText) dialog.findViewById(R.id.email);

        dialog.show();


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().length() > 0) {
                    Toast.makeText(LoginActivity.this, "Email de recuperação enviado!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // resetSenha(email.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Informe e-mail", Toast.LENGTH_SHORT).show();
                  //  dialog.dismiss();
                }
            }
        });

    }

}

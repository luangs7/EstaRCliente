package br.com.tads.estarcliente.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import br.com.tads.estarcliente.AtivarActivity;
import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.model.Usuario;


public class Fragment1 extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    public static final int  REQUEST_PERMISSIONS_CODE = 128;
    private GoogleApiClient mGoogleApiClient;
    Location lastLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Usuario user = new LocalDbImplement<Usuario>(getContext()).getDefault(Usuario.class);

        View view = inflater.inflate(R.layout.activity_fragment1, container, false);

        Button btnAtivar = (Button) view.findViewById(R.id.btnAtivar);
        TextView boasvindas = (TextView) view.findViewById(R.id.textViewNome);
         try {
             boasvindas.setText("Bem vindo, " + user.getNome());
         }catch (Exception e){
             boasvindas.setText("Bem vindo");
         }


        btnAtivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AtivarActivity.class));
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    private synchronized void callConnection(){
        Log.i("LOG", "LastLocationActivity.callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions( getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );

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
            mMap.setMyLocationEnabled(true);
            //mMap.addMarker(new MarkerOptions().position(location).title("Essa é a posição do carro"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,19));

//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//
//
//                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", lastLocation.getLatitude(), lastLocation.getLongitude(), "Localização atual", Double.parseDouble(street.getLatitude()), Double.parseDouble(street.getLongitude()), "Seu destino");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                    startActivity(intent);
//
//                }
//            });

        }else{
            Toast.makeText(getContext(), "Sua localização não foi encontrada. Verifique se seu celular está funcionando corretamente.", Toast.LENGTH_SHORT).show();
            getActivity().finishAffinity();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        callConnection();
    }
}

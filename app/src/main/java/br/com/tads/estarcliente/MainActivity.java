package br.com.tads.estarcliente;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import br.com.tads.estarcliente.adapter.PagerAdapter;
import br.com.tads.estarcliente.alarm.MyServiceTempo;
import br.com.tads.estarcliente.dao.local.LocalDbImplement;
import br.com.tads.estarcliente.model.Usuario;


public class MainActivity extends AppCompatActivity {

        GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.cancelAll();
        stopService(new Intent(getBaseContext(), MyServiceTempo.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EstaR");

        setSupportActionBar(toolbar);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Principal"));
        tabLayout.addTab(tabLayout.newTab().setText("Créditos"));
        tabLayout.addTab(tabLayout.newTab().setText("Veículos"));
        tabLayout.addTab(tabLayout.newTab().setText("Meu histórico"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            new LocalDbImplement<Usuario>(MainActivity.this).clearObject(Usuario.class);
            Intent it = new Intent(getBaseContext(),LoginActivity.class);
            it.putExtra("logout","true");
            startActivity(it);
            return true;
//        }

//            return super.onOptionsItemSelected(item);
        }

        return false;
    }


}
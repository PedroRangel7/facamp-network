package com.pief.facampnetwork;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static int idSessao;
    private static int tipoSessao;
    public static int getIDSessao(){
        return idSessao;
    }
    public static int getTipoSessao(){return tipoSessao;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_market, R.id.navigation_rides, R.id.navigation_classes, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        idSessao = getIntent().getIntExtra("ID_SESSAO", idSessao);
        tipoSessao = getIntent().getIntExtra("TIPO_SESSAO", tipoSessao);
        Log.i("Sessao", "ID: " + String.valueOf(idSessao));
        Log.i("Sessao", "Tipo: " + String.valueOf(tipoSessao));
    }
}
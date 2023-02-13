package com.example.myapplication;

import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int[][] best_score_all = {{0,0,0,0}, {0,0,0,0},{0,0,0,0},{0,0,0,0}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navView.setOnNavigationItemSelectedListener(this::onNavDestinationSelected);

        best_score_all =(int[][]) getIntent().getSerializableExtra("best_score_all");

        Bundle bundle = new Bundle();
        bundle.putSerializable("best_score_all", best_score_all);
        navController.navigate(R.id.navigation_home, bundle);
    }

    private boolean onNavDestinationSelected(MenuItem item) {
        int id = item.getItemId();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        if (id == R.id.navigation_home) {

            navController.navigate(id);
            return true;
        }

        if (id == R.id.navigation_dashboard) {
            navController.navigate(id);
            return true;
        }

        if (id == R.id.navigation_notifications) {
            navController.navigate(id);
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item, navController);
    }








}


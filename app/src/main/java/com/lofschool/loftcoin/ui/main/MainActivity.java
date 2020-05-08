package com.lofschool.loftcoin.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lofschool.loftcoin.BaseComponent;
import com.lofschool.loftcoin.LoftApp;
import com.lofschool.loftcoin.R;
import com.lofschool.loftcoin.databinding.ActivityMainBinding;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private MainComponent component;

    @Inject FragmentFactory fragmentFactory;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final BaseComponent baseComponent = ((LoftApp) newBase.getApplicationContext()).getComponent();
        component = DaggerMainComponent.builder().baseComponent(baseComponent).build();
        component.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().setFragmentFactory(fragmentFactory);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);
        setContentView(binding.getRoot());
        final NavController navController = Navigation.findNavController(this, R.id.main_host);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        NavigationUI.setupWithNavController(binding.toolbar, navController, new AppBarConfiguration
            .Builder(binding.bottomNav.getMenu())
            .build());
    }

}
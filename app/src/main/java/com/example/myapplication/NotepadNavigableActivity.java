package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public abstract class NotepadNavigableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notepad");

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected abstract void initLayout();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_online) {
            Intent intent = new Intent(NotepadNavigableActivity.this, OnlineActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_offline) {
            Intent intent = new Intent(NotepadNavigableActivity.this, OfflineActivity.class);
            startActivity(intent);
        }  else if (item.getItemId() == R.id.nav_login) {
            Intent intent = new Intent(NotepadNavigableActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return true;
    }
}

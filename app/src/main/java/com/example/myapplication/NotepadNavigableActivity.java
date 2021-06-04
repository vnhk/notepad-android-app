package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.data.LoginRepository;
import com.example.myapplication.reponotes.NoteSynchronizer;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public abstract class NotepadNavigableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean noSynchronization;
    public static boolean loggedUser;
    public MenuItem offlineMenuItem;
    public MenuItem onlineMenuItem;
    public MenuItem synchronizeMenuItem;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private String offlineMenuItemText;
    private LoginRepository instance;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        synchronizationStatusChange();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        synchronizationStatusChange();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        super.onPanelClosed(featureId, menu);
        synchronizationStatusChange();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        synchronizationStatusChange();
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronizationStatusChange();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        synchronizationStatusChange();
    }

    public void synchronizationStatusChange() {
        if (offlineMenuItem == null) {
            return;
        }

        setLoginRepository();

        if (loggedUser) {
            onlineMenuItem.setEnabled(true);
            synchronizeMenuItem.setEnabled(true);
        } else {
            onlineMenuItem.setEnabled(false);
            synchronizeMenuItem.setEnabled(false);
            return;
        }

        if (noSynchronization) {
            offlineMenuItem.setEnabled(true);
            offlineMenuItem.setTitle(offlineMenuItemText);
            synchronizeMenuItem.setEnabled(true);
        } else {
            offlineMenuItem.setEnabled(false);
            offlineMenuItem.setTitle(offlineMenuItemText + " (Synchronization)");
            synchronizeMenuItem.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLoginRepository();

        initLayout();

        noSynchronization = true;
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        offlineMenuItem = navigationView.getMenu().getItem(0);
        onlineMenuItem = navigationView.getMenu().getItem(1);
        synchronizeMenuItem = navigationView.getMenu().getItem(3);
        offlineMenuItemText = offlineMenuItem.getTitle().toString();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notepad");

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                synchronizationStatusChange();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                synchronizationStatusChange();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                synchronizationStatusChange();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                synchronizationStatusChange();
            }
        });
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setLoginRepository() {
        try {
            instance = LoginRepository.getInstance(null);
            loggedUser = instance.getUser() != null;
        } catch (Exception e) {
            loggedUser = false;
        }
    }

    protected abstract void initLayout();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_online) {
            Intent intent = new Intent(NotepadNavigableActivity.this, OnlineActivity.class);
            startActivity(intent);
        } else if (noSynchronization && item.getItemId() == R.id.nav_offline) {
            Intent intent = new Intent(NotepadNavigableActivity.this, OfflineActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_login) {
            Intent intent = new Intent(NotepadNavigableActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_synchronize) {
            String token = instance.getUser().getToken();
            NoteSynchronizer.synchronizeNotes(token, getApplicationContext());
        }

        synchronizationStatusChange();

        return true;
    }
}

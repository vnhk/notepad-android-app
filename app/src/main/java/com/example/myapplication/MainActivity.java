package com.example.myapplication;

import android.os.Bundle;

public class MainActivity extends NotepadNavigableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main_layout);
    }
}
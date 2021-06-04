package com.example.myapplication;


import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.reponotes.NoteSynchronizer;

import java.util.List;

public class OfflineActivity extends NotepadNavigableActivity {
    private ExampleAdapter adapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillExampleList();
        setUpRecyclerView();

        SearchView searchView = findViewById(R.id.searchView);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private List<Note> getNotes() {
        return NoteSynchronizer.getNotes(getApplicationContext());
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_offline);
    }

    private void fillExampleList() {
        noteList = getNotes();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(noteList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public View getView() {
        return findViewById(R.id.nav_layout);
    }
}
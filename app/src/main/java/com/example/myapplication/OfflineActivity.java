package com.example.myapplication;


import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OfflineActivity extends NotepadNavigableActivity {
    private ExampleAdapter adapter;
    private List<Note> exampleList;

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
        exampleList = new ArrayList<>();
        Note e = new Note(R.drawable.icon_document, "Two", "Eleven");
        e.setContent("");
        e.setSecondary("");
        exampleList.add(e);
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        exampleList.add(new Note(R.drawable.icon_document, "Two", "Eleven"));
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        exampleList.add(new Note(R.drawable.icon_document, "Two", "Eleven"));
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        exampleList.add(new Note(R.drawable.icon_document, "Two", "Eleven"));
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        exampleList.add(new Note(R.drawable.icon_document, "Two", "Eleven"));
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        exampleList.add(new Note(R.drawable.icon_document, "Two", "Eleven"));
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        exampleList.add(new Note(R.drawable.icon_document, "Two", "Eleven"));
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        exampleList.add(new Note(R.drawable.icon_document, "Two", "Eleven"));
        exampleList.add(new Note(R.drawable.icon_calendar_note, "Three", "Twelve"));
        exampleList.add(new Note(R.drawable.icon_quick_note, "Four", "Thirteen"));
        return exampleList;
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_offline);
    }

    private void fillExampleList() {
        getNotes();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public View getView() {
        return findViewById(R.id.nav_layout);
    }
}
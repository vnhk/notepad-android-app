package com.example.myapplication;


import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.reponotes.NoteSynchronizer;

import java.util.List;

public class OfflineActivity extends NotepadNavigableActivity {
    private NoteFilterAdapter adapter;
    private List<Note> noteList;
    private CheckBox documentCheckbox;
    private CheckBox quickNotesCheckbox;
    private CheckBox calendarNoteCheckbox;
    private SearchView searchView;

    public void setCheckboxListeners() {
        documentCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NoteFilterAdapter.checkedDocument = isChecked;
            adapter.getFilter().filter(searchView.getQuery());
        });

        quickNotesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NoteFilterAdapter.checkedQuickNote = isChecked;
            adapter.getFilter().filter(searchView.getQuery());
        });

        calendarNoteCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NoteFilterAdapter.checkedCalendarNote = isChecked;
            adapter.getFilter().filter(searchView.getQuery());
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillExampleList();
        setUpRecyclerView();

        searchView = findViewById(R.id.searchView);

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

        documentCheckbox = findViewById(R.id.documentCheckbox);
        quickNotesCheckbox = findViewById(R.id.quickNotesCheckbox);
        calendarNoteCheckbox = findViewById(R.id.calendarNoteCheckbox);

        setCheckboxListeners();

        documentCheckbox.setChecked(true);
        NoteFilterAdapter.checkedDocument = true;
        quickNotesCheckbox.setChecked(true);
        NoteFilterAdapter.checkedQuickNote = true;
        calendarNoteCheckbox.setChecked(true);
        NoteFilterAdapter.checkedCalendarNote = true;
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
        adapter = new NoteFilterAdapter(noteList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public View getView() {
        return findViewById(R.id.nav_layout);
    }
}
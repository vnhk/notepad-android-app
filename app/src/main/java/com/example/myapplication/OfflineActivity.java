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
        Note javaTutorial = new Note(R.drawable.icon_document, "Java 8", "");
        javaTutorial.setContent("<div style=\"text-align: justify;\"><font color=\"#000000\" face=\"Arial, Verdana, Tahoma\" size=\"5\"><b>Java 8:</b></font></div><div style=\"text-align: justify;\"><h2 id=\"4fb8\" class=\"ib ic dj bm id ie if ig ih ii ij ik il im in io ip iq ir is it iu iv iw ix iy eg\" data-selectable-paragraph=\"\" style=\"box-sizing: inherit; margin: 1.72em 0px -0.31em; font-family: sohne, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif; color: rgb(41, 41, 41); line-height: 28px; text-align: start;\"><ul style=\"font-size: 22px;\"><li><i>Lambda Expressions</i></li><li><i>Null Reference Template<br></i></li><li><i>Streams API</i></li></ul><div style=\"\"><pre style=\"color: rgb(169, 183, 198); font-family: &quot;JetBrains Mono&quot;, monospace; background-color: rgb(43, 43, 43);\"><font size=\"2\"><span style=\"color: rgb(204, 120, 50);\">public void </span><span style=\"color: rgb(255, 198, 109);\">streamExample</span>() {<br>    Stream.<span style=\"font-style: italic;\">of</span>(<span style=\"color: rgb(104, 151, 187);\">1</span><span style=\"color: rgb(204, 120, 50);\">, </span><span style=\"color: rgb(104, 151, 187);\">2</span><span style=\"color: rgb(204, 120, 50);\">, </span><span style=\"color: rgb(104, 151, 187);\">3</span><span style=\"color: rgb(204, 120, 50);\">, </span><span style=\"color: rgb(104, 151, 187);\">4</span><span style=\"color: rgb(204, 120, 50);\">, </span><span style=\"color: rgb(104, 151, 187);\">5</span><span style=\"color: rgb(204, 120, 50);\">, </span><span style=\"color: rgb(104, 151, 187);\">6</span><span style=\"color: rgb(204, 120, 50);\">, </span><span style=\"color: rgb(104, 151, 187);\">7</span>)<br>            .sorted()<br>            .collect(Collectors.<span style=\"font-style: italic;\">toList</span>())<br>            .forEach(System.<span style=\"color: rgb(152, 118, 170); font-style: italic;\">out</span>::println)<span style=\"color: rgb(204, 120, 50);\">;<br></span>}</font></pre></div></h2></div><div style=\"text-align: center;\"><br></div>");
        javaTutorial.setSecondary("Introduction to Java 8.");
        javaTutorial.setTags("#java, #tutorial");
        exampleList.add(javaTutorial);

        Note jogging = new Note(R.drawable.icon_calendar_note, "Jogging", "Daily Calendar: 28.05.2021");
        jogging.setTags("#dailyroutine");
        exampleList.add(jogging);

        Note quick_note = new Note(R.drawable.icon_quick_note, "Quick Note", "");
        quick_note.setTags("#shopping, #list");
        exampleList.add(quick_note);

        Note android_studio = new Note(R.drawable.icon_document, "Android Studio", "");
        android_studio.setTags("#android, #tools");
        exampleList.add(android_studio);
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
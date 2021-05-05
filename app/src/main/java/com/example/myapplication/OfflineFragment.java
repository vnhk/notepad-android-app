package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OfflineFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<String> notes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment2_layout, container, false);
//
//        recyclerView = view.findViewById(R.id.recycler_view);
//
//        getNotes();
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
//        adapter = new NoteAdapter(notes);
//        recyclerView.setAdapter(adapter);
//
//        return view;
       return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getNotes() {
        notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            notes.add("test" + i);
        }
    }
}

package com.example.myapplication;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private final List<Note> exampleList;
    private final List<Note> exampleListFull;
    private final OfflineActivity activity;
    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Note item : exampleListFull) {
                    if (item.getSecondary().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    ExampleAdapter(List<Note> exampleList, OfflineActivity activity) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,
                parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Note currentItem = exampleList.get(position);

        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.header.setText(currentItem.getHeader());
        holder.secondary.setText(currentItem.getSecondary());
        holder.tags.setText(holder.tags.getText() + " " + currentItem.getTags());
        holder.id = position;
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView header;
        TextView secondary;
        TextView tags;
        ImageView details;
        int id;

        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            details = itemView.findViewById(R.id.showNote);
            header = itemView.findViewById(R.id.headerNote);
            secondary = itemView.findViewById(R.id.secondaryNote);
            tags = itemView.findViewById(R.id.tags);


            details.setOnClickListener(v -> {
                Intent intent = new Intent(activity, NoteDetailsActivity.class);
                intent.putExtra("note", exampleList.get(id));
                activity.startActivity(intent);
            });
        }
    }

}
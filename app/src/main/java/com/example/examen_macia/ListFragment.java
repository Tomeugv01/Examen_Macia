package com.example.examen_macia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private ListView listView;
    private ActivityAdapter adapter;
    private List<Activitat> activitats;
    private OnActivitySelectedListener listener;

    public interface OnActivitySelectedListener {
        void onActivitySelected(Activitat activitat);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = view.findViewById(R.id.listview_activities);

        // If activities haven't been set, initialize with empty list
        if (activitats == null) {
            activitats = new ArrayList<>();
        }

        // Create and set the adapter
        adapter = new ActivityAdapter(getContext(), activitats);
        listView.setAdapter(adapter);

        // Set up click listener
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Activitat selectedActivity = activitats.get(position);
            if (listener != null) {
                listener.onActivitySelected(selectedActivity);
            }
        });

        return view;
    }

    public void setOnActivitySelectedListener(OnActivitySelectedListener listener) {
        this.listener = listener;
    }

    public void setActivities(List<Activitat> activities) {
        if (activities != null) {
            // Update the internal list reference
            this.activitats = activities;
            // If adapter already exists, update it with the new list
            if (adapter != null) {
                adapter.updateActivities(this.activitats);
            }
        }
    }
}

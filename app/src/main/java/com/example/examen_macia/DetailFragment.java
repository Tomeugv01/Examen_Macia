package com.example.examen_macia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    public static final String ARG_ACTIVITAT = "activitat";

    private ImageView iconImageView;
    private TextView nomTextView;
    private TextView descripcioTextView;
    private TextView duradaTextView;
    private TextView intensitatTextView;

    public static DetailFragment newInstance(Activitat activitat) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITAT, activitat);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        iconImageView = view.findViewById(R.id.detail_icon);
        nomTextView = view.findViewById(R.id.detail_nom);
        descripcioTextView = view.findViewById(R.id.detail_descripcio);
        duradaTextView = view.findViewById(R.id.detail_durada);
        intensitatTextView = view.findViewById(R.id.detail_intensitat);

        if (getArguments() != null) {
            Activitat activitat = (Activitat) getArguments().getSerializable(ARG_ACTIVITAT);
            if (activitat != null) {
                displayActivity(activitat);
            }
        }

        return view;
    }

    private void displayActivity(Activitat activitat) {
        if (iconImageView != null && nomTextView != null && descripcioTextView != null &&
            duradaTextView != null && intensitatTextView != null) {
            iconImageView.setImageResource(activitat.getIconResId());
            nomTextView.setText(activitat.getNom());
            descripcioTextView.setText(activitat.getDescripcio());
            duradaTextView.setText(activitat.getDurada());
            intensitatTextView.setText(activitat.getIntensitat());
        }
    }

    public void updateActivity(Activitat activitat) {
        if (activitat != null && isAdded() && getView() != null) {
            displayActivity(activitat);
        }
    }
}

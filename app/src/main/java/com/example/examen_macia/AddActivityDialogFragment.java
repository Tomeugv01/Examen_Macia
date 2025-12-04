package com.example.examen_macia;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddActivityDialogFragment extends DialogFragment {

    private EditText editNom;
    private EditText editDescripcio;
    private EditText editDurada;
    private EditText editIntensitat;

    private OnActivityAddedListener listener;

    public interface OnActivityAddedListener {
        void onActivityAdded(Activitat activitat);
    }

    public void setOnActivityAddedListener(OnActivityAddedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_activity, container, false);

        // Initialize views
        editNom = view.findViewById(R.id.edit_nom);
        editDescripcio = view.findViewById(R.id.edit_descripcio);
        editDurada = view.findViewById(R.id.edit_durada);
        editIntensitat = view.findViewById(R.id.edit_intensitat);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnAdd = view.findViewById(R.id.btn_add);

        // Set up cancel button
        btnCancel.setOnClickListener(v -> dismiss());

        // Set up add button
        btnAdd.setOnClickListener(v -> addActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Make dialog wider
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private void addActivity() {
        // Get input values
        String nom = editNom.getText().toString().trim();
        String descripcio = editDescripcio.getText().toString().trim();
        String durada = editDurada.getText().toString().trim();
        String intensitat = editIntensitat.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(nom)) {
            editNom.setError("El nom és obligatori");
            editNom.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(descripcio)) {
            editDescripcio.setError("La descripció és obligatòria");
            editDescripcio.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(durada)) {
            editDurada.setError("La durada és obligatòria");
            editDurada.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(intensitat)) {
            editIntensitat.setError("La intensitat és obligatòria");
            editIntensitat.requestFocus();
            return;
        }

        // Create new activity with default icon
        Activitat newActivity = new Activitat(
            nom,
            descripcio,
            android.R.drawable.ic_menu_info_details, // Default icon for new activities
            durada,
            intensitat
        );

        // Notify listener
        if (listener != null) {
            listener.onActivityAdded(newActivity);
        }

        // Show success message
        Toast.makeText(getContext(), "Activitat afegida correctament", Toast.LENGTH_SHORT).show();

        // Close dialog
        dismiss();
    }
}

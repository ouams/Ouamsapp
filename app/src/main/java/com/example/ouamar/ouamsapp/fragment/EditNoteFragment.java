package com.example.ouamar.ouamsapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ouamar.ouamsapp.R;
import com.example.ouamar.ouamsapp.activite.NoteActivity;
import com.example.ouamar.ouamsapp.entite.Note;
import com.example.ouamar.ouamsapp.helper.OuamsappDatabaseHelper;

import java.sql.SQLException;
import java.util.List;


public class EditNoteFragment extends Fragment {

    EditText title;
    EditText content;
    Button valid;
    Note note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vEdit = inflater.inflate(R.layout.fragment_edit_note, container, false);

        this.title = (EditText) vEdit.findViewById(R.id.titleEdit);
        this.content = (EditText) vEdit.findViewById(R.id.contentEdit);

        this.valid = (Button) vEdit.findViewById(R.id.valid);
        this.setOnValidListener();
        String idNote = (String) getArguments().get("note");
        if (!(idNote.equals("new"))) {
            this.loadContentNote(idNote);
        }
        return vEdit;
    }


    private void setOnValidListener() {

        this.valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OuamsappDatabaseHelper databaseHelper = OuamsappDatabaseHelper.getHelper(getActivity());

                if (note == null) {
                    note = new Note(((NoteActivity) getActivity()).getCurrentUser(), content.getText().toString(), title.getText().toString());
                } else {
                    note.setTitre(title.getText().toString());
                    note.setNotes(content.getText().toString());
                }

                try {
                    databaseHelper.getNoteDao().createOrUpdate(note);

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    //replace your current container being most of the time as FrameLayout
                    transaction.replace(R.id.notes, new ListNotesFragment(), "fragment");
                    transaction.commit();
                    getActivity().setTitle(getString(R.string.list));


                } catch (SQLException e) {
                    Log.e("EditNoteFragment", "fail to create or update note : " + e);
                }


            }
        });
    }

    private void loadContentNote(String idNote) {
        this.note = getNote(idNote);
        this.title.setText(note.getTitre());
        this.content.setText(note.getNotes());

    }

    private Note getNote(String idNote) {

        OuamsappDatabaseHelper databaseHelper = OuamsappDatabaseHelper.getHelper(getActivity());
        long id = Long.parseLong(idNote);

        List<Note> offres = null;
        try {
            offres = databaseHelper.getNoteDao().queryBuilder().where().eq("id", id).query();
        } catch (SQLException e) {
            Log.e("BookAnnounceFragment", "Fail to get note from database : " + e);
        }

        if (offres != null && offres.size() == 1) {
            return offres.get(0);

        } else {

            Log.d("BookAnnounceFragment", "Offre not found or Multiple propositions");
            return null;
        }
    }
}

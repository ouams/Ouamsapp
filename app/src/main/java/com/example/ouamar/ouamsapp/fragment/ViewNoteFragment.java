package com.example.ouamar.ouamsapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ouamar.ouamsapp.R;
import com.example.ouamar.ouamsapp.entite.Note;
import com.example.ouamar.ouamsapp.helper.OuamsappDatabaseHelper;

import java.sql.SQLException;
import java.util.List;


public class ViewNoteFragment extends Fragment {

    TextView title;
    TextView content;
    ImageButton edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View nView = inflater.inflate(R.layout.fragment_view_note, container, false);
        this.title = (TextView) nView.findViewById(R.id.titleView);
        this.content = (TextView) nView.findViewById(R.id.contentView);
        this.edit = (ImageButton) nView.findViewById(R.id.editButton);

        this.initComponant();

        return nView;
    }

    private void initComponant() {

        final Note note = this.getNote();

        this.title.setText(note.getTitre());
        this.content.setText(note.getNotes());

        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                //replace your current container being most of the time as FrameLayout
                Fragment fEditNote = new EditNoteFragment();

                Bundle bundle = new Bundle();
                bundle.putString("note", "" + note.getId());
                fEditNote.setArguments(bundle);

                transaction.replace(R.id.notes, fEditNote, "fragment");
                transaction.commit();
                getActivity().setTitle(getString(R.string.edit));

            }
        });


    }

    private Note getNote() {
        String idNote = (String) getArguments().get("note");
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



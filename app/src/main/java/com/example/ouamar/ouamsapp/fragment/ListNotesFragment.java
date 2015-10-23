package com.example.ouamar.ouamsapp.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ouamar.ouamsapp.R;
import com.example.ouamar.ouamsapp.activite.NoteActivity;
import com.example.ouamar.ouamsapp.entite.Note;
import com.example.ouamar.ouamsapp.helper.OuamsappDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListNotesFragment extends Fragment {

    ListView lNotes;
    List<Note> notes;
    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View vNotes = inflater.inflate(R.layout.fragment_list_notes, container, false);

        this.lNotes = (ListView) vNotes.findViewById(R.id.notesList);
        this.initNotes();

        return vNotes;
    }

    private void initNotes() {

        this.notes = null;

        final OuamsappDatabaseHelper ouamsappDatabaseHelper = OuamsappDatabaseHelper.getHelper(this.getActivity());

        try {
            this.notes = ouamsappDatabaseHelper.getNoteDao().queryBuilder().where().eq("IdUser_id",((NoteActivity)this.getActivity()).getCurrentUser()).query();
        } catch (SQLException e) {
            Log.e("ListNotesFragment", "Failed found notes by user: " +e);
        }
        List<String> nomNotes= new ArrayList<String>();
        for(Note n : this.notes){
            nomNotes.add(n.getTitre());
        }

        this.adapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,  android.R.id.text1,nomNotes);

        this.lNotes.setAdapter(this.adapter);

        this.lNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                //replace your current container being most of the time as FrameLayout
                Fragment fViewNote = new ViewNoteFragment();

                Bundle bundle = new Bundle();
                long idNote = notes.get(position).getId();
                bundle.putString("note", "" + idNote);
                fViewNote.setArguments(bundle);

                transaction.replace(R.id.notes, fViewNote, "fragment");
                transaction.commit();
                getActivity().setTitle(notes.get(position).getTitre());

            }
        });


        this.lNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Remove this note ?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                OuamsappDatabaseHelper databaseHelper = OuamsappDatabaseHelper.getHelper(getActivity());

                                try {
                                    databaseHelper.getNoteDao().delete(notes.get(pos));
                                    adapter.remove(notes.get(pos).getTitre());
                                    notes.remove(pos);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT);
                                } catch (SQLException e) {
                                    Log.e("ListNotesFragment", "fail to remove note : " + e);
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });
    }



}

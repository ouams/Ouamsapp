package com.example.ouamar.ouamsapp.activite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ouamar.ouamsapp.R;
import com.example.ouamar.ouamsapp.entite.User;
import com.example.ouamar.ouamsapp.fragment.EditNoteFragment;
import com.example.ouamar.ouamsapp.fragment.ListNotesFragment;
import com.example.ouamar.ouamsapp.helper.OuamsappDatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private User currentUser;
    FragmentManager manager;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note);

        this.setCurrentUser();
    }

    private void setCurrentUser() {
        if (currentUser == null) {
            OuamsappDatabaseHelper database = OuamsappDatabaseHelper.getHelper(this);

            //Getting Current User
            String pseudo = this.getIntent().getStringExtra("LOGIN");

            // Liste des utilisateurs en base
            List<User> users = null;
            try {
                users = database.getUsersDao().queryBuilder().where().eq("pseudo", pseudo).query();
            } catch (SQLException e) {
                Log.e("ListNotesFragment","faild getting current user : " +e);
            }

            // Si on trouve bien un seul utilisateur, alors on le retourne
            if (users.size() == 1) {
                this.currentUser = users.get(0);
            }else{
                currentUser = null;
                Log.e("ListNotesFragment","failed getting current user no user found or multiple posibility");
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        this.setFragment();
        return true;
    }

    private void setFragment() {
         manager = getSupportFragmentManager();
         transaction = manager.beginTransaction();
        //replace your current container being most of the time as FrameLayout

        transaction.replace(R.id.notes, new ListNotesFragment(), "fragment");
        transaction.commit();
        this.setTitle(getString(R.string.list));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            this.createNewNote();
        }

        return super.onOptionsItemSelected(item);
    }


    private void createNewNote() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fEdit =  new EditNoteFragment();

        Bundle bundle = new Bundle();
        bundle.putString("note", "" + "new");
        fEdit.setArguments(bundle);

        transaction.replace(R.id.notes,fEdit, "fragment");
        transaction.commit();
        this.setTitle(getString(R.string.newnote));


    }

    public User getCurrentUser(){
        return this.currentUser;
    }


    // 2.0 and above on button back pressed
    @Override
    public void onBackPressed() {

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment currentFragment = manager.findFragmentByTag("fragment");


        // Sinon si le fragment existe et qu'il n'est pas celui de l'accueil
       if (currentFragment != null && !currentFragment.getClass().equals(ListNotesFragment.class)) {

            // On remplace le fragment courant par le fragment List
           ListNotesFragment fList = new ListNotesFragment();
            transaction.replace(currentFragment.getId(), fList, "fragment");

            transaction.commit();
            setTitle(getString(R.string.list));
        } else {
            super.onBackPressed();
        }
    }

    // Before 2.0 on button back pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            Fragment current = manager.findFragmentByTag("fragment");

            // Si le fragment existe et qu'il n'est pas celui de l'accueil
            if (current != null && !current.getClass().equals(ListNotesFragment.class)) {

                // On remplace le fragment courant par le fragment List
                ListNotesFragment fList = new ListNotesFragment();
                transaction.replace(current.getId(), fList, "fragment");

                transaction.commit();
                setTitle(getString(R.string.list));
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

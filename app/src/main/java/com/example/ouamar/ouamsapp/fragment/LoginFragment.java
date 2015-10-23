package com.example.ouamar.ouamsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ouamar.ouamsapp.R;
import com.example.ouamar.ouamsapp.activite.NoteActivity;
import com.example.ouamar.ouamsapp.entite.User;
import com.example.ouamar.ouamsapp.helper.OuamsappDatabaseHelper;

import java.sql.SQLException;
import java.util.List;


public class LoginFragment extends Fragment {

    EditText name;
    EditText password;
    Button valid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View vLogin = inflater.inflate(R.layout.fragment_login, container, false);

        this.name = (EditText) vLogin.findViewById(R.id.usernameLogin);
        this.password = (EditText) vLogin.findViewById(R.id.passwordLogin);
        this.valid = (Button) vLogin.findViewById(R.id.validLogin);

        this.addOnValidListener();

        return vLogin;
    }

    private void addOnValidListener() {

        this.valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0  ) {
                    OuamsappDatabaseHelper databaseHelper = OuamsappDatabaseHelper.getHelper(getActivity());
                    String username = name.getText().toString();

                    List<User> users = null;

                    try {
                        users = databaseHelper.getUsersDao().queryBuilder().where().eq("pseudo", username).query();
                    } catch (SQLException e) {
                        Log.e("LoginFragment", "Fail to get user from db : " + e);
                    }

                    if (users.size() == 1) {
                        if(users.get(0).isPasswordEqual(password.getText().toString())){
                            Toast.makeText(getActivity(), "Login Correct !", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), NoteActivity.class);
                            String message = name.getText().toString();
                            intent.putExtra("LOGIN", message);
                            startActivity(intent);
                            getActivity().finish();
                            return;
                        }

                    }
                    Toast.makeText(getActivity(), "Login fail !", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), "Empty field !", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

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


public class SignUpFragment extends Fragment {

    EditText name;
    EditText password;
    EditText confim;
    EditText email;
    Button valid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View vSignUp = inflater.inflate(R.layout.fragment_sign_up, container, false);

        this.name = (EditText) vSignUp.findViewById(R.id.usernameSignUp);
        this.password = (EditText) vSignUp.findViewById(R.id.passwordSignUp);
        this.confim = (EditText) vSignUp.findViewById(R.id.confirmPasswordSignUp);
        this.email =(EditText) vSignUp.findViewById(R.id.emailSignUp);
        this.valid = (Button) vSignUp.findViewById(R.id.validSignUp);

        this.setOnValidListener();

        return vSignUp;
    }

    private void setOnValidListener() {

        this.valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validField()) {
                   if(addUserInDb()) {
                       goToNextActivity();
                   }
                }
            }
        });
    }

    private void goToNextActivity() {
        Toast.makeText(getActivity(), "Login Correct !", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), NoteActivity.class);
        String message = name.getText().toString().trim();
        intent.putExtra("LOGIN", message);
        startActivity(intent);
        getActivity().finish();
    }

    private boolean addUserInDb() {
    User user = new User(this.email.getText().toString().trim(),this.name.getText().toString().trim(),this.password.getText().toString().trim());

        OuamsappDatabaseHelper databaseHelper = OuamsappDatabaseHelper.getHelper(this.getActivity());

        try {
            databaseHelper.getUsersDao().create(user);
        } catch (SQLException e) {
            Log.e("SignUpFragment","Fail to create user : " +e);
            return false;
        }
        return true;
    }

    private boolean validField() {
       if(this.name.getText().toString().trim().length() >0 && this.email.getText().toString().trim().length() >0){
           if(this.password.getText().toString().trim().length() > 0 && this.password.getText().toString().trim().equals(this.confim.getText().toString().trim())){

                if(!(this.password.getText().toString().contains(" "))) {

                    if (this.fieldAreUnique()) {
                        return true;
                    }
                }
               Toast.makeText(getActivity(), "Password can't contains space !", Toast.LENGTH_SHORT).show();
           }
           Toast.makeText(getActivity(), "Password are not the same!", Toast.LENGTH_SHORT).show();

       }
        return false;
    }

    private boolean fieldAreUnique() {

        OuamsappDatabaseHelper databaseHelper = OuamsappDatabaseHelper.getHelper(this.getActivity());

        try {
            List<User> users = databaseHelper.getUsersDao().queryForEq("pseudo",this.name.getText().toString());
            if(users.size() >0){
                this.name.setText("");
                Toast.makeText(getActivity(), "Username already used !", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SQLException e) {
            Log.e("SignUpFragment","fail to testing unique pseudo");
            return false;
        }


        try {
            List<User> users = databaseHelper.getUsersDao().queryForEq("email",this.email.getText().toString());
            if(users.size() >0){
                this.email.setText("");
                Toast.makeText(getActivity(), "Email already used !", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SQLException e) {
            Log.e("SignUpFragment","fail to testing unique email");
            return false;
        }

        return  true;
    }


}

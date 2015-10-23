package com.example.ouamar.ouamsapp.activite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ouamar.ouamsapp.MapsActivity;
import com.example.ouamar.ouamsapp.R;
import com.example.ouamar.ouamsapp.fragment.LoginFragment;
import com.example.ouamar.ouamsapp.fragment.SignUpFragment;


public class MainActivity extends AppCompatActivity {


    private Button swap;
    private Button gMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gMap = (Button) findViewById(R.id.testGMap);
        this.swap = (Button) findViewById(R.id.title);
        this.swap.setText(R.string.login);
        changeFragment();

        this.swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment();
            }
        });

        this.gMap.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //replace your current container being most of the time as FrameLayout

        if(this.swap.getText().toString().equals(getString(R.string.signUp))){
            transaction.replace(R.id.home,new SignUpFragment(),"fragment");
            transaction.commit();
            this.swap.setText(R.string.login);
        }else{
            transaction.replace(R.id.home,new LoginFragment(),"fragment");
            transaction.commit();
            this.swap.setText(R.string.signUp);
        }
    }



}

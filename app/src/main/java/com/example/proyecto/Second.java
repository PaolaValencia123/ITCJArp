package com.example.proyecto;

import static com.example.proyecto.Helper.ErrorMessages.sendToLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class Second extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
    }

    public void mover2(){
        Intent intentAbrirPagina = new Intent(getApplicationContext(),MenuSlideActivity.class);
        startActivity(intentAbrirPagina);
    }

    public void logOut(View view) {
        mAuth.signOut();
        sendToLogin(this);
    }
}
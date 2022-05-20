package com.example.proyecto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference rootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void nuevaPagina(View view){
        Uri abrirPagWeb = Uri.parse("https://www.facebook.com/");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, abrirPagWeb);
        startActivity(webIntent);
    }

    public void enviarDatos(View view){
        EditText edtCorreoE = findViewById(R.id.txtCoreoElect);
        EditText edtContrasenia = findViewById(R.id.txtContrasenia);

        String user = edtCorreoE.getText().toString();
        String pass = edtContrasenia.getText().toString();

        if(user.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Algún campo está vacío", Toast.LENGTH_SHORT).show();
        }
        else{
            login();
        }
    }

    public void login(){
        EditText edtCorreoE = findViewById(R.id.txtCoreoElect);
        EditText edtContrasenia = findViewById(R.id.txtContrasenia);

        String user = edtCorreoE.getText().toString();
        String pass = edtContrasenia.getText().toString();

        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this, Second.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Comprueba tu información", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void crearUsuario(View view){
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
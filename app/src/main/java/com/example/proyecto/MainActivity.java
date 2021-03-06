package com.example.proyecto;

import static com.example.proyecto.Helper.ErrorMessages.getErrorMessage;
//import static com.example.proyecto.Helper.ErrorMessages.sendToLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto.Historial.HistorialActivity;
import com.example.proyecto.Map.MapActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.config.Configuration;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference rootReference;
    EditText edtCorreoE;
    EditText edtContrasenia;
    MaterialTextView txvRecuperarContrasenia;

    //Progress para mostrar que se esta realizando el inicio de sesión
    private ProgressDialog mDialogInicioSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        //Instanciar el ProgressDialog
        mDialogInicioSesion = new ProgressDialog(this);

        txvRecuperarContrasenia = findViewById(R.id.textViewSendToResetPassword);

        txvRecuperarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewNewPassword();
            }
        });
    }

    public void nuevaPagina(View view) {
        startActivity(new Intent(MainActivity.this, HistorialActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser userF = mAuth.getCurrentUser();
        if(userF != null)
            startActivity(new Intent(MainActivity.this, MenuSlideActivity.class));
    }

    public void enviarDatos(View view) {
        edtCorreoE = findViewById(R.id.txtCoreoElect);
        edtContrasenia = findViewById(R.id.txtContrasenia);

        String user = edtCorreoE.getText().toString();
        String pass = edtContrasenia.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Algún campo está vacío", Toast.LENGTH_SHORT).show();
        } else {
            login(user, pass);
        }
    }

    public void login(String user, String pass) {
        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Mensaje de espera al iniciar sesión
                        mDialogInicioSesion.setMessage("Iniciando Sesión...");
                        // Establecer en false, para que el usuario no pueda quitarlo
                        mDialogInicioSesion.setCanceledOnTouchOutside(false);
                        mDialogInicioSesion.show();
                        startActivity(new Intent(MainActivity.this, MenuSlideActivity.class));
                        //Cuando la tarea finalice, ocultar el mensaje de progreso
                        mDialogInicioSesion.dismiss();
                        finish();
                    } else {
                        getErrorMessage(Registro.getInstance(), MainActivity.this,2,((FirebaseAuthException) task.getException()).getErrorCode(), edtCorreoE, edtContrasenia);
                    }
                });
    }

    public void textViewNewPassword(){
        Intent intentResetPass = new Intent(this, ResetPasswordActivity.class);
        startActivity(intentResetPass);
    }

    public void crearUsuario(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);

    }

    public static MainActivity getInstance(){
        return new MainActivity();
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
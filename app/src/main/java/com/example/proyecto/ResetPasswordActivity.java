package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.Helper.ErrorMessages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private Button mButtonResetPassword;

    private String email = "";
    private FirebaseAuth mAuth;

    //Progress para mostrar que se esta realizando el envio del correo
    private ProgressDialog mDialog;

    //private ProgressDialog mDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //Para restablecer el password, objeto de tipo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //Instanciar el ProgressDialog
        mDialog = new ProgressDialog(this);
        //mDialog2 = new ProgressDialog(this);

        //Instanciar con el Id de cada uno
        mEditTextEmail = findViewById(R.id.editTextEmail);
        mButtonResetPassword = findViewById(R.id.btnResetPassword);

        //Evento para que el botón ejecute la acción
        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tomar el email del EditText que ingrese el usuario
                email = mEditTextEmail.getText().toString();

                // Validar si el email es diferente de vacío, ejecute el método
                if (!email.isEmpty()){
                    //Mensaje de espera
                    mDialog.setMessage("Espere un momento...");
                    //Metodo que se establece en false, para que el usuario no pueda quitarlo, mientras se está ejecutando
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                }
                else{
                    Toast.makeText(ResetPasswordActivity.this, "Ingrese el correo electrónico registrado", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Método para realizar el cambio de contraseña
    private void resetPassword(){
        //Lenguaje del correo
        mAuth.setLanguageCode("es");

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Snackbar mySnackbarResetPassword = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Se ha enviado un correo para restablecer tu contraseña. Revisa tu bandeja de entrada o Spam.", Snackbar.LENGTH_SHORT);
                    mySnackbarResetPassword.show();
                    mEditTextEmail.setText("");
                    ErrorMessages.sendToLogin(ResetPasswordActivity.this);
                }
                else{
                    Toast.makeText(ResetPasswordActivity.this, "Error al enviar correo para restablecer contraseña", Toast.LENGTH_SHORT).show();
                }
                //Cuando la tarea finalice, ocultar el mensaje
                mDialog.dismiss();
            }
        });
    }
}
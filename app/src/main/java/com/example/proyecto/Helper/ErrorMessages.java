package com.example.proyecto.Helper;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import com.example.proyecto.MainActivity;
import com.example.proyecto.Registro;


public class ErrorMessages {

    public static void sendToLogin(Activity from) {
        Intent loginIntent = new Intent(from, MainActivity.class);
        from.startActivity(loginIntent);
        from.finish();
    }

    public static void getErrorMessage(Registro reg, MainActivity main, Integer selection, String errorCode, EditText edtEmail, EditText edtPass) {
        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(selection == 1 ? reg : main, "El formato del token personalizado es incorrecto. Por favor, compruebe la documentación.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(selection == 1 ? reg : main, "El token personalizado corresponde a un público diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(selection == 1 ? reg : main, "La credencial de autentificación suministrada está malformada o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                edtEmail.setError("La dirección de correo electrónico no es válida.");
                edtEmail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                edtPass.setError("La contraseña es incorrecta.");
                edtPass.requestFocus();
                edtPass.selectAll();
                edtPass.requestFocus();
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(selection == 1 ? reg : main, "Las credenciales suministradas no se corresponden con el usuario previamente registrado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(selection == 1 ? reg : main, "Esta operación es sensible y requiere una autentificación reciente. Inicie sesión de nuevo antes de reintentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(selection == 1 ? reg : main, "Ya existe una cuenta con la misma dirección de correo electrónico pero con credenciales de acceso diferentes. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(selection == 1 ? reg : main, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta.", Toast.LENGTH_LONG).show();
                edtEmail.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                edtEmail.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(selection == 1 ? reg : main, "Esta credencial ya está asociada a otra cuenta de usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(selection == 1 ? reg : main, "La cuenta de usuario ha sido desactivada por un administrador.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(selection == 1 ? reg : main, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión de nuevo.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(selection == 1 ? reg : main, "No hay ningún registro de usuario correspondiente a este identificador. El usuario puede haber sido eliminado.", Toast.LENGTH_LONG).show();
                break;


            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(selection == 1 ? reg : main, "Esta operación no está permitida. Debe habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                edtEmail.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                edtEmail.requestFocus();
                break;
            default:
                break;
        }
    }
}

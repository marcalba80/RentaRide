package com.example.rentaride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;


public class Login extends AppCompatActivity {
    EditText textoemail, textocontraseña;
    CircularProgressButton botonlogin;
    String email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        botonlogin = findViewById(R.id.login);
        textocontraseña = findViewById(R.id.input_password);
        textoemail = findViewById(R.id.input_email);
        botonlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        if(validate()){
            botonlogin.showProgress();
            botonlogin.setIndeterminateProgressMode(true);
            botonlogin.showComplete();
            Toast.makeText(getApplicationContext(), R.string.correctologin, Toast.LENGTH_LONG).show();
            startActivity(new Intent(Login.this, Calendar.class));
            finish();
        }else
            Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();

        startActivity(new Intent(Login.this, Calendar.class));
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        email = textoemail.getText().toString();
        password = textocontraseña.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textoemail.setError(getString(R.string.email));
            valid = false;
        }

        if (password.isEmpty() || password.length() < 6 ) {
            textocontraseña.setError(getString(R.string.errorcontraseña));
            valid = false;
        }
        return valid;
    }

    public void olvidado(View view) {
        startActivity(new Intent(Login.this, RecuperarContrasena.class));
    }

    public void registro(View view) {
        startActivity(new Intent(Login.this, RegistrarUsuario.class));
    }
}

package co.xtvt.xtvt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText editTextEmailLogin;
    EditText editTextPasswordLogin;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    Button buttonLogin;
    String email, password;
    Boolean userEmailValid = false;
    Boolean userPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        editTextEmailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        if (mAuth != null){
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        buttonLogin.setEnabled(false);
        textInputLayoutEmail.setErrorEnabled(true);
        textInputLayoutPassword.setErrorEnabled(true);

        findViewById(R.id.textViewRegister).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewForgotPassword).setOnClickListener(this);

        editTextEmailLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPasswordLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validateEmail(){
        textInputLayoutEmail.setError(null);
        email = editTextEmailLogin.getText().toString().trim();
        password = editTextPasswordLogin.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmailValid = false;
            textInputLayoutEmail.setError("Email is invalid");
            enableButton();
        }
        else {
            userEmailValid = true;
            enableButton();
        }
    }

    private void validatePassword(){
        email = editTextEmailLogin.getText().toString().trim();
        password = editTextPasswordLogin.getText().toString().trim();

        if (password.isEmpty()){
            userPasswordValid = false;
            enableButton();
        }
        else {
            userPasswordValid = true;
            enableButton();
        }
    }

    public void enableButton(){
        if (userEmailValid && userPasswordValid){
            buttonLogin.setEnabled(true);
        }
        else {
            buttonLogin.setEnabled(false);
        }
    }

    private void userLogin(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Toast.makeText(Login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewRegister:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.buttonLogin:
                userLogin();
                break;
            case R.id.textViewForgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;

        }
    }
}

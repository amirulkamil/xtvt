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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityRegister extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextUserNameRegister;
    private EditText editTextEmailRegister;
    private EditText editTextPasswordRegister;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReferenceUser;
    String userName, userEmail, userPassword;
    Boolean userNameValid = false;
    Boolean userEmailValid = false;
    Boolean userPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmailRegister = findViewById(R.id.editTextEmailRegister);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        editTextUserNameRegister = findViewById(R.id.editTextUserNameRegister);
        textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(ActivityRegister.this, ActivityDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        buttonRegister.setEnabled(false);
        textInputLayoutUsername.setErrorEnabled(true);
        textInputLayoutEmail.setErrorEnabled(true);
        textInputLayoutPassword.setErrorEnabled(true);

        findViewById(R.id.textViewLogin).setOnClickListener(this);
        findViewById(R.id.buttonRegister).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");

        editTextUserNameRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfUsernameExists();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextEmailRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfEmailExists();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPasswordRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfPasswordValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void checkIfUsernameExists(){
        textInputLayoutUsername.setError(null);
        userName = editTextUserNameRegister.getText().toString().trim();
        userEmail = editTextEmailRegister.getText().toString().trim();
        userPassword = editTextPasswordRegister.getText().toString().trim();

        if (userName.matches("[a-zA-Z0-9]*")) {
            if (userName.length() < 3) {
                userNameValid = false;
                textInputLayoutUsername.setError(getString(R.string.message_minimum_length_is_3));
                enableButton();
            } else {
                databaseReferenceUser.orderByChild("userName").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            userNameValid = false;
                            textInputLayoutUsername.setError(getString(R.string.message_username_exists));
                            enableButton();
                        } else {
                            userNameValid = true;
                            enableButton();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }else {
            userNameValid = false;
            textInputLayoutUsername.setError(getString(R.string.message_alphabetical_numerical));
            enableButton();
        }
    }

    public void checkIfEmailExists(){
        textInputLayoutEmail.setError(null);
        userName = editTextUserNameRegister.getText().toString().trim();
        userEmail = editTextEmailRegister.getText().toString().trim();
        userPassword = editTextPasswordRegister.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            userEmailValid = false;
            textInputLayoutEmail.setError(getString(R.string.message_email_is_invalid));
            enableButton();
        }
        else {
            databaseReferenceUser.orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userEmailValid = false;
                        textInputLayoutEmail.setError(getString(R.string.message_email_exists));
                        enableButton();
                    }
                    else{
                        userEmailValid = true;
                        enableButton();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void checkIfPasswordValid(){
        textInputLayoutPassword.setError(null);
        userName = editTextUserNameRegister.getText().toString().trim();
        userEmail = editTextEmailRegister.getText().toString().trim();
        userPassword = editTextPasswordRegister.getText().toString().trim();

        if (userPassword.length() < 6){
            userPasswordValid = false;
            textInputLayoutPassword.setError(getString(R.string.message_minimum_length_is_6));
            enableButton();
        }
        else
        {
            userPasswordValid = true;
            enableButton();
        }
    }

    public void enableButton(){
        if (userNameValid && userEmailValid && userPasswordValid){
            buttonRegister.setEnabled(true);
        }
        else {
            buttonRegister.setEnabled(false);
        }
    }

    public void registerUser(){
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    createNewUser(task.getResult().getUser());
                    Intent intent = new Intent(ActivityRegister.this, ActivityDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void createNewUser(FirebaseUser newUser){
        String userName = editTextUserNameRegister.getText().toString().trim();
        String userEmail = newUser.getEmail();
        String userId = newUser.getUid();

        User user = new User(userId, userName, userEmail);

        databaseReferenceUser.child(userId).setValue(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewLogin:
                startActivity(new Intent(this, ActivityLogin.class));
                break;
            case R.id.buttonRegister:
                registerUser();
                break;
        }
    }
}

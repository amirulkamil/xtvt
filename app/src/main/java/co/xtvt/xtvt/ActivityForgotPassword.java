package co.xtvt.xtvt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityForgotPassword extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewLogin;
    private EditText editTextEmailForgotPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        editTextEmailForgotPassword = (EditText) findViewById(R.id.editTextEmailForgotPassword);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
        findViewById(R.id.buttonSubmitForgotPassword).setOnClickListener(this);
    }

    public void submitForgotPassword(){
        String email = editTextEmailForgotPassword.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();

        if (checkIfEmailExists(email)) {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ActivityForgotPassword.this, "Email is already sent", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(ActivityForgotPassword.this, "Email does not exists", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkIfEmailExists(String userEmail){
        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user");
        boolean isExist = false;
        databaseReferenceUser.orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    editTextEmailForgotPassword.setError("Email already exists");
                    editTextEmailForgotPassword.requestFocus();
                    final boolean isExist = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return isExist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewLogin:
                startActivity(new Intent(this, ActivityLogin.class));
                break;
            case R.id.buttonSubmitForgotPassword:
                submitForgotPassword();
                break;
        }
    }
}

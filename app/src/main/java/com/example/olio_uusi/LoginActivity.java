package com.example.olio_uusi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login, signup;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PwdHash pwdhash = new PwdHash();

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.signupPassword);

        /*authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class); // This method takes user to menu activity
                    startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(LoginActivity.this, "Wrong email or password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        };*/

        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String passwrd = password.getText().toString();
                if (mail.isEmpty()) {
                    email.setError("Insert e-mail!");
                    email.requestFocus();
                }
                else if (passwrd.isEmpty()) {
                    password.setError("Insert password!");
                    password.requestFocus();
                }
                else if (!passwrd.isEmpty() && !mail.isEmpty()) {

                    // we need to hash the password to match one in database
                    String salt = pwdhash.getSalt();
                    String securepasswrd = pwdhash.getSecuredPassword(passwrd, salt);


                    firebaseAuth.signInWithEmailAndPassword(mail, securepasswrd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Wrong password or e-mail!", Toast.LENGTH_LONG).show();
                                password.setError("Wrong password or e-mail!");
                                password.requestFocus();
                            }
                            else {
                                Intent goHome = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(goHome);
                            }
                        }
                    });
                }
            }
        });
        signup = findViewById(R.id.signupButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(LoginActivity.this, RegisteryActivity.class);
                startActivity(signUp);
            }
        });

    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
*/
}
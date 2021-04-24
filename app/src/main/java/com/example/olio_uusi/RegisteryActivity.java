package com.example.olio_uusi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisteryActivity extends AppCompatActivity {
    Button registery, goBack;
    EditText mail, password, password2;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("loginInfo");
        setContentView(R.layout.activity_registery);
        mail = findViewById(R.id.signUpMail);
        password = findViewById(R.id.signupPassword);
        password2 = findViewById(R.id.signupPassword2);
        PwdHash pwdhash = new PwdHash();

        registery = findViewById(R.id.registerButton);
        registery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String pwd = password.getText().toString();
                String pwd2 = password2.getText().toString();


                if(email.isEmpty()) {
                    mail.setError("Insert email first!");
                }
                else if (pwd.isEmpty()) {
                    password.setError("Insert password first!");
                }
                else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(RegisteryActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if (!pwd.equals(pwd2)) {
                    password.setError("Passwords do not match!");
                    password2.setError("Passwords do not match!");
                }
                else if (pwd.equals(pwd2)) {
                    boolean test;
                    test = checkPwd(pwd);
                    if (!test) {
                        password.setError("Password must be 12 char, and contain special, upper and lowercase");
                    }

                }
                ///////////////////////////////////////////////////////////////
                if (!(email.isEmpty() && pwd.isEmpty()) && pwd.equals(pwd2) && checkPwd(pwd)) {

                   // before we create the account we need to hash the password
                    String salt = pwdhash.getSalt();
                    String securepwd = pwdhash.getSecuredPassword(pwd, salt);

                    firebaseAuth.createUserWithEmailAndPassword(email, securepwd).addOnCompleteListener(RegisteryActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(RegisteryActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                startActivity(new Intent(RegisteryActivity.this, MainActivity.class));
                            }
                        }
                    });
                }
            }
        });

        goBack = findViewById(R.id.backButton);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisteryActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkPwd(String password) {
        boolean valid = false;
        boolean length = false;
        boolean number = false;
        boolean special = false;
        boolean upper = false;
        boolean lower = false;

        if (password.length() >= 12) {
            length = true;
        }
        if (password.contains(" ")) {
            valid = false;
        }
        char[] passwordArr = password.toCharArray();
        for (char c: passwordArr) {
            if (Character.isDigit(c)) {
                number = true;
            }

            if (Character.isLowerCase(c)) {
                lower = true;
            }
            if (Character.isUpperCase(c)) {
                upper = true;
            }
        }
        Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(password);
        if (match.find()) {
            special = true;
        }
        if (length && number && special && lower && upper) {
            valid = true;
        }
        return valid;
    }
}
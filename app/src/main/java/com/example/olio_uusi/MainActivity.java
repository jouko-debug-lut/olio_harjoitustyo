package com.example.olio_uusi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button logout, info, insertFoodConsumption, calculateBMI, calculator;
    TextView name;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator = findViewById(R.id.button);
        name = findViewById(R.id.textView);
        logout = findViewById(R.id.logOut);
        info = findViewById(R.id.userInfo);
        insertFoodConsumption = findViewById(R.id.foodConsumption);
        calculateBMI = findViewById(R.id.bmiButton);

        ///////
        // Now we are fetching the users name from the database to print in the home screen
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();
        String user_id = firebaseUser.getUid();
        reference = rootNode.getReference().child("Users").child(user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UserDataSet personalInfo = snapshot.getValue(UserDataSet.class);
                    name.setText("Welcome "+personalInfo.getUserName()+"!");
                }
                else{
                    // if user does not have any data in the database the greeting is hello user
                    name.setText("Welcome user!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //////

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logoff = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logoff);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setInfo = new Intent(MainActivity.this, UserInfo.class);
                startActivity(setInfo);
            }
        });

        insertFoodConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertFoodInfo = new Intent(MainActivity.this, userConsumption.class);
                startActivity(insertFoodInfo);
            }
        });

        calculateBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calcBMI = new Intent(MainActivity.this, CalculateBMI.class);
                startActivity(calcBMI);
            }
        });

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopSmoking = new Intent(MainActivity.this, cigaretteCalc.class);
                startActivity(stopSmoking);
            }
        });
    }
}
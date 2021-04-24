package com.example.olio_uusi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.Gravity;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserInfo extends AppCompatActivity {
    Spinner spinner;
    TextView name, height, weight, age;
    Button saveInfo, backToMenu;

    FirebaseDatabase rootNode;
    FirebaseUser firebaseUser;
    DatabaseReference reference, reference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        name = findViewById(R.id.userName);
        height = findViewById(R.id.userHeight);
        weight = findViewById(R.id.userWeight);
        age = findViewById(R.id.userAge);
        saveInfo = findViewById(R.id.saveInfo);
        backToMenu = findViewById(R.id.backToHome);

        // We made spinner and array for setting user gender.
        spinner = (Spinner) findViewById(R.id.spinner);
        String[] gender = new String[]{"Gender", "Woman", "Man", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, gender);
        spinner.setAdapter(adapter);

        // We are getting current user and user id from our firebase database using "Users" as a path.
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();
        String user_id = firebaseUser.getUid();
        reference = rootNode.getReference().child("Users").child(user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if user exists, we get users current data to text fields
                if (snapshot.exists()){
                    UserDataSet personalInfo = snapshot.getValue(UserDataSet.class);
                    name.setText(personalInfo.getUserName());
                    height.setText(personalInfo.getUserHeight());
                    weight.setText(personalInfo.getUserWeight());
                    age.setText(personalInfo.getUserAge());
                    String userSex = personalInfo.getUserGender();
                    if (userSex.equals("Woman")) {
                        spinner.setSelection(1);
                    }
                    if (userSex.equals("Man")) {
                        spinner.setSelection(2);
                    }
                    if (userSex.equals("Other")) {
                        spinner.setSelection(3);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // We save user info to firebase database when saveInfo button is clicked.
        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();

                // We save user data as a strings, so we have to convert text to string.
                String userName = name.getText().toString();
                String userHeight = height.getText().toString();
                String userWeight = weight.getText().toString();
                String userAge = age.getText().toString();
                String userGender = spinner.getSelectedItem().toString();

                // If user is not giving us all data, we set error and ask user to fill all information.
                if(userName.isEmpty()) {
                    name.setError("Enter your name!");
                }
                else if (userHeight.isEmpty()) {
                    height.setError("Enter your height!");
                }
                else if (userWeight.isEmpty()) {
                    weight.setError("Enter your height!");
                }
                else if (userAge.isEmpty()) {
                    age.setError("Enter your height!");
                }

                // We set users data, weight data to database with "Users" and "UsersWeightData" paths.
                String user_id = firebaseUser.getUid();
                reference = rootNode.getReference().child("Users").child(user_id);
                reference2 = rootNode.getReference().child("UserWeightData").child(user_id);

                UserDataSet dataSet = new UserDataSet(userName, userHeight, userWeight, userAge, userGender);
                reference.setValue(dataSet);

                // We push weight data to database, so we can use all values in our Graph View.
                int weight_value = Integer.parseInt(userWeight);
                UserWeightData weightData = new UserWeightData(weight_value);
                reference2.push().setValue(weightData);

                Toast toast = Toast.makeText(UserInfo.this, "Data saved!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }
}
package com.example.olio_uusi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class CalculateBMI extends AppCompatActivity {

    Button homeButton, graphButton;
    TextView bmiValue, bmiText;
    FirebaseDatabase rootNode;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmi);


        graphButton = (Button) findViewById(R.id.weightGraph);
        homeButton = (Button) findViewById(R.id.homeButton);
        bmiValue = (TextView) findViewById(R.id.bmiNumber);
        bmiText = (TextView) findViewById(R.id.bmiResult);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();
        String user_id = firebaseUser.getUid();
        reference = rootNode.getReference().child("Users").child(user_id);

        bmiText.setText("According to the health care standards you are: \n");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserDataSet personalInfo = snapshot.getValue(UserDataSet.class);
                    String height = personalInfo.getUserHeight();
                    double userHeight = Double.parseDouble(height);
                    String weight = personalInfo.getUserWeight();
                    double userWeight = Double.parseDouble(weight);
                    double bmi = calculateBmi(userHeight, userWeight);
                    String textToPrint = bmiPrint(bmi);
                    String result = Double.toString(bmi);
                    bmiValue.setText("Your BMI is: " + result);
                    bmiText.setText("According to the health care standards you are: \n\n" + textToPrint);




                } else {
                    bmiValue.setText("");
                    bmiText.setText("Insert personal data first!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(CalculateBMI.this, MainActivity.class);
                startActivity(goHome);
            }
        });

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showWeight = new Intent(CalculateBMI.this, WeightGraph.class);
                startActivity(showWeight);
            }
        });


    }
    // BMI:n laskukaava otettu https://www.terveyskirjasto.fi/dlk01001
    public double calculateBmi(double h, double w) {
        // we have to change height to meters
        h = h/100;
        double BMI = w/(h*h);
        // We use decimal format to show only 1 decimal number.
        BMI = Double.parseDouble(new DecimalFormat("##.#").format(BMI));
        System.out.println(BMI);
        return BMI;
    }
    // BMI:n ohjearvot otettu https://www.terveyskirjasto.fi/dlk01001
    public String bmiPrint(double bmi) {
        String print;
        if (bmi < 17) {
            print = "Severely underweight";
        } else if (bmi >= 17 && bmi < 18.5) {
            print = "Underweight";
        } else if (bmi >= 18.5 && bmi < 25) {
            print = "Healthy weight";
        } else if (bmi >= 25 && bmi < 30) {
            print = "Overweight";
        } else if (bmi >= 30 && bmi < 35) {
            print = "Moderately obese";
        } else if (bmi >= 35 && bmi < 40) {
            print = "Severely obese";
        } else if (bmi >= 40) {
            print = "Very severely obese";
        } else {
            print = "Error!!!!";
        }
        return print;
    }

}

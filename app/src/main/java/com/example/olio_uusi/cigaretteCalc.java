package com.example.olio_uusi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class cigaretteCalc extends AppCompatActivity {

    TextView cigaretteDay, textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cigarette_calc);

        int money, day;
        int cigarettePrice = 8;

        cigaretteDay = findViewById(R.id.cigaretteDay);
        textView = findViewById(R.id.textView3);
        button = findViewById(R.id.button2);

        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
        String startDateString = cigaretteDay.getText().toString();


        //SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        String currentDateString = myFormat.format(today);

        try {
            Date startDate = myFormat.parse(startDateString);
            Date currentDate = myFormat.parse(currentDateString);
            long difference = startDate.getTime() - currentDate.getTime();
            float daysBetween = (difference / (1000*60*60*24));
            System.out.println(daysBetween);
        } catch (Exception e) {
            e.printStackTrace();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
package com.example.olio_uusi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WeightGraph extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    LineGraphSeries series;
    Button backToMenu;
    FirebaseUser fbuser;
    GraphView graphView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_graph);

        backToMenu = findViewById(R.id.back);
        graphView = findViewById(R.id.idGraphView);

        // we need to initialize GridLabelRernderer so we can add padding to the graphview.
        GridLabelRenderer glr = graphView.getGridLabelRenderer();

        // We make sure all numbers in y-axis are shown with this padding.
        glr.setPadding(50);
        graphView.setTitle("Weight graph");

        // we run the get weight data program to get the correct values to the series.
        getWeightData();

        // we initialize the series to be inserted in the graph and then add it to the graphview.
        series = new LineGraphSeries();
        graphView.addSeries(series);

        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightGraph.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // We get users weight data from Firebase Database.
    private void getWeightData() {

        // we need to get the user data from firebase and for that we need to get the user_id from the database.
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();
        String user_id = fbuser.getUid();

        // We form the correct path to get the right data from the database.
        databaseReference = rootNode.getReference().child("UserWeightData").child(user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // We save all weight data to dataPoint array with for loop.
                DataPoint[] dataPoint = new DataPoint[(int) snapshot.getChildrenCount()];
                int i = 0;
                for(DataSnapshot dataSnap : snapshot.getChildren()) {
                    UserWeightData userWeight = dataSnap.getValue(UserWeightData.class);
                    dataPoint[i] = new DataPoint(i, userWeight.getUserWeight());
                    i++;
                }
                // We have to reset the series to show the data from datapoints formed from the database.
                series.resetData(dataPoint);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
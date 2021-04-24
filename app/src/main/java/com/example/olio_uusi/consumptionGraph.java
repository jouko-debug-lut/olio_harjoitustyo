package com.example.olio_uusi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.olio_uusi.ui.CarbonfpValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class consumptionGraph extends AppCompatActivity {

    TextView total;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    LineGraphSeries series;
    Button backToMenu;
    FirebaseUser fbuser;

    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_graph);

        total = findViewById(R.id.showTotal);
        backToMenu = findViewById(R.id.back);
        graphView = findViewById(R.id.idGraphView);
        GridLabelRenderer glr = graphView.getGridLabelRenderer();
        glr.setPadding(100);
        //
        graphView.setTitle("Total carbon footprint");
        series = new LineGraphSeries();

        graphView.addSeries(series);
        getConsumptionData();

        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(consumptionGraph.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getConsumptionData() {
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();
        String user_id = fbuser.getUid();
        databaseReference = rootNode.getReference().child("Consumption_DATA").child(user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataPoint[] dataPoint = new DataPoint[(int) snapshot.getChildrenCount()];
                int i = 0;
                for(DataSnapshot dataSnap : snapshot.getChildren()) {
                    CarbonfpValue totalfp = dataSnap.getValue(CarbonfpValue.class);
                    dataPoint[i] = new DataPoint(i, totalfp.getTotalfp());
                    i++;

                }
                series.resetData(dataPoint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
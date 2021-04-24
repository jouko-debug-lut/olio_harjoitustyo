package com.example.olio_uusi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olio_uusi.ui.CarbonfpValue;
import com.example.olio_uusi.ui.JsonReader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class userConsumption extends AppCompatActivity {
    Button submitBtn, backToHome;
    EditText meatConsumption, fishConsumption, porkConsumption, dairyConsumption, cheeseConsumption, riceConsumption, eggConsumption, wsaladConsumption, restaurantSpending;
    Spinner spinner;
    TextView infoText;

    FirebaseDatabase rootNode;
    FirebaseUser firebaseUser;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_consumption);

        submitBtn = findViewById(R.id.submitConsumption);
        backToHome = findViewById(R.id.backToMenu2);

        infoText = findViewById(R.id.Information);
        meatConsumption = findViewById(R.id.beefConsumption);
        fishConsumption = findViewById(R.id.fishConsumption);
        porkConsumption = findViewById(R.id.porkConsumption);
        dairyConsumption = findViewById(R.id.dairyConsumption);
        cheeseConsumption = findViewById(R.id.cheeseConsumption);
        riceConsumption = findViewById(R.id.riceConsumption);
        eggConsumption = findViewById(R.id.eggConsumption);
        wsaladConsumption = findViewById(R.id.winterSalConsumption);
        restaurantSpending = findViewById(R.id.restaurantConsumption);

        spinner = findViewById(R.id.dietSpinner);

        // form a list of diets to choose and add different diets to the list
        ArrayList<String> diets = new ArrayList<>();
        String diet1 = "omnivore";
        String diet2 = "vegetarian";
        String diet3 = "vegan";
        diets.add(diet1);
        diets.add(diet2);
        diets.add(diet3);
        // create an adapter to add the list of the diets to the spinner
        ArrayAdapter<String> dietAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, diets);
        spinner.setAdapter(dietAdapter);


        // We need to use StrictMode to catch accidental disk or network access on the application's main thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // initialize the json reader class
        JsonReader jsonread = new JsonReader();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the values from the text fields and convert them to string
                String meat = meatConsumption.getText().toString();
                String fish = fishConsumption.getText().toString();
                String pork = porkConsumption.getText().toString();
                String dairy = dairyConsumption.getText().toString();
                String cheese = cheeseConsumption.getText().toString();
                String rice = riceConsumption.getText().toString();
                String egg = eggConsumption.getText().toString();
                String wsalad = wsaladConsumption.getText().toString();
                String restaurant = restaurantSpending.getText().toString();

                // initialize the values 0 to make sure that the program does not fail.
                // The api does not understand url-link with no values in every field also the value must be integer.
                // Change the string to double and then calculate the percentage based on the values in ilmastodieetti.

                if (meat.isEmpty() == true) {
                    meat = "0";
                }
                Double meat_val = Double.parseDouble(meat);
                meat_val = (meat_val / 400) * 100; // 400 is the average consumption of finnish person in week.
                int meat_value = (int) Math.round(meat_val);


                if (fish.isEmpty() == true) {
                    fish = "0";
                }
                double fish_val = Double.parseDouble(fish);
                fish_val = (fish_val / 600) * 100; // 600 is the average consumption of finnish person in week.
                int fish_value = (int) Math.round(fish_val);


                if (pork.isEmpty() == true) {
                    pork = "0";
                }
                double pork_val = Double.parseDouble(pork);
                pork_val = (pork_val / 1000) * 100; // 1000 is the average consumption of finnish person in week.
                int pork_value = (int) Math.round(pork_val);


                if (dairy.isEmpty() == true) {
                    dairy = "0";
                }
                double dairy_val = Double.parseDouble(dairy);
                dairy_val = (dairy_val / 3800) * 100; // 3800 is the average consumption of finnish person in week.
                int dairy_value = (int) Math.round(dairy_val);


                if (cheese.isEmpty() == true) {
                    cheese = "0";
                }
                Double cheese_val = Double.parseDouble(cheese);
                cheese_val = (cheese_val / 300) * 100; // 300 is the average consumption of finnish person in week.
                int cheese_value = (int) Math.round(cheese_val);


                if (rice.isEmpty() == true) {
                    rice = "0";
                }
                Double rice_val = Double.parseDouble(rice);
                rice_val = (rice_val / 90) * 100; // 90 is the average consumption of finnish person in week.
                int rice_value = (int) Math.round(rice_val);


                if (egg.isEmpty() == true) {
                    egg = "0";
                }
                int egg_value = Integer.parseInt(egg); // egg consumption is entered in pc / week and calculated with that value in api.


                if (wsalad.isEmpty() == true) {
                    wsalad = "0";
                }
                double wsalad_val = Integer.parseInt(wsalad);
                wsalad_val = (wsalad_val / 1400) * 100; // 1400 is the average consumption of finnish person in week.
                int wsalad_value = (int) Math.round(wsalad_val);


                if (restaurant.isEmpty() == true) {
                    restaurant = "0"; // restaurant spending is entered in monthly spending in euros.
                }
                int restaurant_value = Integer.parseInt(restaurant);

                // we need to form the correct url address to api based on the users entries.
                String url = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/FoodCalculator" +
                        "?query.diet=" + spinner.getSelectedItem() +
                        "&query.beefLevel=" + meat_value +
                        "&query.fishLevel=" + fish_value +
                        "&query.porkPoultryLevel=" + pork_value +
                        "&query.dairyLevel=" + dairy_value +
                        "&query.cheeseLevel=" + cheese_value +
                        "&query.riceLevel=" + rice_value +
                        "&query.eggLevel=" + egg_value +
                        "&query.winterSaladLevel=" + wsalad_value +
                        "&query.restaurantSpending=" + restaurant_value + "&api_key=diary";

                // create a boolean variable that test the users entries and if they are in the accepted range.
                boolean valueTest = valueTester(meat_value, fish_value, pork_value, dairy_value, cheese_value, rice_value, egg_value, wsalad_value, restaurant_value);
                // get the result from the api if values are correct.
                if (valueTest) {

                    try {
                        //  get the JSONObject from the json reader class with the formed url address.
                        JSONObject jsonobject = jsonread.readJsonFromUrl(url);
                        // seperate the total carbon footprint value from the JSONObject to string.
                        String totalCFP = jsonobject.getString("Total");

                        // Change the total string to number format as double and then round it to int value.
                        Double totalCFPnbr1 = Double.parseDouble(totalCFP);
                        int totalCFPnbr = (int) Math.round(totalCFPnbr1);

                        //We calculate % of co2 compared to average
                        double userCon = calculateCarbonP(totalCFPnbr1);
                        String userConFinal = String.valueOf(userCon);
                        infoText.setText("Your total consumption "+totalCFPnbr+" is "+userConFinal+"% of average.");

                        // We initialize the Firebase database so we can save the users data based on the user_id.
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        rootNode = FirebaseDatabase.getInstance();
                        String user_id = firebaseUser.getUid();

                        // Form the correct path to save the data
                        reference = rootNode.getReference().child("Consumption_DATA").child(user_id);

                        // use the Carbonfpvalue to set and save the calculated total.
                        CarbonfpValue carbonfpValue= new CarbonfpValue(totalCFPnbr);
                        // We use the push function to keep the old values also saved.
                        reference.push().setValue(carbonfpValue);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Initialize a new intent to move user to show consumption graph and start the activity.
                    Intent consGraph = new Intent (userConsumption.this, consumptionGraph.class);
                    startActivity(consGraph);

            } else {
                    Toast.makeText(userConsumption.this, "Failed!!", Toast.LENGTH_LONG).show();
                }
            }


        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(userConsumption.this, MainActivity.class);
                startActivity(goHome);
            }
        });
    }
    // We need to test that all of the inserted values are between the range of ilmastodieetti
    public boolean valueTester(int beef, int fish, int pork, int dairy, int cheese, int rice, int egg, int wsalad, int rest){
        System.out.println("toimiii");
        boolean valid = false;
        // we need to test all of the values and even if one fails then the return is false
        boolean b = false;
        boolean f = false;
        boolean p = false;
        boolean d = false;
        boolean c = false;
        boolean r = false;
        boolean e = false;
        boolean ws = false;
        boolean res = false;

        // the values we are testing and submitting to the api are percentages and max input is 200 percent.
        // if the value is not under the max value then we set an error to corresponding field.

        // test meat
        if (beef <= 200) {
            b = true;
        } else {
            meatConsumption.setError("Value must be between 0 - 800!");
        }
        // test fish
        if (fish <= 200) {
            f = true;
        }else {
            fishConsumption.setError("Value must be between 0 - 800!");
        }
        // test pork
        if (pork <= 200) {
            p = true;
        } else {
            porkConsumption.setError("Must be between 0 - 4000");
        }
        // test dairy
        if (dairy <= 200){
            d = true;
        } else {
            dairyConsumption.setError("Must be between 0 - 7600");
        }
        // test cheese
        if (cheese <= 200){
            c = true;
        } else {
            cheeseConsumption.setError("Must be between 0 - 600");
        }
        // test rice
        if (rice <= 200) {
            r = true;
        } else {
            riceConsumption.setError("Must be between 0 - 180");
        }
        // test egg
        // egg is not submitted in percentages
        if (egg <= 30) {
            e = true;
        } else {
            eggConsumption.setError("Must be between 0 - 30");
        }
        // test winter salad
        if (wsalad <= 200){
            ws = true;
        } else {
            wsaladConsumption.setError("Must be between 0 - 2800");
        }
        // test restaurant spending
        // Not submitted in percentages.
        if (rest <= 800){
            res = true;
        } else {
            restaurantSpending.setError("Must be between 0 - 800");
        }

        // test if all ok
        if (b && f && p && d && c && r && e && ws && res) {
            valid = true;
            System.out.println("toimiii edelleen");
        }

        return valid;
    }
    // Function to calculate the percentage compared to finnish average
    public double calculateCarbonP(double total) {

        // Finnish average consumption is 1600 kg per year
        double average = (total/1600) * 100;
        System.out.println("TESTI" + average);
        return average;
    }

}
package com.firstapp.plantera;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String plantName = intent.getStringExtra(SearchFragment.EXTRA_PLANT_NAME);
        String imageUrl = intent.getStringExtra(SearchFragment.EXTRA_URL);

        ImageView imageView = findViewById(R.id.plant_image_details);
        TextView textView = findViewById(R.id.plant_common_name_details);
        Picasso.get().load(imageUrl).into(imageView);
        textView.setText(plantName);


        int plantId = intent.getIntExtra(SearchFragment.EXTRA_PLANT_ID, -1); // Get the plant ID
        if (plantId != -1) {
            new GetPlantDetailsTask().execute(plantId);
        }
    }

    private class GetPlantDetailsTask extends AsyncTask<Integer, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            int plantId = params[0];
            try {
                URL url = new URL("https://perenual.com/api/species/details/" + plantId + "?key=sk-wq5s65181f3ed666f2313");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    connection.disconnect();

                    return new JSONObject(response.toString());
                } else {
                    // Handle connection error or response code other than HTTP_OK
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                // Handle exceptions
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject plantDetails) {
            if (plantDetails != null) {
                try {
                    TextView typeTextView = findViewById(R.id.type);
                    TextView cycleTextView = findViewById(R.id.cycle);
                    TextView originTextView = findViewById(R.id.origin);
                    TextView propagationTextView = findViewById(R.id.propagation);
                    TextView sunlightTextView = findViewById(R.id.sunlight);
                    TextView pruningMonthTextView = findViewById(R.id.pruning_month);
                    TextView descriptionTextView = findViewById(R.id.description);

                    String commonName = plantDetails.getString("common_name");
                    JSONArray scientificNameArray = plantDetails.getJSONArray("scientific_name");
                    String scientificName = scientificNameArray.optString(0, "Unknown");
                    String type = plantDetails.optString("type", "Unknown");
                    String cycle = plantDetails.optString("cycle", "Unknown");
                    String watering = plantDetails.optString("watering", "Unknown");


                    // Origin
                    JSONArray originArray = plantDetails.optJSONArray("origin");
                    StringBuilder originText = new StringBuilder();
                    if (originArray != null) {
                        for (int i = 0; i < originArray.length(); i++) {
                            originText.append(originArray.optString(i));
                            if (i < originArray.length() - 1) {
                                originText.append(", ");
                            }
                        }
                    }

                    // Propagation
                    JSONArray propagationArray = plantDetails.optJSONArray("propagation");
                    StringBuilder propagationText = new StringBuilder();
                    if (propagationArray != null) {
                        for (int i = 0; i < propagationArray.length(); i++) {
                            propagationText.append(propagationArray.optString(i));
                            if (i < propagationArray.length() - 1) {
                                propagationText.append(", ");
                            }
                        }
                    }


                    // Sunlight
                    JSONArray sunlightArray = plantDetails.optJSONArray("sunlight");
                    StringBuilder sunlightText = new StringBuilder();
                    if (sunlightArray != null) {
                        for (int i = 0; i < sunlightArray.length(); i++) {
                            sunlightText.append(sunlightArray.optString(i));
                            if (i < sunlightArray.length() - 1) {
                                sunlightText.append(", ");
                            }
                        }
                    }

                    // Pruning Month
                    JSONArray pruningMonthArray = plantDetails.optJSONArray("pruning_month");
                    StringBuilder pruningMonthText = new StringBuilder();
                    if (pruningMonthArray != null) {
                        for (int i = 0; i < pruningMonthArray.length(); i++) {
                            pruningMonthText.append(pruningMonthArray.optString(i));
                            if (i < pruningMonthArray.length() - 1) {
                                pruningMonthText.append(", ");
                            }
                        }
                    }

                    // Description
                    String description = plantDetails.optString("description", "No description available");

                    // Set the text for each TextView
                    typeTextView.setText(type);
                    cycleTextView.setText(cycle);
                    originTextView.setText(originText.toString());
                    propagationTextView.setText(propagationText.toString());
                    sunlightTextView.setText(sunlightText.toString());
                    pruningMonthTextView.setText(pruningMonthText.toString());
                    descriptionTextView.setText(description);

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                }
            }
        }
    }
}

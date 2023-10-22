package com.firstapp.plantera;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements PlantAdapter.OnItemClickListener {
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_PLANT_NAME = "plantName";
    public static final String EXTRA_PLANT_ID = "scientificName";


    private EditText searchEditText;
    private Button searchButton;

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailsActivity.class);
        PlantItem clickedItem = mPlantList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_PLANT_NAME, clickedItem.getPlant());
        detailIntent.putExtra(EXTRA_PLANT_ID, clickedItem.getPlantId()); // Add the plant ID
        startActivity(detailIntent);
    }


    private RecyclerView mRecyclerView;
    private PlantAdapter mAdapter;
    private ArrayList<PlantItem> mPlantList;
    private RequestQueue mRequestQueue;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPlantList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(requireActivity());
        parseJSON();

        // Add these lines to handle search input
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
            }
        });

        return view;
    }

    private void performSearch(String query) {
        // Clear the existing plant list
        mPlantList.clear();

        // Construct the URL for searching
        String url = "https://perenual.com/api/species-list?key=sk-d7Ry6534cbcf6da2b2641&q=" + query;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.has("data")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject plant = jsonArray.getJSONObject(i);

                        // Check if "default_image" exists and is not null
                        if (plant.has("default_image") && !plant.isNull("default_image")) {
                            JSONObject defaultImage = plant.getJSONObject("default_image");

                            // Check if "original_url" exists and is not null
                            if (defaultImage.has("original_url") && !defaultImage.isNull("original_url")) {
                                String imageUrl = defaultImage.getString("original_url");
                                String plantName = plant.optString("common_name", ""); // Use optString to handle null values

                                mPlantList.add(new PlantItem(imageUrl, plantName, plant.getInt("id")));
                            }
                        }
                    }

                    // Notify the adapter of the data change
                    mAdapter.notifyDataSetChanged();
                } else {
                    // Handle cases where "data" field is missing in the JSON response
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle JSON parsing errors
            }
        }, Throwable::printStackTrace);

        mRequestQueue.add(request);
    }


    private void parseJSON() {
        String url = "https://perenual.com/api/species-list?key=sk-d7Ry6534cbcf6da2b2641&page=3";
        // Handle network request errors
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.has("data")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject plant = jsonArray.getJSONObject(i);

                        // Check if "default_image" exists and is not null
                        if (plant.has("default_image") && !plant.isNull("default_image")) {
                            JSONObject defaultImage = plant.getJSONObject("default_image");

                            // Check if "original_url" exists and is not null
                            if (defaultImage.has("original_url") && !defaultImage.isNull("original_url")) {
                                String imageUrl = defaultImage.getString("original_url");
                                String plantName = plant.optString("common_name", ""); // Use optString to handle null values

                                mPlantList.add(new PlantItem(imageUrl, plantName, plant.getInt("id")));
                            }
                        }
                    }

                    mAdapter = new PlantAdapter(requireActivity(), mPlantList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(SearchFragment.this);



//                    mAdapter.notifyDataSetChanged(); // Notify the adapter of the data change
                } else {
                    // Handle cases where "data" field is missing in the JSON response
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle JSON parsing errors
            }
        }, Throwable::printStackTrace);
        mRequestQueue.add(request);
    }
}
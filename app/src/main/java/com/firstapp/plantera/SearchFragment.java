package com.firstapp.plantera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SearchFragment extends Fragment {

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
        return view;
    }

    private void parseJSON() {
        String url = "https://perenual.com/api/species-list?key=sk-wq5s65181f3ed666f2313";
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

                                mPlantList.add(new PlantItem(imageUrl, plantName));
                            }
                        }
                    }

                    mAdapter = new PlantAdapter(requireActivity(), mPlantList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged(); // Notify the adapter of the data change
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

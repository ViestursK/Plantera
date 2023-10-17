package com.firstapp.plantera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private final Context mContext;
    private final ArrayList<PlantItem> mPlantList;
    private static OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }

    public PlantAdapter(Context context, ArrayList<PlantItem> plantList) {
        mContext = context;
        mPlantList = plantList;

    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.plant_item, parent, false);
        return new PlantViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantItem currentItem = mPlantList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String plant = currentItem.getPlant();

        holder.mPlantName.setText(plant);

        Picasso.get().load(imageUrl).into(holder.mImageView);
    }


    @Override
    public int getItemCount() {
        return mPlantList.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mPlantName;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mPlantName = itemView.findViewById(R.id.plant_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }

            });
        }
    }
}

package com.example.wellplants.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wellplants.R;
import com.example.wellplants.pojo.Plant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlantCardAdapter extends RecyclerView.Adapter<PlantCardAdapter.ViewHolder> {
    private List<Plant> feeds = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Plant feedInfo;
        private TextView title;
        private ImageView feedImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.plant_title);
            itemView.setOnClickListener(this);
        }

        public void bind(Plant feedInfo) {
            this.feedInfo = feedInfo;
            title.setText(feedInfo.getName());
//            feedImage.setImageBitmap(feedInfo.getContent());
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("petInfo", feedInfo);
            System.out.println("INFO" + feedInfo);
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_view_plant, bundle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(feeds.get(position));
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public void setItems(Collection<Plant> pets) {
        feeds.addAll(pets);
        notifyDataSetChanged();
    }

    public void clearItems() {
        feeds.clear();
        notifyDataSetChanged();
    }

    public List<Plant> getFeeds() {
        return feeds;
    }
}


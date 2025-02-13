package com.example.tasktwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private FirebaseFirestore db;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.recipeTitle.setText(recipe.getTitle());
        holder.recipeIngredients.setText(recipe.getIngredients());
        holder.recipeInstructions.setText(recipe.getInstructions());
        holder.recipeLikes.setText(String.valueOf(recipe.getLikes()));

        // Load Image using Glide
        Glide.with(context).load(recipe.getImageUrl()).into(holder.recipeImage);

        // Like Button Click
        holder.btnLike.setOnClickListener(view -> {
            int newLikes = recipe.getLikes() + 1;
            db.collection("recipes").document(recipe.getId()).update("likes", newLikes)
                    .addOnSuccessListener(unused -> {
                        recipe.setLikes(newLikes);
                        holder.recipeLikes.setText(String.valueOf(newLikes));
                        Toast.makeText(context, "Liked!", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle, recipeIngredients, recipeInstructions, recipeLikes;
        ImageView recipeImage;
        ImageButton btnLike;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeIngredients = itemView.findViewById(R.id.recipeIngredients);
            recipeInstructions = itemView.findViewById(R.id.recipeInstructions);
            recipeLikes = itemView.findViewById(R.id.recipeLikes);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            btnLike = itemView.findViewById(R.id.btnLike);
        }
    }
}
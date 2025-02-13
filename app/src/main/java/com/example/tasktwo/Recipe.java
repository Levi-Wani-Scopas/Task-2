package com.example.tasktwo;

public class Recipe {
    private String id;
    private String title;
    private String ingredients;
    private String instructions;
    private String imageUrl;
    private int likes;

    // Empty constructor for Firebase
    public Recipe() {}

    // Constructor without ID (Firebase will generate one)
    public Recipe(String title, String ingredients, String instructions, String imageUrl, int likes) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.likes = likes;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public String getImageUrl() { return imageUrl; }
    public int getLikes() { return likes; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setLikes(int likes) { this.likes = likes; }
}

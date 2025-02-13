package com.example.tasktwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText editTitle, editIngredients, editInstructions;
    private ImageView imageViewRecipe;
    private Button btnSelectImage, btnSaveRecipe , btnExit, btnRecipeList;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTitle = findViewById(R.id.editTitle);
        editIngredients = findViewById(R.id.editIngredients);
        editInstructions = findViewById(R.id.editInstructions);
        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);
        btnExit = findViewById(R.id.Exit);
        btnRecipeList = findViewById(R.id.RecipeList);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("recipe_images");
        progressDialog = new ProgressDialog(this);

        btnSelectImage.setOnClickListener(view -> selectImage());
        btnSaveRecipe.setOnClickListener(view -> saveRecipe());

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddRecipeActivity.this, MainActivity.class));
            }
        });

        btnRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddRecipeActivity.this, RecipeListActivity.class));
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewRecipe.setImageURI(imageUri);
        }
    }

    private void saveRecipe() {
        String title = editTitle.getText().toString().trim();
        String ingredients = editIngredients.getText().toString().trim();
        String instructions = editInstructions.getText().toString().trim();

        if (title.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || imageUri == null) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Saving Recipe to firebase");
        progressDialog.show();

        String imageId = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(imageId + ".jpg");
        imageRef.putFile(imageUri).continueWithTask(task -> imageRef.getDownloadUrl()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUrl = task.getResult().toString();
                Recipe recipe = new Recipe(title, ingredients, instructions, imageUrl, 0);

                db.collection("recipes").add(recipe).addOnCompleteListener(task1 -> {
                    progressDialog.dismiss();
                    if (task1.isSuccessful()) {
                        Toast.makeText(this, "Recipe Added!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error saving recipe!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
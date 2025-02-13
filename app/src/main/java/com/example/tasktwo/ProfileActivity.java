package com.example.tasktwo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView userEmail;
    private EditText editName;
    private ImageView profileImage;
    private Button btnSave, btnChangePic, btnBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Uri imageUri;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_pics");

        userEmail = findViewById(R.id.userEmail);
        editName = findViewById(R.id.editName);
        profileImage = findViewById(R.id.profileImage);
        btnSave = findViewById(R.id.btnSave);
        btnChangePic = findViewById(R.id.btnChangePic);
        btnBack = findViewById(R.id.Back);
        progressDialog = new ProgressDialog(this);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userEmail.setText(user.getEmail());
            loadUserData(user.getUid());
        }

        btnChangePic.setOnClickListener(view -> chooseImage());
        btnSave.setOnClickListener(view -> saveProfileData());
    }


    private void loadUserData(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        editName.setText(document.getString("name"));
                        String profileUrl = document.getString("profileImage");
                        if (profileUrl != null) {
                            Glide.with(this).load(profileUrl).into(profileImage);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }
    private void saveProfileData() {
        String name = editName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Saving...");
        progressDialog.show();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);

        if (imageUri != null) {
            StorageReference fileRef = storageRef.child(user.getUid() + ".jpg");
            fileRef.putFile(imageUri)
                    .continueWithTask(task -> fileRef.getDownloadUrl())
                    .addOnSuccessListener(uri -> {
                        Log.d("ProfileUpdate", "Image uploaded successfully!");
                        userData.put("profileImage", uri.toString());
                        saveToFirestore(user.getUid(), userData);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ProfileUpdate", "Image upload failed!", e);
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Image upload failed!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            saveToFirestore(user.getUid(), userData);
        }

    }

    private void saveToFirestore(String userId, Map<String, Object> userData) {
        db.collection("users").document(userId).set(userData)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Firestore update failed!", Toast.LENGTH_SHORT).show();
                });
    }
}
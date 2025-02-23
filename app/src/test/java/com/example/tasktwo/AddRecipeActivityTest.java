package com.example.tasktwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.mockito.Mockito.*;
import com.google.firebase.auth.FirebaseAuth;


public class AddRecipeActivityTest {

    @Mock
    private FirebaseFirestore mockDb;
    @Mock
    private StorageReference mockStorageRef;
    @Mock
    private FirebaseStorage mockFirebaseStorage;
    @Mock
    private ProgressDialog mockProgressDialog;
    @Mock
    private Toast mockToast;
    @Mock
    private EditText mockEditTitle, mockEditIngredients, mockEditInstructions;
    @Mock
    private ImageView mockImageViewRecipe;
    @Mock
    private Uri mockImageUri;
    @Mock
    private Intent mockIntent;

    private AddRecipeActivity addRecipeActivity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addRecipeActivity = mock(AddRecipeActivity.class);

        // Mock Firebase instances
        when(addRecipeActivity.getApplicationContext()).thenReturn(mock(AddRecipeActivity.class));

        // Use reflection to set private fields
        setPrivateField(addRecipeActivity, "mAuth", mock(FirebaseAuth.class));
        setPrivateField(addRecipeActivity, "db", mockDb);
        setPrivateField(addRecipeActivity, "storageRef", mockStorageRef);
        setPrivateField(addRecipeActivity, "progressDialog", mockProgressDialog);

        // Initialize views
        setPrivateField(addRecipeActivity, "editTitle", mockEditTitle);
        setPrivateField(addRecipeActivity, "editIngredients", mockEditIngredients);
        setPrivateField(addRecipeActivity, "editInstructions", mockEditInstructions);
        setPrivateField(addRecipeActivity, "imageViewRecipe", mockImageViewRecipe);
    }

    private void setPrivateField(Object targetObject, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // make private field accessible
        field.set(targetObject, value);
    }

    @Test
    public void testSaveRecipe_Success() {
        when(mockEditTitle.getText().toString()).thenReturn("Chocolate Cake");
        when(mockEditIngredients.getText().toString()).thenReturn("Flour, Sugar, Cocoa");
        when(mockEditInstructions.getText().toString()).thenReturn("Mix and bake at 180°C.");

        // Simulate user clicking the save button (assuming you have a button)
        addRecipeActivity.findViewById(R.id.btnSave).performClick();

        // Verify Firebase Firestore interaction
        verify(mockDb).collection("recipes");
        verify(mockProgressDialog).show(); // Your test logic here
    }

    // Other tests...
}

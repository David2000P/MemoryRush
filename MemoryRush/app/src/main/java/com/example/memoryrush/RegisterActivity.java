package com.example.memoryrush;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;
    private EditText editTextUsername;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextUsername = findViewById(R.id.editTextUsername);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();
            String username = editTextUsername.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty() && !username.isEmpty()) {
                if (password.equals(passwordConfirm)) {
                    registerUser(email, password, username);
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            updateUserProfile(user, username);

                            saveUserToDatabase(user.getUid(), username, email);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registrierung fehlgeschlagen: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToDatabase(String userId, String username, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        User newUser = new User(username, email);

        usersRef.child(userId).setValue(newUser);
    }

    private void updateUserProfile(FirebaseUser user, String username) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailVerification(user);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Fehler beim Aktualisieren des Profils", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Bestätigungs-E-Mail gesendet. Bitte überprüfen Sie Ihre E-Mails.", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Fehler beim Senden der Bestätigungs-E-Mail.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}

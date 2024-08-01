package com.shlokyadav.votingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

    EditText user_email,user_password;
    TextView login_btn,signup_btn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        dbHelper = new DatabaseHelper(this);

        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(LoginPage.this, RegistrationPage.class);
                    startActivity(i);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }

    private void loginUser() {
        String email = user_email.getText().toString().trim();
        String password = user_password.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.authenticateUser(email, password)) {
            boolean isAdmin = dbHelper.isAdmin(email);
            SharedPreferences prefs = getSharedPreferences("VotingAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userEmail", email);
            editor.putBoolean("isAdmin", isAdmin);
            editor.apply();

            Intent intent;
            if (isAdmin) {
                intent = new Intent(LoginPage.this, AdminPage.class);
            } else {
                intent = new Intent(LoginPage.this, VotingPage.class);
            }
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}

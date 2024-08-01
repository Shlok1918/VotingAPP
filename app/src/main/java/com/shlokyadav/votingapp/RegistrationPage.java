package com.shlokyadav.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationPage extends AppCompatActivity {

    private EditText user_email,user_password,user_name,user_phone;
    private TextView login_btn,signup_btn;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        dbHelper = new DatabaseHelper(this);

        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationPage.this, LoginPage.class);
                startActivity(i);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

        private void registerUser() {
            String email = user_email.getText().toString().trim();
            String password = user_password.getText().toString().trim();
            String username = user_name.getText().toString().trim();
            String userphone = user_phone.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()||username.isEmpty()|| userphone.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.addUser(email, password, username,userphone,false);
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            finish();

    }
}
package com.shlokyadav.votingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminPage extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText candidate_name;
    private TextView add_candidate,logout_btn;
    private RecyclerView candidate_rv;
    private CandidateAdapter candidateAdapter;
    private List<Candidate> candidateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        dbHelper = new DatabaseHelper(this);

        candidate_name = findViewById(R.id.candidate_name_input);
        add_candidate = findViewById(R.id.add_candidate_button);
        candidate_rv = findViewById(R.id.candidate_list);
        logout_btn = findViewById(R.id.logout_btn);

        candidateList = new ArrayList<>();
        candidateAdapter = new CandidateAdapter(this, candidateList);
        candidate_rv.setLayoutManager(new LinearLayoutManager(this));
        candidate_rv.setAdapter(candidateAdapter);


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


        add_candidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCandidate();
            }
        });

        loadCandidates();
    }



    private void addCandidate() {
        String candidateName = candidate_name.getText().toString().trim();
        if (!candidateName.isEmpty()) {
            dbHelper.addCandidate(candidateName);
            Toast.makeText(this, "Candidate added", Toast.LENGTH_SHORT).show();
            candidate_name.setText("");
            loadCandidates();
        } else {
            Toast.makeText(this, "Please enter a candidate name", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCandidates() {
        candidateList.clear();
        Cursor cursor = dbHelper.getCandidatesWithVoteCounts();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("candidate_name"));
                int voteCount = cursor.getInt(cursor.getColumnIndexOrThrow("vote_count"));
                candidateList.add(new Candidate(id, name, voteCount));
            } while (cursor.moveToNext());
        }

        cursor.close();
        candidateAdapter.notifyDataSetChanged();
    }

    private void logout() {
        Intent intent = new Intent(AdminPage.this, LoginPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

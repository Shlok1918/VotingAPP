package com.shlokyadav.votingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VotingPage extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView candidate_rv;
    private VotingCandidateAdapter candidateAdapter;
    private List<Candidate> candidateList;
    private Button voteButton;
    private TextView logout_btn, already_voted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_page);

        dbHelper = new DatabaseHelper(this);

        candidate_rv = findViewById(R.id.candidate_rv);
        voteButton = findViewById(R.id.vote_button);
        logout_btn = findViewById(R.id.logout_btn);
        already_voted = findViewById(R.id.already_voted);

        candidateList = new ArrayList<>();
        candidateAdapter = new VotingCandidateAdapter(this, candidateList);
        candidate_rv.setLayoutManager(new LinearLayoutManager(this));
        candidate_rv.setAdapter(candidateAdapter);

        loadCandidates();
        checkIfUserHasVoted();

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfUserHasVoted();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void loadCandidates() {
        candidateList.clear();
        Cursor cursor = dbHelper.getAllCandidates();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("candidate_name"));
                candidateList.add(new Candidate(id, name, 0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        candidateAdapter.notifyDataSetChanged();
    }

    private void checkIfUserHasVoted() {
        SharedPreferences sharedPreferences = getSharedPreferences("VotingAppPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", null);

        if (email != null) {
            int userId = dbHelper.getUserId(email);

            if (dbHelper.hasVoted(userId)) {
                String candidateName = dbHelper.getVotedCandidateName(userId);
                already_voted.setText("You have already voted for " + candidateName);
                showAlreadyVotedDialog();

                candidate_rv.setVisibility(View.GONE);
                already_voted.setVisibility(View.VISIBLE);
                voteButton.setVisibility(View.GONE);
            } else {
                castVote(userId);
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void castVote(int userId) {
        int selectedCandidateId = candidateAdapter.getSelectedCandidateId();
        if (selectedCandidateId != -1) {
            if (dbHelper.recordVote(userId, selectedCandidateId)) {
                Toast.makeText(this, "Vote cast successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "An error occurred while casting your vote", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please select a candidate to vote for", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlreadyVotedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Already Voted")
                .setMessage("You have already voted. Please log out.")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("VotingAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userEmail");
        editor.apply();

        Intent intent = new Intent(VotingPage.this, LoginPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

package com.shlokyadav.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VotingCandidateAdapter extends RecyclerView.Adapter<VotingCandidateAdapter.CandidateViewHolder> {

    private final List<Candidate> candidateList;
    private final Context context;
    private int selectedPosition = -1;

    public VotingCandidateAdapter(Context context, List<Candidate> candidateList) {
        this.context = context;
        this.candidateList = candidateList;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_vote_list, parent, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Candidate candidate = candidateList.get(position);
        holder.candidateName.setText(candidate.getName());
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public int getSelectedCandidateId() {
        if (selectedPosition != -1) {
            return candidateList.get(selectedPosition).getId();
        }
        return -1;
    }

    public static class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName;
        RadioButton radioButton;

        public CandidateViewHolder(View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.candidate_name);
            radioButton = itemView.findViewById(R.id.radio_button);
        }
    }
}

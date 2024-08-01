package com.shlokyadav.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder> {

    private final List<Candidate> candidateList;
    private final Context context;

    public CandidateAdapter(Context context, List<Candidate> candidateList) {
        this.context = context;
        this.candidateList = candidateList;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_list_item, parent, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Candidate candidate = candidateList.get(position);
        holder.candidateName.setText(candidate.getName());
        holder.candidateVotes.setText("Votes: " + candidate.getVoteCount());
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public static class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName;
        TextView candidateVotes;

        public CandidateViewHolder(View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.candidate_name);
            candidateVotes = itemView.findViewById(R.id.candidate_votes);
        }
    }
}

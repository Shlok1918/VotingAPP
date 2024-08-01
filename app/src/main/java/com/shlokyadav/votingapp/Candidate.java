package com.shlokyadav.votingapp;

public class Candidate {
    private int id;
    private String name;
    private int voteCount;

    public Candidate(int id, String name, int voteCount) {
        this.id = id;
        this.name = name;
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVoteCount() {
        return voteCount;
    }
}

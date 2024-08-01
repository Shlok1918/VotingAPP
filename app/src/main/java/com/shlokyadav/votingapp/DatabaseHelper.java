package com.shlokyadav.votingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "VotingApp.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_VOTES = "votes";
    private static final String TABLE_CANDIDATES = "candidates";
    private static final String COLUMN_CANDIDATE_NAME = "candidate_name";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PHONE = "userphone";


    private static final String COLUMN_IS_ADMIN = "is_admin";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CANDIDATE_ID = "candidate_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_IS_ADMIN + " INTEGER" + ")";

        String CREATE_VOTES_TABLE = "CREATE TABLE " + TABLE_VOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_CANDIDATE_ID + " INTEGER" + ")";

        String CREATE_CANDIDATES_TABLE = "CREATE TABLE " + TABLE_CANDIDATES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CANDIDATE_NAME + " TEXT" + ")";


        db.execSQL(CREATE_CANDIDATES_TABLE);

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_VOTES_TABLE);

        insertAdminUser(db);
    }

    private void insertAdminUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, "admin@votingapp.com");
        values.put(COLUMN_PASSWORD, "admin123");
        values.put(COLUMN_USERNAME, "Admin");
        values.put(COLUMN_PHONE, "1234567890");
        values.put(COLUMN_IS_ADMIN, 1);

        db.insert(TABLE_USERS, null, values);

        Log.d("adminData ", "inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOTES);
        onCreate(db);
    }
    public void addUser(String email, String password,String username,String userphone, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_PHONE,userphone);
        values.put(COLUMN_IS_ADMIN, isAdmin ? 1 : 0);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    public boolean authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean authenticated = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return authenticated;
    }

    public boolean isAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_IS_ADMIN + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean isAdmin = false;
        if (cursor.moveToFirst()) {
            isAdmin = cursor.getInt(0) == 1;
        }
        cursor.close();
        db.close();
        return isAdmin;
    }

    public boolean recordVote(int userId, int candidateId) {
        if (hasVoted(userId)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_CANDIDATE_ID, candidateId);

        db.insert(TABLE_VOTES, null, values);
        db.close();
        return true;
    }

    public boolean hasVoted(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_VOTES + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        boolean hasVoted = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return hasVoted;
    }


    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    public void addCandidate(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANDIDATE_NAME, name);

        db.insert(TABLE_CANDIDATES, null, values);
        db.close();
    }

    public Cursor getAllCandidates() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CANDIDATES, null);
    }


    public Cursor getCandidatesWithVoteCounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c." + COLUMN_ID + ", c." + COLUMN_CANDIDATE_NAME + ", COUNT(v." + COLUMN_ID + ") as vote_count "
                + "FROM " + TABLE_CANDIDATES + " c "
                + "LEFT JOIN " + TABLE_VOTES + " v ON c." + COLUMN_ID + " = v." + COLUMN_CANDIDATE_ID + " "
                + "GROUP BY c." + COLUMN_ID + ", c." + COLUMN_CANDIDATE_NAME;
        return db.rawQuery(query, null);
    }

    public String getVotedCandidateName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String candidateName = null;
        Cursor cursor = db.rawQuery(
                "SELECT candidates.candidate_name FROM votes INNER JOIN candidates ON votes.candidate_id = candidates.id WHERE votes.user_id = ?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            candidateName = cursor.getString(cursor.getColumnIndexOrThrow("candidate_name"));
        }

        cursor.close();
        return candidateName;
    }
}


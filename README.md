![VotingAPP_SS1](https://github.com/user-attachments/assets/2368630b-d8ec-4bfc-8acf-acf091b46bb3)
![VotingApp_SS](https://github.com/user-attachments/assets/aafe9761-00cd-42c3-b2f7-9ba64c0513ae)



The Voting App is an Android application designed to facilitate the voting process in an organized and user-friendly manner. It allows users to log in, view a list of candidates, and cast their votes. Admins can add candidates, manage users, and view voting results. The app ensures that each user can only vote once and provides feedback if they have already voted.

**Features**

**User Authentication:**
Users can register and log in.
Secure authentication to verify user credentials.

**Candidate Management:**
Admins can add, edit, and delete candidates.
Users can view a list of all candidates.

**Voting Process:**
Users can select a candidate and cast their vote.
The app checks if the user has already voted and prevents multiple votes.

**Voting Results:**
Admins can view the voting results with vote counts for each candidate.

**User Feedback:**
If a user attempts to vote again, they are notified that they have already voted.
A dialog prompts users who have already voted to log out.

**Logout Functionality:**
Users can log out of the app, which clears their session data.
Technical Stack
Android Studio: Used for app development with Java.
SQLite: For local database management.
SharedPreferences: To store session data like user email and admin status.
RecyclerView: To display the list of candidates.
AlertDialog: To show dialog messages to users.
Key Classes and Components

**LoginPage:**
Handles user login and authentication.
Stores user session data in SharedPreferences.

**VotingPage:**
Displays the list of candidates.
Allows users to cast their vote.
Checks if the user has already voted and shows appropriate feedback.

**DatabaseHelper:**
Manages database operations including user authentication, candidate management, and voting records.
Provides methods to check if a user has voted and to retrieve the candidate they voted for.

**VotingCandidateAdapter:**
Binds candidate data to the RecyclerView for display.
Usage Flow

**User Registration and Login:**
Users register with their email and password.
On successful login, users are redirected to the voting page.

**Viewing Candidates and Voting:**
Users view a list of candidates.
Users select a candidate and cast their vote.
The app records the vote and ensures the user cannot vote again.

**Admin Functions:**
Admins log in with their credentials.
Admins can manage candidates and view voting results.

**Logout:**
Users and admins can log out, which clears their session and returns them to the login page.
This project provides a comprehensive solution for managing an election process through a mobile application, ensuring a smooth, secure, and user-friendly experience for both voters and administrators.

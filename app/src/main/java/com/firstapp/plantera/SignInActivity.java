package com.firstapp.plantera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

public class SignInActivity extends AppCompatActivity {

    ImageButton googleButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private ActivityResultLauncher<Intent> signInLauncher;
    private static final String TAG = "SignInActivity"; // Add this line for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        googleButton = findViewById(R.id.googleButton);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                handleSignInResult(data);
            } else {
                Toast.makeText(this, "Error! Problem Signing In", Toast.LENGTH_SHORT).show();
            }
        });

        googleButton.setOnClickListener(v -> SignIn());
    }

    private void SignIn() {
        Log.d(TAG, "Starting sign-in process"); // Add this log

        Intent intent = gsc.getSignInIntent();
        signInLauncher.launch(intent);
    }

    private void handleSignInResult(Intent data) {
        try {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
            Log.d(TAG, "Sign-in successful for user: " + account.getDisplayName()); // Add this log

            HomeActivity();
        } catch (ApiException e) {
            Log.e(TAG, "Sign-in failed with status code: " + e.getStatusCode()); // Add this log

            Toast.makeText(this, "Error! Problem Signing In", Toast.LENGTH_SHORT).show();
        }
    }

    private void HomeActivity() {
        // Store a flag indicating the user is signed in
        setSignedInFlag();

        // Navigate to the main navigation menu with HomeFragment open
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("open_home_fragment", true);
        startActivity(intent);
        finish();
    }

    private void setSignedInFlag() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("signed_in", true);
        editor.apply();
    }

    public void onGoogleButtonClick(View view) {
        // For the Google Sign-In button click
        SignIn();
    }
}

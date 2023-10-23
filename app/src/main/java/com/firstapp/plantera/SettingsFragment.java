package com.firstapp.plantera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button signOutButton = view.findViewById(R.id.sign_out_button);
        CardView signOutCard = view.findViewById(R.id.sign_out_card);

        // Retrieve the signed-in Google account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());

        if (account != null) {
            // Display user information in the UI
            TextView userNameTextView = view.findViewById(R.id.user_name_text_view);
            TextView userEmailTextView = view.findViewById(R.id.user_email_text_view);
            ImageView userImageView = view.findViewById(R.id.user_image_view);

            String userName = account.getDisplayName();
            String userEmail = account.getEmail();
            String userPhotoUrl = String.valueOf(account.getPhotoUrl());

            userNameTextView.setText(userName);
            userEmailTextView.setText(userEmail);

            // Load and display the user's profile picture using Picasso
            if (userPhotoUrl != null) {
                Picasso.get().load(userPhotoUrl).into(userImageView);
            }
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(requireContext(), gso); // Initialize GoogleSignInClient

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform sign-out logic here
                signOut();
            }
        });

        signOutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform sign-out logic here when the CardView is clicked
                signOut();
            }
        });

        return view;
    }

    private void signOut() {
        // Sign out the user
        gsc.signOut().addOnCompleteListener(requireActivity(), task -> {
            // Clear the "signed_in" flag
            clearSignedInFlag();
            // Start SignInActivity
            startSignInActivity();

            // Show a "Signed out" toast message
            showSignedOutToast();
        });
    }

    private void showSignedOutToast() {
        Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_SHORT).show();
    }




    private void clearSignedInFlag() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("signed_in");
        editor.apply();
    }

    private void startSignInActivity() {
        Intent intent = new Intent(requireContext(), SignInActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}

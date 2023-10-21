package com.firstapp.plantera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check the "signed_in" flag in SharedPreferences
        boolean isSignedIn = checkSignedInFlag();

        if (isSignedIn) {
            // User was signed in, go to HomeFragment
            startHomeFragment();
        } else {
            // User was not signed in, go to SignInActivity
            startSignInActivity();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Handle fragment navigation
            Fragment selectedFragment = FragmentNavigator.selectFragment(item.getItemId(), isSignedIn);
            if (selectedFragment != null) {
                FragmentNavigator.replaceFragment(this, selectedFragment);
            }
            return true;
        });

        // Check if the "open_home_fragment" flag is set
        if (getIntent().getBooleanExtra("open_home_fragment", false)) {
            // Open the Home fragment
            startHomeFragment();
        }
    }


    private boolean checkSignedInFlag() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("signed_in", false);
    }

    private void startHomeFragment() {
        Fragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    private void startSignInActivity() {
        // Start SignInActivity
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish(); // Finish MainActivity
    }
}

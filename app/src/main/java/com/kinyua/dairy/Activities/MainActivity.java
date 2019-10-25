package com.kinyua.dairy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kinyua.dairy.Fragments.AddFragment;
import com.kinyua.dairy.Fragments.DiscoverFragment;
import com.kinyua.dairy.Fragments.ProfileFragment;
import com.kinyua.dairy.Helpers.BottomViewHelper;
import com.kinyua.dairy.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    fragment = new DiscoverFragment();
                    toolbar.setTitle("Dairy Animals");
                    break;
                case R.id.navigation_add:
                    fragment = new AddFragment();
                    toolbar.setTitle("Add Animals");
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    toolbar.setTitle("Profile");
                    break;
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dairy Animals");
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        DiscoverFragment discoverFragment = new DiscoverFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_container, discoverFragment).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomViewHelper.removeNavigationShiftMode(navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}

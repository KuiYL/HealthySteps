package com.example.healthysteps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.healthysteps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String SELECTED_FRAGMENT_TAG = "selected_fragment_tag";

    private int selectedItem;
    private String selectedFragmentTag;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.healthysteps.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(SELECTED_ITEM_ID);
            selectedFragmentTag = savedInstanceState.getString(SELECTED_FRAGMENT_TAG);
        } else {
            selectedItem = R.id.home;
            selectedFragmentTag = "home_fragment"; // Задайте тег фрагмента по умолчанию
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            selectedItem = item.getItemId();
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment(), "home_fragment");
                    break;
                case R.id.receipt:
                    replaceFragment(new ReceiptFragment(), "receipt_fragment");
                    break;
                case R.id.account:
                    AccountFragment accountFragment = new AccountFragment();

                    // Создание Bundle и передача события входа в систему
                    Bundle bundle = new Bundle();
                    bundle.putString("loginEvent", "Вход выполнен успешно");
                    accountFragment.setArguments(bundle);

                    replaceFragment(new AccountFragment(), "account_fragment");
                    break;
            }
            return true;
        });

        binding.bottomNavigationView.setSelectedItemId(selectedItem);
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, selectedItem);
        outState.putString(SELECTED_FRAGMENT_TAG, selectedFragmentTag);
    }


}

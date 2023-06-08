package com.example.healthysteps;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ReceiptFragment extends Fragment implements SearchView.OnQueryTextListener {
    private GridView gridView;
    private SearchView searchView;
    private ArrayList<String> filteredStringArray;
    private ImageAdapter imageAdapter;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        gridView = view.findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(requireActivity());
        gridView.setAdapter(imageAdapter);

        searchView = view.findViewById(R.id.Search);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterItems(newText);
        return true;
    }

    private void filterItems(String query) {
        filteredStringArray = new ArrayList<>();

        if (query.isEmpty()) {
            filteredStringArray.addAll(Arrays.asList(imageAdapter.getStringArray()));
        } else {
            query = query.toLowerCase();
            for (String item : imageAdapter.getStringArray()) {
                if (item.toLowerCase().contains(query)) {
                    filteredStringArray.add(item);
                }
            }
        }

        imageAdapter.setFilteredStringArray(filteredStringArray);
        gridView.setAdapter(imageAdapter);
    }
}
package com.example.proyecto.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto.BuildConfig;
import com.example.proyecto.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class HomeFragment extends Fragment {
    private MapView mapView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mapView = getView().findViewById(R.id.mapView);
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getActivity().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getActivity().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }
}
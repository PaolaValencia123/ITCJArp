package com.example.proyecto.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto.BuildConfig;
import com.example.proyecto.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
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


        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // put your own code here
        mapView = getActivity().findViewById(R.id.mapView);

        double[] center = {31.720714, -106.422508};
        System.out.println("Prueba 1");
        // Applying parameters
        mapView.post(
                new Runnable() {
                    @Override
                    public void run() {
                        double[] center = {31.720714, -106.422508};

                        mapView.setTileSource(TileSourceFactory.MAPNIK);
                        mapView.setMultiTouchControls(true);
                        IMapController mapController = mapView.getController();
                        mapController.setZoom(16.0);

                        GeoPoint  currentLocation = new GeoPoint(center[0],center[1]);
                        mapController.setCenter(currentLocation);
                        System.out.println("Prueba");
                    }
                }
        );
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
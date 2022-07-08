package com.example.proyecto.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.proyecto.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class MapActivity extends AppCompatActivity {
    private MapView mapView = null;
    private FloatingActionButton info_clasification;

    private GeoPoint[] ubicacionContenedores = {
            new GeoPoint(31.721588145085963, -106.42382084373638),
            new GeoPoint(31.721271, -106.423081),
            new GeoPoint(31.72043288365676, -106.42233885112591),
            new GeoPoint(31.72071027657493, -106.42134940358919),
            new GeoPoint(31.71988464415384, -106.42244771859868),
            new GeoPoint(31.719648619365444, -106.42202408166553),
            new GeoPoint(31.71930722301224, -106.42169210702619),
            new GeoPoint(31.71878472591591, -106.42175950250721),
            new GeoPoint(31.71848915597332, -106.4218670534107),
            new GeoPoint(31.719006404130727, -106.42249787387615)
    };

    private String[] ubicacionesTitle = {
            "Edificio Administrativo: 1",
            "Edificio Rivera Lara: 1",
            "División de estudios profesionales: 1",
            "Educación a distancia: 1",
            "Edificio Guillot: 1",
            "Gestión y vinculación: 1",
            "Departamento de Ing. Industrial: 1",
            "Centro de lenguas: 1",
            "Posgrado: 1",
            "Departamento de sistemas: 2"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);

        double[] center = {31.720714, -106.422508};

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        IMapController mapController = mapView.getController();
        mapController.setZoom(18.0);

        GeoPoint currentLocation = new GeoPoint(center[0], center[1]);
        mapController.setCenter(currentLocation);

        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_recycle_background, null);
        Drawable newMarker = this.getResources().getDrawable(R.drawable.ic_recycle_background);
        generateMarkers(mapView, resize(newMarker));


        info_clasification = findViewById(R.id.FbInfo);

        info_clasification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

    }

    private void showEditDialog() {

        FragmentManager fm = getSupportFragmentManager();

        fragmentInfo editNameDialogFragment = fragmentInfo.newInstance();

        editNameDialogFragment.show(fm, "Informacion");

    }

    private void generateMarkers(MapView mapView, Drawable markerIcon){
        int index = 0;
        for (GeoPoint ubicacionContenedore : ubicacionContenedores) {


            Marker newMarker = new Marker(mapView);
            newMarker.setIcon(markerIcon);
            newMarker.setPosition(ubicacionContenedore);
            newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            newMarker.setTitle(ubicacionesTitle[index++]);
            mapView.getOverlays().add(newMarker);
        }

        mapView.invalidate();
    }

    private Drawable resize(Drawable drawable) {
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            return new BitmapDrawable(getResources(), bitmapResized);
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }
}
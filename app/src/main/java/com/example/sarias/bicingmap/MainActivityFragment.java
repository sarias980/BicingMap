package com.example.sarias.bicingmap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArrayList<Station> result;
    private MapView map;
    private MyLocationNewOverlay myLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private CompassOverlay mCompassOverlay;
    private IMapController mapController;
    private RadiusMarkerClusterer bicingMarkers;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        map = (MapView) view.findViewById(R.id.map);
        this.result= new ArrayList<>();

        initialMap();
        setZoom();
        setOverlays();
        setMarker();

        return view;
    }

    private void setMarker() {
        bicingMarkers= new RadiusMarkerClusterer(getContext());
        map.getOverlays().add(bicingMarkers);

        Drawable clusterIconD = getResources().getDrawable(R.drawable.moreinfo_arrow);
        Bitmap clusterIcon = ((BitmapDrawable)clusterIconD).getBitmap();

        bicingMarkers.setIcon(clusterIcon);
        bicingMarkers.setRadius(100);

        RefreshDataTask refreshDataTask = new RefreshDataTask();
        refreshDataTask.execute();
    }

    private void setZoom() {
        mapController = map.getController();
        mapController.setZoom(14);

    }

    private void initialMap() {
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setTilesScaledToDpi(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
    }

    private void setOverlays() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();

        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this.getActivity()
        ), map);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo(myLocationOverlay
                        .getMyLocation());
            }
        });

        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        mCompassOverlay = new CompassOverlay(
                getContext(),
                new InternalCompassOrientationProvider(getContext()),
                map
        );
        mCompassOverlay.enableCompass();

        map.getOverlays().add(myLocationOverlay);
        map.getOverlays().add(this.mScaleBarOverlay);
        map.getOverlays().add(this.mCompassOverlay);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        initialMap();
        setZoom();
        setOverlays();
        setMarker();
    }

    private class RefreshDataTask extends AsyncTask<Void, Void, ArrayList<Station>> {
        @Override
        protected void onPostExecute(ArrayList<Station> stations) {

            for(Station station : stations) {
                Marker marker = new Marker(map);
                GeoPoint point = new GeoPoint(
                        Double.parseDouble(station.getLatitude()),
                        Double.parseDouble(station.getLongitude())
                );

                Log.d("GeoAAA", point.toString());

                marker.setPosition(point);

                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                int valor = (station.getBikes() * 100);
                int porcentaje =  0;

                try {
                    porcentaje = valor / station.getSlots();
                } catch (Exception e) {

                }

                marker.setTitle(station.getStreetName());
                String description = "Bicis libres: " + station.getBikes();

                if(station.getType().toLowerCase().contains("electric"))
                    description = description.concat(" - ELECTRICA");

                marker.setSubDescription(description);
                marker.setAlpha(0.6f);

                if(porcentaje == 0) {
                    marker.setIcon(getResources().getDrawable(R.drawable.ic_action_00));
                } else if (porcentaje > 0 && porcentaje <= 25) {
                    marker.setIcon(getResources()
                            .getDrawable(R.drawable.ic_action_25));
                } else if (porcentaje > 25 && porcentaje <= 50) {
                    marker.setIcon(getResources()
                            .getDrawable(R.drawable.ic_action_50));
                } else if (porcentaje > 50 && porcentaje <= 75) {
                    marker.setIcon(getResources()
                            .getDrawable(R.drawable.ic_action_75));
                } else {
                    marker.setIcon(getResources()
                            .getDrawable(R.drawable.ic_action_100));
                }
                bicingMarkers.add(marker);
            }

            bicingMarkers.invalidate();
            map.invalidate();
        }

        @Override
        protected ArrayList<Station> doInBackground(Void... voids) {
            BikeAPI api = new BikeAPI();
            result = api.getStations();

            return result;
        }
    }


}

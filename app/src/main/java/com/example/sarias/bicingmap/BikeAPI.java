package com.example.sarias.bicingmap;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sarias on 04/02/2018.
 */

public class BikeAPI {
    private final String BASE_URL = "http://wservice.viabicing.cat/v2/stations";

    ArrayList<Station> getStations() {
        return doCall();
    }

    private String getUrlPage() {
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .build();
        return builtUri.toString();
    }

    private ArrayList<Station> doCall() {
        ArrayList<Station> station = new ArrayList<>();

        try{
            String url = getUrlPage();
            String JsonResponse = HttpUtils.get(url);
            ArrayList<Station> list = processJason(JsonResponse);
            station.addAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return station;
    }

    private ArrayList<Station> processJason(String jsonResponse) {
        ArrayList<Station> stations = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(jsonResponse);
            JSONArray jsonStation = data.getJSONArray("stations");
            for (int i = 0; i < jsonStation.length(); i++) {
                JSONObject jsonstation = jsonStation.getJSONObject(i);

                Station station = new Station();
                if(jsonstation.has("id")){station.setIdNum(jsonstation.getInt("id"));}
                if(jsonstation.has("type")){station.setType(jsonstation.getString("type"));}
                if(jsonstation.has("latitude")){station.setLatitude(jsonstation.getString("latitude"));}
                if(jsonstation.has("longitude")){station.setLongitude(jsonstation.getString("longitude"));}
                if(jsonstation.has("streetName")){station.setStreetName(jsonstation.getString("streetName"));}
                if(jsonstation.has("streetNumber")){station.setStreetNumber(jsonstation.getInt("streetNumber"));}
                if(jsonstation.has("altitude")){station.setAltitude(jsonstation.getInt("altitude"));}
                if(jsonstation.has("slots")){station.setSlots(jsonstation.getInt("slots"));}
                if(jsonstation.has("bikes")){station.setBikes(jsonstation.getInt("bikes"));}

                stations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stations;
    }
}

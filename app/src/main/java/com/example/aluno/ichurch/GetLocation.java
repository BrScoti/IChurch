package com.example.aluno.ichurch;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.aluno.ichurch.Remote.IGoogleAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.aluno.ichurch.MainActivity.*;
import static com.example.aluno.ichurch.MainActivity.CURRENT_ZOOM;
import static com.example.aluno.ichurch.MainActivity.TAG;


/**
 * Created by aluno on 12/09/2018.
 */

public class GetLocation {
    protected GoogleMap mMap;
      FusedLocationProviderClient mFusedLocationProviderClient;
    protected double latitude,longitude;
    protected  Location currentLocation;
    private Runnable runnable;
    protected IGoogleAPIService mService;
    public GetLocation(FusedLocationProviderClient mFusedLocationProviderClient, GoogleMap mMap){
            this.mFusedLocationProviderClient=mFusedLocationProviderClient;
            this.mMap=mMap;

            getDeviceLocation();
    }



    protected void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: obtendo a localização atual");

        try {
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Localização encontrada");
                       currentLocation = (Location) task.getResult();

                        //nearbyPlaces(currentLocation.getLatitude(),currentLocation.getLongitude());
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), CURRENT_ZOOM);

                        runnable.run();
                    } else {
                       // Toast.makeText(getApplicationContext(), "Erro ao encontrar localização", Toast.LENGTH_SHORT);
                    }

                }
            });
        } catch (SecurityException e) {
           // Log.d(TAG, "getDeviceLocation: Erro " + e.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom) {
        //Log.d(TAG, "moveCamera: " + latLng);
       latitude= latLng.latitude;
        longitude=latLng.longitude;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

    }
}

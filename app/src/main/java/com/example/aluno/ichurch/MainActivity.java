package com.example.aluno.ichurch;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluno.ichurch.Model.MyPlaces;
import com.example.aluno.ichurch.Model.Results;
import com.example.aluno.ichurch.Remote.IGoogleAPIService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.firebase.ui.auth.AuthUI;




import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    protected static final String TAG = "MainActivity";
    protected static final int ERROR_DIALOG_REQUEST = 9001;
    protected static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    protected static float CURRENT_ZOOM = 15f;

    protected GoogleMap mMap;

    protected boolean mLocationPermissionsGranted = false;
    protected FusedLocationProviderClient mFusedLocationProviderClient;

    protected NavigationView navigationView;
    protected  double latitude, longitude;

    protected IGoogleAPIService mService;
    private FirebaseAuth mAuth;
    private Button btnLogin,btnLogout;
    private TextView userName,userEmail;

    private View userLayout;


    private MyPlaces currentPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        getLocationPermission();
        if (isServiceOK()) {
            mService = Common.getGoogleAPIService();

            if (mLocationPermissionsGranted) {

                initMap();
                mAuth=FirebaseAuth.getInstance();

                if(mAuth.getCurrentUser()!=null){
                    Toast.makeText(getApplicationContext(),"OPA",Toast.LENGTH_LONG).show();
                     loginSucess();
                }

            }
        }

    }

    //Avalia se as versões dos aplicativos da Google estão funcionando
    public boolean isServiceOK() {
        Log.d(TAG, "isServiceOK: checando versão dos serviços do google ");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //Tudo está bem e o usuário pode fazer requesições no mapa
            Log.d(TAG, "isServiceOk: Google Play Services está funcionando");

            return true;

        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Ocorreu um erro mas isso pode ser resolvido
            Log.d(TAG, "isServiceOk: Aconteceu um erro mas pode ser arrumado");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();


        } else {
            Toast.makeText(this, "Não é possível fazer requições do mapa", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }
        if(id== R.id.action_restart){
          getDeviceLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        navigationView= findViewById(R.id.nav_view);
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setVisible(false);
        }
        userName= findViewById(R.id.userName);
        userEmail= findViewById(R.id.userEmail);
        userLayout=findViewById(R.id.userLayout);

    }



    /**
     * orMapFragment. This method will only be triggered once the user has
     * installed Google Play services and .Manipulates the map once availablee map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a tmarker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the Supp
     * This callback is triggered when threturned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        btnLogin = findViewById(R.id.btnLogin);

        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Common.currentResult= currentPlace.getResults()[Integer.parseInt(marker.getSnippet())];


                startActivity(new Intent(MainActivity.this,ViewPlace.class));

                return true;
            }
        });
    }
    //Pede requisição de localização
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        this.getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionsGranted = true;


        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1234);
            Log.d(TAG, "getLocationPermission: Requesitando permissão ");
        }
    }

    //Aguarda a resposta do usuário para a requisição
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1234) {


            if (grantResults.length > 0) {
                Log.d(TAG, "onRequestPermissionsResult: Código igual ");
                for (int i = 0; i < grantResults.length; i++) {
                    Log.d(TAG, "Resultados:" + grantResults[i]);
                    Log.d(TAG, "Resultado esperado:" + PackageManager.PERMISSION_GRANTED);
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = false;
                        Log.d(TAG, "onRequestPermissionsResult: Não foi possível obter permissão ");
                        return;
                    }
                }
                mLocationPermissionsGranted = true;


                initMap();
            }
        }
    }




    //Recebe o local do usuário
    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: obtendo a localização atual");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


       // getLocation= new GetLocation(mFusedLocationProviderClient,mMap);


          try {
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Localização encontrada");
                        Location currentLocation = (Location) task.getResult();

                        nearbyPlaces(currentLocation.getLatitude(),currentLocation.getLongitude());
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), CURRENT_ZOOM);
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao encontrar localização", Toast.LENGTH_SHORT);
                    }
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Erro " + e.getMessage());
        }
    }
    //Move a camera

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: " + latLng);
        latitude= latLng.latitude;
        longitude=latLng.longitude;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

    }
        public void logIn(View view) {
        Log.d("login", "Iniciando Login");
        Toast.makeText(getApplicationContext(),"Iniciando Login",Toast.LENGTH_SHORT).show();
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .build(),
                        123
                );
            } else {
                Log.d("TESTE",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                Toast.makeText(this,
                        "Bem vindo(a) " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_LONG)
                        .show();
                mAuth=FirebaseAuth.getInstance();

                loginSucess();


            }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            Toast.makeText(getApplicationContext(),requestCode+"",Toast.LENGTH_LONG).show();
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Login bem sucedido!",
                        Toast.LENGTH_LONG)
                        .show();
                mAuth=FirebaseAuth.getInstance();

                loginSucess();

            } else {
                Toast.makeText(this,
                        "Erro de login.",
                        Toast.LENGTH_LONG)
                        .show();
                startActivity(new Intent(MainActivity.this, MainActivity.class));

                finish();
            }
        }
    }

    public void logOut(View v){
        if (v.getId() == R.id.btnLogOut) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            logoutSucess();

                        }
                    });
        }
    }
    public void loginSucess(){
        mAuth=FirebaseAuth.getInstance();

        //Toast.makeText(getApplicationContext(),btnLogin+"",Toast.LENGTH_LONG).show();

            //  btnLogin.setVisibility(View.INVISIBLE);

            //String t=mAuth.getCurrentUser()+"";
            userLayout.setVisibility(View.VISIBLE);
            userName.setText("" + mAuth.getCurrentUser().getDisplayName());
            userEmail.setText("" + mAuth.getCurrentUser().getEmail());
           btnLogin.setVisibility(View.INVISIBLE);
            //Toast.makeText(getApplicationContext(),btnLogin+"",Toast.LENGTH_LONG).show();
    }
    public void logoutSucess(){
            userLayout.setVisibility(View.INVISIBLE);
            userName.setText("");
            userEmail.setText("");
             btnLogin.setVisibility(View.VISIBLE);
    }


    //Utilizando Retrofit faz a pesquisa dos locais
    public void nearbyPlaces(final double  lat, final double  lng){
        String url= getUrl(lat,lng);
         mService.getNearbyPlaces(url)
                .enqueue(new Callback<MyPlaces>() {
                    @Override
                    public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                        Log.d("nearbyPlaces","Tentando localizar locais");
                        currentPlace= response.body();
                        if(response.isSuccessful()){
                            for (int i=0;i<response.body().getResults().length;i++){
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    Results googlePlace = response.body().getResults()[i];
                                    String placeName = googlePlace.getName();

                                if( ! (placeName.toLowerCase().contains("adventista") || placeName.toLowerCase().contains("seminário") || placeName.toLowerCase().contains("casa") || placeName.toLowerCase().contains("evangélica"))) {

                                    double lat1 = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                                    double lng1 = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                                    String vicinity = googlePlace.getFormatted_address();
                                    LatLng latLng = new LatLng(lat1, lng1);
                                    markerOptions.position(latLng);
                                    markerOptions.title(placeName);
                                    markerOptions.snippet(String.valueOf(i));
                                    LatLng latLngZoom = new LatLng(lat, lng);
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                   // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngZoom));
                                  //  mMap.animateCamera(CameraUpdateFactory.zoomTo(CURRENT_ZOOM));
                                    mMap.addMarker(markerOptions);
                                    Log.d("nearbyPlaces", "Locais encontrados com sucesso");
                                    Log.d("nearbyPlaces", lat1 + "," + lng1);
                                }
                                }
                        }
                    }

                    @Override

                    public void onFailure(Call<MyPlaces> call, Throwable t) {
                        Log.d(TAG, "nearbyPlaces: Locais não encontrados");
                    }
                });
    }


    //Gera URL para requisição HTTP
    private String getUrl(double latitude, double longitude) {
        //StringBuilder googlePlacesUrl= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=church&keyword=catolica");


        StringBuilder googlePlacesUrl= new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?query=catholic+church");
        googlePlacesUrl.append("&radius="+1000);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&language=pt-BR");
        googlePlacesUrl.append("&location="+latitude+","+longitude);

        googlePlacesUrl.append("&key="+getResources().getString(R.string.browser_key));
        Log.d("getUrl:",googlePlacesUrl.toString());
        return googlePlacesUrl.toString();
    }

}

package com.survey.innovation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.survey.innovation.R;

import static android.net.Uri.*;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private Button btnSndData;
    private String gasPriceValue = "";
    private String barRate = "";
    private TextView gasPrice;
    private GasData data;
    private String TAG = "DATA_SEND";
    private RatingBar rb = null;
    ArrayList<GasStation> estaciones = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnSndData = (Button)findViewById(R.id.btndSnd);
        gasPrice = (TextView)findViewById(R.id.gas_price);
        rb = (RatingBar)findViewById(R.id.ratingBar);
        btnSndData.setOnClickListener(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void onNormalMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onSatelliteMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onTerrainMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void onHybridMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void onSendData(View view){
        Toast.makeText(this,"onSendData",Toast.LENGTH_LONG).show();
        Log.i("OnSendData","Hola Send Data on function");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.i(TAG,"en On Connected");
            Log.i(TAG,"Array Size:"+estaciones.size());
        } catch (SecurityException e) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        //Log.i(TAG,"Terminando...");
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng sydney = null;
        // Add a marker in Sydney and move the camera
        /*if(mLastLocation != null){
            sydney = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney));
        }else {
            sydney = new LatLng(19.2555977, -99.4683496);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Ocoyoacac, Mexico"));
        }*/



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        new GetJsonArray(mMap).execute("http://gasstations.000webhostapp.com/get_locations.php");
        //Log.i(TAG,"BanderaOnMapReady->"+flag);
        //while (flag){}
        /*for(GasStation gas : estaciones){
            Log.i("JSONArray","Creando el marcador "+gas.getPrice());
            mMap.addMarker(new MarkerOptions().position(new LatLng(gas.getLat(),gas.getLon())).title(gas.getPrice()));
        }*/

    }

    //@Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btndSnd:
                //if(!validate())
               // Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                //Log.i("ONCLick","En el onclick "+gasPrice.getText().toString());
                gasPriceValue = gasPrice.getText().toString();
                barRate = String.valueOf(rb.getRating());
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute("http://gasstations.000webhostapp.com/read_gas.php");
                break;
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        //@Override
        protected String doInBackground(String... urls) {
            data = new GasData();
            if(mLastLocation!=null){
                data.setLat(String.valueOf(mLastLocation.getLatitude()));
                data.setLon(String.valueOf(mLastLocation.getLongitude()));
                data.setPrice(gasPriceValue);
                data.setBarRate(barRate);
            }

            Log.i("DataSend","Lat: "+data.getLat()+" Lon: "+data.getLon());
            return POST(urls[0],data);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent! "+result, Toast.LENGTH_LONG).show();
        }
    }

    public static String POST(String link, GasData data){
        URL url = null;
        try {
            url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Builder builder = new Builder()
                    .appendQueryParameter("lat", data.getLat())
                    .appendQueryParameter("lon", data.getLon())
                    .appendQueryParameter("price", data.getPrice())
                    .appendQueryParameter("calif",data.getBarRate());
            String query = builder.build().getEncodedQuery();
            Log.i("DATA_SEND","Query:"+query);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                /*String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }*/
                Log.i("SEND","Envio correcto: "+responseCode);
            }
            else {
                //response="";
                Log.i("SEND","Envio incorrecto: "+responseCode);
            }
            conn.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    private class GetJsonArray extends AsyncTask<String, Void, String> {

        private GoogleMap mlMap;

        public GetJsonArray(GoogleMap mmMap ){
            this.mlMap = mmMap;
        }

        @Override
        protected String doInBackground(String... params) {
            //ArrayList<GasStation> ag = new ArrayList<GasStation>();
            estaciones = new ArrayList<GasStation>();
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();

                JSONArray jArray = new JSONArray(sb.toString());
                for(int i=0;i<jArray.length();i++){
                    //if(i==1) {break;}
                    JSONObject e = jArray.getJSONObject(i);
                    Log.i("JSAONARRAY","lat"+e.getString("lat")+" lon:"+e.getString("lon")+" price:"+e.getString("price"));
                    estaciones.add(new GasStation(Double.valueOf(e.getString("lat")), Double.valueOf(e.getString("lon")),e.getString("price"), e.getString("date")));
                }
                //Log.i("JSAONARRAY","estaciones.size "+estaciones.size());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(), "Data Read! "+s, Toast.LENGTH_LONG).show();

            for (GasStation ga : estaciones) {
                mlMap.addMarker(new MarkerOptions().position(new LatLng(ga.getLat(), ga.getLon())).title("$"+ga.getPrice()+" "+ga.getDate()).visible(true)); //.icon(BitmapDescriptorFactory.fromResource(R.drawable.gasstation))
            }
            CameraPosition CENTER = CameraPosition.builder().
                    target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).
                    zoom(16).
                    bearing(0).
                    tilt(45).
                    build();

            mlMap.moveCamera(CameraUpdateFactory.newCameraPosition(CENTER));
        }
    }

}

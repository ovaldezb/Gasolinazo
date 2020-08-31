package com.survey.innovation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.survey.innovation.R;

/**
 * Created by omar.valdez on 12/01/2017.
 */

public class Survey extends Activity {

    private Button avanza;
    private TextView marca;
    private TextView modelo;
    private TextView auto;
    private TextView kilom;
    private TextView comments;
    private TextView autonuevo;
    private static String TAG = "SURVEY";
    private String android_id ="";
    private  SurveyData sd = null;
    Context context = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"Antes del set content");
        setContentView(R.layout.survey);
        Log.i(TAG,"Despues del set content");
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        context = this;
        marca = (TextView)findViewById(R.id.editMarca);
        modelo = (TextView)findViewById(R.id.editModelo);
        auto = (TextView)findViewById(R.id.editAuto);
        kilom = (TextView)findViewById(R.id.editKm);
        comments = (TextView)findViewById(R.id.editCmts);
        autonuevo = (TextView)findViewById(R.id.editAutnvo);

        avanza = (Button) findViewById(R.id.avanza);
        avanza.setBackgroundColor(Color.BLUE);

        avanza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(validateText()) {
                    avanza.setEnabled(false);
                    avanza.setBackgroundColor(Color.LTGRAY);
                    //new HttpAsyncTask.execute();
                    sd = new SurveyData(marca.getText().toString(),modelo.getText().toString(),
                            auto.getText().toString(),kilom.getText().toString(),comments.getText().toString(),autonuevo.getText().toString(),android_id);
                    //ctx = getContext();

                    new HttpAsyncTask().execute("http://gasstations.000webhostapp.com/save_survey.php");
                }else{
                    Toast.makeText(getBaseContext(),"Datos Incompletos",Toast.LENGTH_LONG);
                }

            }

        });
    }

    private boolean validateText(){
        boolean flag = true;
        if(marca.getText().length()==0) {
            flag = false;
            marca.requestFocus();
        }else if (modelo.getText().length()==0) {
            flag = false;
            modelo.requestFocus();
        }else if (auto.getText().length()==0){
            auto.requestFocus();
            flag = false;
        }else if (kilom.getText().length()==0){
            flag = false;
            kilom.requestFocus();
        }else if (comments.getText().length()==0){
            flag = false;
            comments.requestFocus();
        }else if (autonuevo.getText().length()==0){
            flag = false;
            autonuevo.requestFocus();
        }
        return flag;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {


        //@Override
        protected String doInBackground(String... urls) {


            return POST(urls[0],sd);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent! "+result, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MapActivity.class);
            startActivity(intent);
        }
    }

    public static String POST(String link, SurveyData data){
        URL url = null;
        String ret = "";
        try {
            url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("marca", data.getMarca())
                    .appendQueryParameter("modelo", data.getModelo())
                    .appendQueryParameter("auto", data.getAuto())
                    .appendQueryParameter("kilom", data.getKilom())
                    .appendQueryParameter("comments", data.getComments())
                    .appendQueryParameter("newcar", data.getAutonvo())
                    .appendQueryParameter("iddevice", data.getIdDevice());
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
            Log.i(TAG,conn.getResponseMessage());
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                /*String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }*/
                ret = "OK!";
                Log.i("SEND","Envio correcto: "+responseCode);
            }
            else {
                ret = "Error!";
                Log.i("SEND","Envio incorrecto: "+responseCode);
            }
            conn.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            ret = "Error!";
            Log.i("SEND","Envio incorrecto: "+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}

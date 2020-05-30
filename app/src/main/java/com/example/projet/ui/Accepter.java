package com.example.projet.ui;

import com.example.projet.R;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Accepter extends AppCompatActivity {

    private JSONArray json;
    private TextView title;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Spinner spinner1, spinner2, spinner;
    private static String semaine,classe,cours;
    private Button fiche,statisique;
    private static JSONArray jsonarray;
    private static Map<String, String> abs;
    private static ArrayList<Map<String,String>> feuilleabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logok);

        json = MainActivity.getJson();
        Enseignent enseignent = null;
        System.out.println(json.length());

        try {
            JSONObject prof = json.getJSONObject(0);
            enseignent = new Enseignent(prof.optString("nom"),prof.optString("prenom"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        title = (TextView) findViewById(R.id.title);
        title.setText( enseignent.getNom() + " " + enseignent.getPrenom());

        addItemsOnSpinnerClasse(EnseignentClasse(json));
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

    }
    public ArrayList<String> EnseignentClasse(JSONArray classe){

        JSONObject list;
        ArrayList<String> niveau = new ArrayList<>();

        int sommet = 0;
        do {
            list = null;

            try {
                list = classe.getJSONObject(sommet++);
                if (!niveau.contains(list.optString("id_classe")))
                    niveau.add(list.optString("id_classe"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }while(list != null);
        return niveau;
    }
    public ArrayList EnseignentCours(String idClasse) throws JSONException {
        ArrayList < String > coursfiltre = new ArrayList<>();
        for (int i = 0 ; i < json.length() ; i++){
            if (json.getJSONObject(i).optString("id_classe").equals(idClasse)){
                coursfiltre.add(json.getJSONObject(i).optString("nomCours"));
            }
        }
        return coursfiltre;

    }

    // add items into spinner dynamically
    public void addItemsOnSpinnerClasse(ArrayList <String> classe ) {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, classe);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }


    public void addListenerOnSpinnerItemSelection() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener2());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner = (Spinner) findViewById(R.id.spinner);
        fiche = (Button) findViewById(R.id.btnSubmit);
        statisique = (Button) findViewById(R.id.statistique);


        fiche.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (spinner1.getSelectedItem().toString().equals("Choisir une semaine")){
                    Toast.makeText(Accepter.this,
                                    String.valueOf(spinner1.getSelectedItem()),
                                    Toast.LENGTH_SHORT).show();

                }
                else {
                    // Initialize  AsyncLogin()
                    semaine = spinner1.getSelectedItem().toString();
                    classe  = spinner2.getSelectedItem().toString();
                    cours  = spinner.getSelectedItem().toString();
                    new Accepter.AsyncLogin().execute(classe);

                }
            }

        });
        statisique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    // Initialize  AsyncLogin()
                    semaine = spinner1.getSelectedItem().toString();
                    classe  = spinner2.getSelectedItem().toString();
                    cours  = spinner.getSelectedItem().toString();
                    new Accepter.AsyncLogin1().execute(cours);


            }
        });
    }
    public static JSONArray  getJson() {
        return jsonarray;
    }
    public static String getSemaine(){
        return semaine;
    }
    public static String getClasse(){
        return classe;
    }
    public  static String getCours(){ return cours; }
    public static ArrayList getFeuilleabs() { return feuilleabs; }


    public void setJson(JSONArray jsonarray) {
        this.jsonarray = jsonarray;
    }
    private class CustomOnItemSelectedListener2  implements AdapterView.OnItemSelectedListener {
        private Spinner spinner;

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

            try {
                addItemsOnSpinnerCours(EnseignentCours(parent.getItemAtPosition(pos).toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void addItemsOnSpinnerCours(ArrayList<String> cours ) {

            spinner = (Spinner)findViewById(R.id.spinner);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Accepter.this,android.R.layout.simple_spinner_item, cours);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://172.20.10.2/ScriptPHP/student.inc.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id_classe", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                System.out.println(conn);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println(result.toString());

                    // Pass data to onPostExecute method
                    return(result.toString());


                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            JSONArray jsonarray = null;
            JSONObject   json = null;
            try {
                jsonarray = new JSONArray(result);
                json = jsonarray.getJSONObject(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(json !=null)
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                setJson(jsonarray);
                Intent intent = new Intent(Accepter.this,Fiche.class);
                startActivity(intent);

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(Accepter.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
    private class AsyncLogin1 extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://172.20.10.2/ScriptPHP/absence.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("cours", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                System.out.println(conn);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println(result.toString());

                    // Pass data to onPostExecute method
                    return(result.toString());


                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            JSONArray jsonarray = null;
            JSONObject json = null;
            try {
                jsonarray = new JSONArray(result);
                json = jsonarray.getJSONObject(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(json !=null)
            {
                String value;
                feuilleabs = new ArrayList<>();


                for (int i = 0;i<jsonarray.length();i++) {
                    try {
                        json = jsonarray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    value = json.toString();
                    value = value.substring(11, value.length() - 1);           //remove curly brackets
                    String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
                     abs = new HashMap<>();

                    for (String pair : keyValuePairs)                        //iterate over the pairs
                    {
                        String[] entry = pair.split("=");                   //split the pairs to get key and value
                        abs.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                    }
                    feuilleabs.add(abs);


                }


            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(Accepter.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
            else {
                feuilleabs = null;

            }

            Intent intent = new Intent(Accepter.this,Statistique.class);
            startActivity(intent);
        }

    }



    }





package com.example.projet.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.example.projet.R;
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

public class Fiche extends AppCompatActivity {

    private JSONArray ListEtudiant ;
    private ArrayList<CheckBox> CheckBoxes;
    private HashMap<String, Boolean> fiche;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private ArrayList <Etudiant> listeetud = new ArrayList<>();
    private String id_classe;
    private String id_fiche;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepter);
        ListEtudiant = Accepter.getJson();
       int sommet = 0;
        JSONObject etudiant;
        do {
            etudiant = null;

            try {
               etudiant = ListEtudiant.getJSONObject(sommet++);
               listeetud.add(new Etudiant(etudiant.optString("nom"),etudiant.optString("prenom"), Accepter.getClasse()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }while(etudiant != null);




        TableLayout table = (TableLayout) findViewById(R.id.idTable); // on prend le tableau défini dans le layout
        TableRow row;
        TextView tv1; // création des cellules
        CheckBox tv2;
        CheckBox tv3;
        CheckBoxes = new ArrayList<CheckBox>();
        fiche = new HashMap<String, Boolean>();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;




// pour chaque ligne
        for (int i = 0; i < sommet-1; i++) {
            row = new TableRow(this); // création d'une nouvelle ligne
            if (i % 2 == 0)
                row.setBackgroundColor(Color.parseColor("#577998"));
            tv1 = new TextView(this); // création cellule
            tv1.setText(listeetud.get(i).getNom().toUpperCase() + " " + listeetud.get(i).getPrenom().toUpperCase()); // ajout du texte
            tv1.setTextColor(Color.WHITE);
            tv1.setTextSize(16);
            tv1.setId(i);
            tv1.setTypeface(Typeface.DEFAULT_BOLD);
            tv1.setPadding(0, 0, 0, 10);
            tv1.setGravity(Gravity.LEFT); // centrage dans la cellule
            // adaptation de la largeur de colonne à l'écran :
            tv1.setLayoutParams(new TableRow.LayoutParams(width*2/3, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));


            // idem 2ème cellule
            tv2 = new CheckBox(this);
            tv2.setGravity(Gravity.LEFT);
            tv2.setId(i);
            tv2.setX(160);
            tv2.setPadding(0, 0, 0, 10);

            tv2.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            // idem 3ème cellule
            tv3 = new CheckBox(this);
            tv3.setGravity(Gravity.LEFT);
            tv3.setX(20);
            tv3.setId(i);
            tv3.setPadding(0, 0, 50, 10);
            tv3.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            CheckBoxes.add(tv2);
            CheckBoxes.add(tv3);
            final CheckBox finalTv = tv3;
            final CheckBox finalTv1 = tv2;
            tv2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (((CheckBox) view).isChecked())
                        finalTv.setChecked(false);
                        fiche.put(listeetud.get(finalTv1.getId()).getNom()+" "+listeetud.get(finalTv1.getId()).getPrenom(), true);
                }
            });

            final CheckBox finalTv2 = tv2;
            final CheckBox finalTv3 = tv3;
            tv3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (((CheckBox) view).isChecked()) {
                        finalTv2.setChecked(false);
                        fiche.put(listeetud.get(finalTv3.getId()).getNom()+" "+listeetud.get(finalTv3.getId()).getPrenom(), false);

                    }

                }


            });


            // ajout des cellules à la ligne
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);
            // ajout de la ligne au tableau
            table.addView(row);


        }
        row = new TableRow(this); // création d'une nouvelle ligne
        tv1 = new TextView(this); // création cellule
        tv1.setText(""); // ajout du texte
        tv1.setY(100);
        row.addView(tv1);
        table.addView(row);


        row = new TableRow(this); // création d'une nouvelle ligne
        tv1 = new TextView(this); // création cellule
        tv1.setText(""); // ajout du texte


        Button b1 = new Button(this);
        b1.setText("Validé");
        b1.setTextSize(18);
        b1.setTextColor(Color.parseColor("#365a7a"));
        b1.setBackgroundResource(R.drawable.image);
        b1.setGravity(Gravity.CENTER);
        b1.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Verifier()) {
                    Toast.makeText(Fiche.this, "Fiche bien enregistrer", Toast.LENGTH_LONG).show();
                    EnvoyeFiche();
                    new Fiche.AsyncLogin().execute(id_fiche, id_classe,Accepter.getCours(), fiche.toString());
                    Fiche.this.finish();
                }
                else {
                    Toast.makeText(Fiche.this, "Completer la fiche d'absence", Toast.LENGTH_LONG).show();
                }
            }
        });

        row.addView(tv1);
        row.addView(b1);
        table.addView(row);

        row = new TableRow(this); // création d'une nouvelle ligne
        tv1 = new TextView(this); // création cellule
        tv1.setText(""); // ajouté du texte
        tv1.setY(100);
        row.addView(tv1);
        table.addView(row);


    }

    public boolean Verifier() {
        int n = 0;
        for (int i = 0; i < CheckBoxes.size(); i++) {
            if (CheckBoxes.get(i).isChecked()) {
                n += 1;
            }
        }
        return n == listeetud.size();
    }

    public void EnvoyeFiche() {
        int index = 0;
        for (int i = 0; i < Accepter.getSemaine().length(); i++) {
            if (Accepter.getSemaine().charAt(i) == ':')
                index = i;
        }
         String id_semaine = Accepter.getSemaine().substring(0, index-1);
         id_classe = Accepter.getClasse();
         id_fiche = "F" + id_semaine.substring(1) +"-"+ id_classe +"-"+ Accepter.getCours();


    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Fiche.this);
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(true);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://172.20.10.2/ScriptPHP/fiche.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id_fiche", params[0])
                        .appendQueryParameter("id_classe", params[1])
                        .appendQueryParameter("cours", params[2])
                        .appendQueryParameter("fiche", params[3]);
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
                    return (result.toString());


                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }


    }
}
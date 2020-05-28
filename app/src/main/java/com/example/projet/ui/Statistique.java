package com.example.projet.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projet.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Statistique extends AppCompatActivity {
    private static int absence;
    private static int present;
    private static Spinner etudiant;
    private static Map<String, String> arrayname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);
        if(Accepter.getFeuilleabs()!=null) {
            arrayname = (Map<String, String>) Accepter.getFeuilleabs().get(0);
            addItemsOnSpinnerEtudiant();
            addListenerOnSpinnerItemSelection();
        }
        else{
            TextView numberOfAbs = findViewById(R.id.number_of_absence);
            numberOfAbs.setText("fiche n'existe pas");
            // Calculate the slice size and update the pie chart:
            ProgressBar pieChart = findViewById(R.id.nofil);
            pieChart.setProgress(100);

        }
    }

    public void addItemsOnSpinnerEtudiant() {
        etudiant = (Spinner) findViewById(R.id.etudiant);
        List<String> list = new ArrayList<String>();
        for(Map.Entry<String, String> entry : arrayname.entrySet()){
            list.add(entry.getKey());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etudiant.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {

        etudiant.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public  void updateChart(){
        // Update the text in a center of the chart:
        TextView numberOfAbs = findViewById(R.id.number_of_absence);
        numberOfAbs.setText(String.valueOf(absence) + " A / " + String.valueOf(present)+ " P ");
        ProgressBar pieChart1 = findViewById(R.id.nofil);

        pieChart1.setProgress(0);

        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        int somme = absence + present;
        double abs= ((double) absence * (double) 100) /(double) somme  ;
        double pre = 100 - abs;
        int progress = (int) (pre);
        pieChart.setProgress(progress);
    }
    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            absence = 0;
            present = 0;
            for(int i = 0;i<Accepter.getFeuilleabs().size();i++){
                arrayname = (Map<String, String>) Accepter.getFeuilleabs().get(i);
                if (arrayname.get(etudiant.getSelectedItem().toString()).equals("false")){
                    absence +=1;

                } else if (arrayname.get(etudiant.getSelectedItem().toString()).equals("true")) {
                    present+=1;
                }

            }
            updateChart();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }




    }

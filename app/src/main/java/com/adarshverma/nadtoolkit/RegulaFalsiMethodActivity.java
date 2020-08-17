package com.adarshverma.nadtoolkit;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class RegulaFalsiMethodActivity extends AppCompatActivity {


    public static String equation;
    MaterialToolbar appBar;
    String rfinitA;
    String rfinitB;
    String rferror;
    String rfresult;
    LinearLayout rflistHeaders;
    EditText rfequationEntryField;
    TextView rfrootTextView;
    EditText rfinitialAEntryField;
    EditText rfinitialBEntryField;
    EditText rferrorEntryField;
    ArrayList<Iteration> rfiterations = new ArrayList<>();
    boolean empty;
    ArrayAdapter<Iteration> rfiterationAdapter;

    //Graph Declarations
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    Switch graphSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regula_falsi_method);
        rfequationEntryField = (EditText) findViewById(R.id.rfequation_entry_field_id);
        rfinitialAEntryField = (EditText) findViewById(R.id.rfinitA);
        rfinitialBEntryField = (EditText) findViewById(R.id.rfinitB);
        rferrorEntryField = (EditText) findViewById(R.id.rferror);
        rfrootTextView = (TextView) findViewById(R.id.rfroot_text_view);
        rferrorEntryField.setText("0.0001");
        rflistHeaders = (LinearLayout) findViewById(R.id.List);
        rflistHeaders.setVisibility(View.INVISIBLE);
        rfrootTextView.setVisibility(View.INVISIBLE);

        //Graph Initializations-----------------
        graphSwitch = (Switch) findViewById(R.id.graphSwitch);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        graphSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rflistHeaders.setVisibility(View.GONE);
                    graph.setVisibility(View.VISIBLE);
                } else {

                    rflistHeaders.setVisibility(View.VISIBLE);
                    graph.setVisibility(View.INVISIBLE);
                }
            }
        });
        //Graph Initializations End--------------

        appBar = (MaterialToolbar) findViewById(R.id.appBar̥);
        setSupportActionBar(appBar);

    }


    double f(String equation, double x) {
        String valueString = Double.toString(x);
        equation = equation.replace("x", valueString);
        return Utils.eval(equation);
    }

    void regulaFalsiMethod(String equation, double a, double b, double e) {

        ///Graph
        series = new LineGraphSeries<DataPoint>();


        rflistHeaders.setVisibility(View.VISIBLE);
        rfrootTextView.setVisibility(View.VISIBLE);

        if (f(equation, a) * f(equation, b) >= 0) {
            Toast.makeText(this, "Initial Values doesn't satisfies the equation, try different values.", Toast.LENGTH_LONG).show();
            rflistHeaders.setVisibility(View.INVISIBLE);
            rfrootTextView.setVisibility(View.INVISIBLE);
            return;
        }

        double c = a;
        int i = 0;
        while (Math.abs(f(equation, c)) > e) {
            c = ((a * f(equation, b)) - (b * f(equation, a))) / (f(equation, b) - f(equation, a));

            rfiterations.add(new Iteration(i + 1, a, b, c, f(equation, c)));
            //Graph
            series.appendData(new DataPoint(i, f(equation, i)), true, 500);
            if (f(equation, c) == 0.0)
                break;

            else if (f(equation, c) * f(equation, a) < 0)
                b = c;
            else
                a = c;
            i++;
        }

        rfiterationAdapter = new IterationAdapter(this, rfiterations, true);
        ListView listView = (ListView) findViewById(R.id.rfi_list);
        listView.setAdapter(rfiterationAdapter);
        String root = Double.toString(c);
        rfresult = "Root: " + root;
        rfrootTextView.setText(rfresult);


        //Graph
        //graph.getViewport().setScalable(true);
        graph.addSeries(series);

        closeKeyboard();


    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void calculateRegulaFalsi(View view) {


        checkEmptyFields();
        rfiterations.clear();
        //Graph
        graphSwitch.setChecked(false);
        graph.removeAllSeries();

        rflistHeaders.setVisibility(View.INVISIBLE);
        rfrootTextView.setVisibility(View.INVISIBLE);

        if (!empty) {
            try {
                equation = rfequationEntryField.getText().toString();
                rfinitA = rfinitialAEntryField.getText().toString();
                rfinitB = rfinitialBEntryField.getText().toString();
                rferror = rferrorEntryField.getText().toString();
                double a = Double.parseDouble(rfinitA);
                double b = Double.parseDouble(rfinitB);
                double e = Double.parseDouble(rferror);

                regulaFalsiMethod(equation, a, b, e);
            } catch (Exception e) {
                Toast.makeText(this, "Please check your inputs.\nNote:Equation should be in x", Toast.LENGTH_SHORT).show();
                rflistHeaders.setVisibility(View.INVISIBLE);
                rfrootTextView.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void checkEmptyFields() {
        if (TextUtils.isEmpty(rfequationEntryField.getText())) {
            Toast.makeText(this, "Equation Field Empty!!", Toast.LENGTH_SHORT).show();
            rflistHeaders.setVisibility(View.INVISIBLE);
            rfrootTextView.setVisibility(View.INVISIBLE);
            empty = true;

        } else if (TextUtils.isEmpty(rfinitialAEntryField.getText())) {
            Toast.makeText(this, "Please input initial value of a.", Toast.LENGTH_SHORT).show();
            rflistHeaders.setVisibility(View.INVISIBLE);
            rfrootTextView.setVisibility(View.INVISIBLE);
            empty = true;
        } else if (TextUtils.isEmpty(rfinitialBEntryField.getText())) {
            Toast.makeText(this, "Please input initial value of b.", Toast.LENGTH_SHORT).show();
            rflistHeaders.setVisibility(View.INVISIBLE);
            rfrootTextView.setVisibility(View.INVISIBLE);
            empty = true;
        } else if (TextUtils.isEmpty(rferrorEntryField.getText())) {
            Toast.makeText(this, "Please set accuracy.", Toast.LENGTH_SHORT).show();
            rflistHeaders.setVisibility(View.INVISIBLE);
            rfrootTextView.setVisibility(View.INVISIBLE);

            empty = true;
        } else {
            empty = false;
        }

    }


    //App Bar---------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }


    public void openHelpDialog() {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.show(getSupportFragmentManager(), "help Dialog");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                openHelpDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
//App  Bar----------------------------------------------------------------------------------------


}

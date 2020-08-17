package com.adarshverma.nadtoolkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.appbar.MaterialToolbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class NewtonRaphsonMethodActivity extends AppCompatActivity {

    public static String equation;
    public static String derivative;
    MaterialToolbar appBar;
    String nRInitX;
    String nRError;
    String nRResult;
    LinearLayout nRListHeaders;
    EditText nREquationEntryField;
    EditText nRDerivativeEntryField;
    TextView nRRootTextView;
    EditText nRInitialXEntryField;
    EditText nRErrorEntryField;
    ArrayList<Iteration> nRIterations = new ArrayList<>();
    boolean empty;
    ArrayAdapter<Iteration> nRIterationAdapter;


    //Graph Declarations
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    Switch graphSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_raphson_method);
        nREquationEntryField = (EditText) findViewById(R.id.equation_entry_field_id);
        nRDerivativeEntryField = (EditText) findViewById(R.id.derivative_entry_field_id);
        nRInitialXEntryField = (EditText) findViewById(R.id.initX);
        nRErrorEntryField = (EditText) findViewById(R.id.error);
        nRRootTextView = (TextView) findViewById(R.id.root_text_view);
        nRErrorEntryField.setText("0.001");
        nRListHeaders = (LinearLayout) findViewById(R.id.List);
        nRListHeaders.setVisibility(View.INVISIBLE);
        nRRootTextView.setVisibility(View.INVISIBLE);

        //Graph Initializations-----------------
        graphSwitch = (Switch) findViewById(R.id.graphSwitch);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        graphSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nRListHeaders.setVisibility(View.GONE);
                    graph.setVisibility(View.VISIBLE);
                } else {

                    nRListHeaders.setVisibility(View.VISIBLE);
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

    void NewtonRaphsonMethod(String equation, String derivative, double x0, double e) {
        ///Graph
        series = new LineGraphSeries<DataPoint>();


        nRListHeaders.setVisibility(View.VISIBLE);
        nRRootTextView.setVisibility(View.VISIBLE);


        int i = 0;
        while (Math.abs((f(equation, x0))) >= e) {

            x0 = x0 - (f(equation, x0) / f(derivative, x0));

            nRIterations.add(new Iteration(i + 1, x0, f(equation, x0)));//x1=xi  x0=x(i+1)
            //Graph
            series.appendData(new DataPoint(i, f(equation, i)), true, 500);
            i++;
        }

        nRIterationAdapter = new IterationAdapter(this, nRIterations, false);
        ListView listView = (ListView) findViewById(R.id.a_list);
        listView.setAdapter(nRIterationAdapter);
        String root = Double.toString(x0);
        nRResult = "Root: " + root;
        nRRootTextView.setText(nRResult);

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

    public void calculateNewtonRaphson(View view) {


        checkEmptyFields();
        nRIterations.clear();
        //Graph
        graphSwitch.setChecked(false);
        graph.removeAllSeries();

        nRListHeaders.setVisibility(View.INVISIBLE);
        nRRootTextView.setVisibility(View.INVISIBLE);

        if (!empty) {
            try {
                equation = nREquationEntryField.getText().toString();
                derivative = nRDerivativeEntryField.getText().toString();
                nRInitX = nRInitialXEntryField.getText().toString();
                nRError = nRErrorEntryField.getText().toString();
                double x0 = Double.parseDouble(nRInitX);
                double e = Double.parseDouble(nRError);
                NewtonRaphsonMethod(equation, derivative, x0, e);
            } catch (Exception e) {
                Toast.makeText(this, "Please check your inputs.\n\nNote:Equations should be in x", Toast.LENGTH_SHORT).show();
                nRListHeaders.setVisibility(View.INVISIBLE);
                nRRootTextView.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void checkEmptyFields() {
        if (TextUtils.isEmpty(nREquationEntryField.getText())) {
            Toast.makeText(this, "Equation Field Empty!!", Toast.LENGTH_SHORT).show();
            nRListHeaders.setVisibility(View.INVISIBLE);
            nRRootTextView.setVisibility(View.INVISIBLE);
            empty = true;

        } else if (TextUtils.isEmpty(nRDerivativeEntryField.getText())) {
            Toast.makeText(this, "Derivative Field Empty!!", Toast.LENGTH_SHORT).show();
            nRListHeaders.setVisibility(View.INVISIBLE);
            nRRootTextView.setVisibility(View.INVISIBLE);
            empty = true;

        } else if (TextUtils.isEmpty(nRInitialXEntryField.getText())) {
            Toast.makeText(this, "Please input initial value of Xo", Toast.LENGTH_SHORT).show();
            nRListHeaders.setVisibility(View.INVISIBLE);
            nRRootTextView.setVisibility(View.INVISIBLE);
            empty = true;
        } else if (TextUtils.isEmpty(nRErrorEntryField.getText())) {
            Toast.makeText(this, "Please set accuracy.", Toast.LENGTH_SHORT).show();
            nRListHeaders.setVisibility(View.INVISIBLE);
            nRRootTextView.setVisibility(View.INVISIBLE);
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

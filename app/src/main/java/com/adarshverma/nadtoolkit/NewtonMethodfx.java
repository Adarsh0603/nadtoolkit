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

public class NewtonMethodfx extends AppCompatActivity {

    public static String equation;
    MaterialToolbar appBar;
    String nInitX;
    String nError;
    String nResult;
    LinearLayout nListHeaders;
    EditText nEquationEntryField;
    TextView nRootTextView;
    EditText nInitialXEntryField;
    EditText nErrorEntryField;
    ArrayList<Iteration> nIterations = new ArrayList<>();
    boolean empty;
    ArrayAdapter<Iteration> nIterationAdapter;

    //Graph Declarations
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    Switch graphSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_methodfx);
        nEquationEntryField = (EditText) findViewById(R.id.equation_entry_field_id);
        nInitialXEntryField = (EditText) findViewById(R.id.initX);
        nErrorEntryField = (EditText) findViewById(R.id.error);
        nRootTextView = (TextView) findViewById(R.id.root_text_view);
        nErrorEntryField.setText("0.001");
        nListHeaders = (LinearLayout) findViewById(R.id.List);
        nListHeaders.setVisibility(View.INVISIBLE);
        nRootTextView.setVisibility(View.INVISIBLE);


        //Graph Initializations-----------------
        graphSwitch = (Switch) findViewById(R.id.graphSwitch);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        graphSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nListHeaders.setVisibility(View.GONE);
                    graph.setVisibility(View.VISIBLE);
                } else {

                    nListHeaders.setVisibility(View.VISIBLE);
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

    void NewtonMethod(String equation, double x0, double e) {
        ///Graph
        series = new LineGraphSeries<DataPoint>();


        nListHeaders.setVisibility(View.VISIBLE);
        nRootTextView.setVisibility(View.VISIBLE);


        int i = 0;
        while (Math.abs(f(equation, x0)) >= e) {

            x0 = x0 - ((f(equation, x0) * f(equation, x0)) / (f(equation, x0 + f(equation, x0)) - f(equation, x0)));

            nIterations.add(new Iteration(i + 1, x0, f(equation, x0)));//x1=xi  x0=x(i+1)
            //Graph
            series.appendData(new DataPoint(i, f(equation, i)), true, 500);
            i++;
        }

        nIterationAdapter = new IterationAdapter(this, nIterations, false);
        ListView listView = (ListView) findViewById(R.id.a_list);
        listView.setAdapter(nIterationAdapter);
        String root = Double.toString(x0);
        nResult = "Root: " + root;
        nRootTextView.setText(nResult);

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

    public void calculateNewtonMethod(View view) {


        checkEmptyFields();
        nIterations.clear();

        //Graph
        graphSwitch.setChecked(false);
        graph.removeAllSeries();

        nListHeaders.setVisibility(View.INVISIBLE);
        nRootTextView.setVisibility(View.INVISIBLE);

        if (!empty) {
            try {
                equation = nEquationEntryField.getText().toString();
                nInitX = nInitialXEntryField.getText().toString();
                nError = nErrorEntryField.getText().toString();
                double x0 = Double.parseDouble(nInitX);
                double e = Double.parseDouble(nError);
                NewtonMethod(equation, x0, e);
            } catch (Exception e) {
                Toast.makeText(this, "Please check your inputs.\n\nNote:Equations should be in x", Toast.LENGTH_SHORT).show();
                nListHeaders.setVisibility(View.INVISIBLE);
                nRootTextView.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void checkEmptyFields() {
        if (TextUtils.isEmpty(nEquationEntryField.getText())) {
            Toast.makeText(this, "Equation Field Empty!!", Toast.LENGTH_SHORT).show();
            nListHeaders.setVisibility(View.INVISIBLE);
            nRootTextView.setVisibility(View.INVISIBLE);
            empty = true;

        } else if (TextUtils.isEmpty(nInitialXEntryField.getText())) {
            Toast.makeText(this, "Please input initial value of Xo", Toast.LENGTH_SHORT).show();
            nListHeaders.setVisibility(View.INVISIBLE);
            nRootTextView.setVisibility(View.INVISIBLE);
            empty = true;
        } else if (TextUtils.isEmpty(nErrorEntryField.getText())) {
            Toast.makeText(this, "Please set accuracy.", Toast.LENGTH_SHORT).show();
            nListHeaders.setVisibility(View.INVISIBLE);
            nRootTextView.setVisibility(View.INVISIBLE);
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

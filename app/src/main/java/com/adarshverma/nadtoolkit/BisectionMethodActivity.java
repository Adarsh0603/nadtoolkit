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

public class BisectionMethodActivity extends AppCompatActivity {

    public static String equation;
    MaterialToolbar appBar;
    String initA;
    String initB;
    String error;
    String result;
    LinearLayout listHeaders;
    EditText equationEntryField;
    TextView rootTextView;
    EditText initialAEntryField;
    EditText initialBEntryField;
    EditText errorEntryField;
    boolean empty;

    ArrayList<Iteration> iterations = new ArrayList<>();
    ArrayAdapter<Iteration> iterationAdapter;
    //Graph Declarations
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    Switch graphSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bisection_method);


        equationEntryField = (EditText) findViewById(R.id.equation_entry_field_id);
        initialAEntryField = (EditText) findViewById(R.id.initA);
        initialBEntryField = (EditText) findViewById(R.id.initB);
        errorEntryField = (EditText) findViewById(R.id.error);
        rootTextView = (TextView) findViewById(R.id.root_text_view);
        errorEntryField.setText("0.001");
        listHeaders = (LinearLayout) findViewById(R.id.List);
        listHeaders.setVisibility(View.INVISIBLE);
        rootTextView.setVisibility(View.INVISIBLE);

        //Graph Initializations-----------------
        graphSwitch = (Switch) findViewById(R.id.graphSwitch);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        graphSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listHeaders.setVisibility(View.GONE);
                    graph.setVisibility(View.VISIBLE);
                } else {

                    listHeaders.setVisibility(View.VISIBLE);
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

    void bisectionMethod(String equation, double a, double b, double e) {

        ///Graph
        series = new LineGraphSeries<DataPoint>();


        listHeaders.setVisibility(View.VISIBLE);
        rootTextView.setVisibility(View.VISIBLE);


        if (f(equation, a) * f(equation, b) >= 0) {
            Toast.makeText(this, "Initial Values doesn't satisfies the equation, try different values.", Toast.LENGTH_LONG).show();
            listHeaders.setVisibility(View.INVISIBLE);
            rootTextView.setVisibility(View.INVISIBLE);
            return;
        }

        double mid = (b + a) / 2;
        int i = 0;
        while (Math.abs((b - a)) >= e) {
            mid = (a + b) / 2;
            iterations.add(new Iteration(i + 1, a, b, mid, f(equation, mid)));
            //Graph
            series.appendData(new DataPoint(i, f(equation, i)), true, 500);
            if (f(equation, mid) == 0.0)
                break;

            else if (f(equation, mid) * f(equation, a) < 0)
                b = mid;
            else
                a = mid;
            i++;
        }

        iterationAdapter = new IterationAdapter(this, iterations, true);
        ListView listView = (ListView) findViewById(R.id.a_list);
        listView.setAdapter(iterationAdapter);
        String root = Double.toString(mid);
        result = "Root: " + root;
        rootTextView.setText(result);

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

    public void calculate(View view) {

        checkEmptyFields();
        iterations.clear();
        //Graph
        graphSwitch.setChecked(false);
        graph.removeAllSeries();


        listHeaders.setVisibility(View.INVISIBLE);
        rootTextView.setVisibility(View.INVISIBLE);

        if (!empty) {
            try {
                equation = equationEntryField.getText().toString();
                initA = initialAEntryField.getText().toString();
                initB = initialBEntryField.getText().toString();
                error = errorEntryField.getText().toString();
                double a = Double.parseDouble(initA);
                double b = Double.parseDouble(initB);
                double e = Double.parseDouble(error);

                bisectionMethod(equation, a, b, e);
            } catch (Exception e) {
                Toast.makeText(this, "Please check your inputs.\nNote:Equation should be in x", Toast.LENGTH_SHORT).show();
                listHeaders.setVisibility(View.INVISIBLE);
                rootTextView.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void checkEmptyFields() {
        if (TextUtils.isEmpty(equationEntryField.getText())) {
            Toast.makeText(this, "Equation Field Empty!!", Toast.LENGTH_SHORT).show();
            listHeaders.setVisibility(View.INVISIBLE);
            rootTextView.setVisibility(View.INVISIBLE);
            empty = true;

        } else if (TextUtils.isEmpty(initialAEntryField.getText())) {
            Toast.makeText(this, "Please input initial value of a.", Toast.LENGTH_SHORT).show();
            listHeaders.setVisibility(View.INVISIBLE);
            rootTextView.setVisibility(View.INVISIBLE);
            empty = true;
        } else if (TextUtils.isEmpty(initialBEntryField.getText())) {
            Toast.makeText(this, "Please input initial value of b.", Toast.LENGTH_SHORT).show();
            listHeaders.setVisibility(View.INVISIBLE);
            rootTextView.setVisibility(View.INVISIBLE);
            empty = true;
        } else if (TextUtils.isEmpty(errorEntryField.getText())) {
            Toast.makeText(this, "Please set accuracy.", Toast.LENGTH_SHORT).show();
            listHeaders.setVisibility(View.INVISIBLE);
            rootTextView.setVisibility(View.INVISIBLE);

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

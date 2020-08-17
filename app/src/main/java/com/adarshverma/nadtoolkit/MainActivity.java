package com.adarshverma.nadtoolkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBar = (MaterialToolbar) findViewById(R.id.appBar̥);
        setSupportActionBar(appBar);
    }


    public void openBM(View view) {

        Intent intent = new Intent(this, BisectionMethodActivity.class);
        startActivity(intent);

    }

    public void openRF(View view) {
        Intent intent = new Intent(this, RegulaFalsiMethodActivity.class);
        startActivity(intent);

    }

    public void openNR(View view) {
        Intent intent = new Intent(this, NewtonRaphsonMethodActivity.class);
        startActivity(intent);

    }

    public void openNM(View view) {
        Intent intent = new Intent(this, NewtonMethodfx.class);
        startActivity(intent);

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

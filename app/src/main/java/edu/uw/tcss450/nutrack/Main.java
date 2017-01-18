package edu.uw.tcss450.nutrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar titleBar = (Toolbar) findViewById(R.id.titleBar);
        setSupportActionBar(titleBar);

    }

}

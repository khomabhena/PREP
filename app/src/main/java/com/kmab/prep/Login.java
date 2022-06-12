package com.kmab.prep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
    }

    public void adminLogin(View view) {
        EditText etCode = findViewById(R.id.etCode);
        String code = etCode.getText().toString();

        if (!code.trim().equals("") && code.length() == 6) {
            if (code.equals("KM18PR")) {
                editor = prefs.edit();
                editor.putBoolean(AppInfo.BOOL_ADMIN, true);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Admin Privileges Granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Messages.class));
                finish();
            }
        }
    }
}

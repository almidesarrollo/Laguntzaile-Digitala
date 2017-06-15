package com.asistencia.digital.monitor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.asistencia.digital.monitor.R;
import com.asistencia.digital.monitor.localdb.LocalDbManager;
import com.asistencia.digital.monitor.webservice.AsistRestApiClient;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* check first execution of the app */
        if (!LocalDbManager.isFirstExecution(this)) {
            this.startMain();
            return;
        }

        final AsistRestApiClient apiRestClient = new AsistRestApiClient(this);

        /* listener for sign in click */
        this.findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiRestClient.doLoginGUI(R.id.editUser, R.id.editPassword, LoginActivity.this);
            }
        });
    }

    private void startMain() {
        startActivity(new Intent(this, MainGeo.class));
        finish();
    }

    public void onLoginSuccess() {
        Toast.makeText(this, "Sign in Correct!, You can continue...", Toast.LENGTH_LONG).show();
        this.startMain();
    }

    public void onLoginFailed(String message, boolean fatal) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (fatal)
            finish();
    }
}

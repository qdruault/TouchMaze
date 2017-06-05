package fr.UTC_CosTech_CRED.SimpleTouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;
import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogApp;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 11;

    @Bind(R.id.login_login)
    EditText loginEditText;

    @Bind(R.id.login_password)
    EditText passwordEditText;

    @Bind(R.id.login_button)
    Button loginButton;

    @Bind(R.id.login_signup)
    TextView signupTextView;

    DialogApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        super.onCreate(savedInstanceState);

        app = (DialogApp)getApplicationContext();
        app.stopIdleTimer();

        // On regarde si on est déjà connecté.
        if (app.getConn() != null && app.getConn().isConnected()) {
            onLoginSuccess();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // On préremplit le champ login.
        String oldLogin = app.getOldLogin();
        if (oldLogin != null) {
            loginEditText.setText(oldLogin);
            passwordEditText.requestFocus();
        }
        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            // Mise à jour du login.
            @Override
            public void afterTextChanged(Editable s) {
                app.saveOldLogin(s.toString());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    /**
     * Connexion réussie.
     */
    public void onLoginSuccess() {
        app.resetIdleTimer("LoginActivity");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Echec de connexion.
     */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    /**
     * Connexion.
     */
    private void login() {
        // Si les champs ne sont pas remplis.
        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        // Spinner.
        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.authenticating));
        progressDialog.show();

        // On récupère les identifiants.
        final String email = loginEditText.getText().toString();
        final String password = passwordEditText.getText().toString();


        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // On se connecte.
                    app.connect(email, password);
                    onLoginSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onLoginFailed();
                        }
                    });
                } finally {
                    progressDialog.dismiss();
                }
            }
        };

        thread.start();
    }

    /**
     * Renvoie true si les champs login et password sont correctement remplis.
     * @return
     */
    private boolean validate() {
        boolean valid = true;

        String email = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
            loginEditText.setError(getResources().getString(R.string.login_invalid));
            valid = false;
        } else {
            loginEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3) {
            passwordEditText.setError(getResources().getString(R.string.minimum_3_alphanumeric_characters));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}

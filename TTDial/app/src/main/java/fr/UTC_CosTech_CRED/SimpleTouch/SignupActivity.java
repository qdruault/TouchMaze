package fr.UTC_CosTech_CRED.SimpleTouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogApp;

public class SignupActivity extends AppCompatActivity {

    private DialogApp app;

    @Bind(R.id.register_username)
    EditText usernameEditText;

    @Bind(R.id.register_password)
    EditText passwordEditText;

    @Bind(R.id.register_repeat_password)
    EditText repeatPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (DialogApp) getApplication();

        if (app.getConn() != null && app.getConn().isConnected()) {
            onLoginSuccess();
        }

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        app = (DialogApp) getApplication();
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void onRegister(View v) {
        if (validate()) {
            final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.registering));
            progressDialog.show();

            new Thread() {
                @Override
                public void run() {
                    try {
                        AbstractXMPPConnection connection = app.connect();

                        String username = usernameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        Map<String, String> attributes = new HashMap<String, String>();
                        attributes.put("name", username);
                        attributes.put("email", username + '@' + DialogApp.HOST);

                        AccountManager am = AccountManager.getInstance(connection);
                        am.sensitiveOperationOverInsecureConnection(true);
                        boolean can = am.supportsAccountCreation();
                        Set<String> attr = am.getAccountAttributes();
                        am.createAccount(username, password, attributes);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage(getResources().getString(R.string.authenticating));
                            }
                        });

                        app.connect(username, password);

                        DialogApp app = (DialogApp)getApplicationContext();
                        app.resetIdleTimer("SignupActivity");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this, R.string.registered_and_authenticated_successfully, Toast.LENGTH_LONG).show();
                                onLoginSuccess();
                            }
                        });

                    } catch (final XMPPException.XMPPErrorException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (e.getXMPPError().getCondition().name().equals("conflict")) {
                                    usernameEditText.setError(getResources().getString(R.string.this_name_is_already_in_use));
                                }
                                Toast.makeText(SignupActivity.this, R.string.unexpected_error_occurred, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignupActivity.this, R.string.unexpected_error_occurred, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }.start();
        }
    }

    public boolean validate() {
        boolean valid = true;

        if (usernameEditText.getText().toString().isEmpty()) {
            usernameEditText.setError(getResources().getString(R.string.login_invalid));
            valid = false;
        } else {
            usernameEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();

        if (password.isEmpty() || password.length() < 3) {
            passwordEditText.setError(getResources().getString(R.string.minimum_3_alphanumeric_characters));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        if (!repeatPassword.equals(password)) {
            repeatPasswordEditText.setError(getResources().getString(R.string.passwords_are_not_equals));
            valid = false;
        } else {
            repeatPasswordEditText.setError(null);
        }

        return valid;
    }
}

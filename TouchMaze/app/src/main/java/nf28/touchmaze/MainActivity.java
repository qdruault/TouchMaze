package nf28.touchmaze;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nf28.touchmaze.login.DialogHandler;


public class MainActivity extends AppCompatActivity {

    EditText loginText;
    EditText passwordText;
    Button loginButton;
    DialogHandler dialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Classe gérant la connexion.
        dialogHandler = new DialogHandler();

        // On regarde si on est déjà connecté.
        if (dialogHandler.getConn() != null && dialogHandler.getConn().isConnected()) {
            onLoginSuccess();
        }

        // Binding.
        loginText = (EditText)findViewById(R.id.text_login);
        passwordText = (EditText)findViewById(R.id.text_password);
        loginButton = (Button)findViewById(R.id.btn_login);

        // Listener.
        loginButton.setOnClickListener(addListenerLoginButton());
    }

    /**
     * Création du listener du bouton de connexion.
     * @return
     */
    private OnClickListener addListenerLoginButton() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // On vérifie que les champs sont remplis.
                if (checkFields()) {
                    tryLogin();
                }
            }
        };

        return listener;
    }

    /**
     * Connexion réussie.
     */
    private void onLoginSuccess() {
        Log.d("connexion", "Connexion réussie ! :D");
    }

    /**
     * Echec de connexion.
     */
    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Echec de la connexion", Toast.LENGTH_LONG).show();
    }

    /**
     * Vérifie sue les champs login/password sont bien remplis.
     * @return
     */
    private boolean checkFields() {
        // Pas de login.
        if (loginText.getText().toString().isEmpty()) {
            loginText.setError("Login invalide");
            return false;
        } else {
            loginText.setError(null);
        }

        // Pas de mot de passe.
        if (passwordText.getText().toString().isEmpty()) {
            passwordText.setError("Mot de passe invalide");
            return false;
        } else {
            passwordText.setError(null);
        }

        // Tout va bien.
        return true;
    }

    /**
     * On se connecte.
     */
    private void tryLogin() {
        // Spinner.
        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connexion en cours ...");
        progressDialog.show();

        // On récupère les identifiants.
        final String login = loginText.getText().toString();
        final String password = passwordText.getText().toString();

        // On crée et lance le thread de la connexion.
        final Thread thread = createThreadConnection(login, password, progressDialog);
        thread.start();
    }

    /**
     * Crée le thread gérant la connexion.
     * @param login
     * @param password
     * @param progressDialog : le spinner
     * @return
     */
    private Thread createThreadConnection(final String login, final String password, final ProgressDialog progressDialog) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // On se connecte.
                    dialogHandler.connect(login, password);
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
                    // On arrête le spinner.
                    progressDialog.dismiss();
                }
            }
        };

        return thread;
    }
}

package nf28.touchmaze;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText loginText;
    EditText passwordText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if (loginText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()) {
                    onLoginFailed();
                } else {
                    onLoginSuccess();
                }
            }
        };

        return listener;
    }

    /**
     * Connexion réussie.
     */
    private void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "Connexion réussie ! :D", Toast.LENGTH_LONG).show();
    }

    /**
     * Echec de connexion.
     */
    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login ou mot de passe incorrect :(", Toast.LENGTH_LONG).show();
    }
}

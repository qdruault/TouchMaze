package fr.UTC_CosTech_CRED.SimpleTouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogApp;

public class DialogActivity extends AppCompatActivity implements ChatMessageListener {

    private DialogApp app;
    // Connexion.
    private AbstractXMPPConnection conn;
    // Gestionnaire de tchat.
    private ChatManager chatManager;
    // Le tchat.
    private Chat chat;
    // Les messages échangés.
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;

    @Bind(R.id.text_new_message) EditText _newMessageText;
    @Bind(R.id.list_messages) ListView _listMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);

        messages = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
        _listMessages.setAdapter(adapter);

        // Récupère la connexion.
        app = (DialogApp)getApplicationContext();
        conn = app.getConn();

        // Crée le tchat pour cette connexion.
        chatManager = ChatManager.getInstanceFor(conn);
        Intent i = getIntent();
        chat = chatManager.createChat(i.getStringExtra("PARTNER"), this);

        // Listener sur la création d'un nouveau tchat.
        chatManager.addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally)
                    {
                        if (!createdLocally)
                            chat.addMessageListener(DialogActivity.this);
                    }
                });
    }

    /**
     * Envoie un message sur le tchat.
     * @param view
     */
    public void sendMessage(View view) {

        // Récupère le message.
        String message = _newMessageText.getText().toString();
        try {
            // Enioe le message.
            chat.sendMessage(message);
            // On ajoute le message à la liste (historique).
            messages.add(0, message);
            adapter.notifyDataSetChanged();
            // On vide le champ texte.
            _newMessageText.setText("");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Réception d'un message ?
     * @param chat
     * @param message
     */
    public void processMessage(Chat chat, final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message.getBodies().size() > 0) {
                    // On ajoute le message à la liste.
                    messages.add(0, message.getBody());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onUserInteraction() {
        app.resetIdleTimer("DialogActivity");
    }
}

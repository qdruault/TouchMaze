package nf28.touchmaze.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import nf28.touchmaze.login.DialogHandler;

public class ChatActivity extends AppCompatActivity implements ChatManagerListener, ChatMessageListener {

    // Système de chat.
    protected ChatManager chatManager;
    protected Chat chatOut;
    protected Chat chatIn;
    // Message de fin de dialogue.
    protected static final String END_DIALOG_MESSAGE = "END_DIALOG_MESSAGE";
    // Connexion.
    protected DialogHandler dialogHandler;
    protected AbstractXMPPConnection conn;
    // Nom du partenaire.
    protected String partnerJID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On récupère la connexion.
        dialogHandler = (DialogHandler)getApplicationContext();
        conn = dialogHandler.getConn();

        // On récupère le nom du partenaire.
        Intent i = getIntent();
        partnerJID = i.getStringExtra("PARTNER");

        // Création du chat.
        chatManager = ChatManager.getInstanceFor(conn);
        chatOut = chatManager.createChat(partnerJID + "/Smack");
        chatManager.addChatListener(this);
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally)
    {
        if (!createdLocally) {
            chatIn = chat;
            chatIn.addMessageListener(this);
        }
    }

    /**
     * Réception d'un message du partenaire.
     * @param chat
     * @param message
     */
    @Override
    public void processMessage(Chat chat, final Message message) {

    }


}

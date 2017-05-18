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
    private AbstractXMPPConnection conn;
    private ChatManager chatManager;
    private Chat chat;
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


        app = (DialogApp)getApplicationContext();
        conn = app.getConn();

        chatManager = ChatManager.getInstanceFor(conn);
        Intent i = getIntent();
        chat = chatManager.createChat(i.getStringExtra("PARTNER"), this);

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

    public void sendMessage(View view) {

        String message = _newMessageText.getText().toString();
        try {
            chat.sendMessage(message);
            messages.add(0, message);
            adapter.notifyDataSetChanged();
            _newMessageText.setText("");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void processMessage(Chat chat, final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message.getBodies().size() > 0) {
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

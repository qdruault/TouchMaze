package fr.UTC_CosTech_CRED.SimpleTouch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogInvitationHandlerListener;
import fr.UTC_CosTech_CRED.SimpleTouch.util.touch.DialogTouchEvent;
import fr.UTC_CosTech_CRED.SimpleTouch.util.touch.TactileDialogViewHolder;
import fr.UTC_CosTech_CRED.SimpleTouch.layout.SurfaceLayout;
import fr.UTC_CosTech_CRED.SimpleTouch.util.DialogApp;

public class TactileDialogActivity extends AppCompatActivity implements TactileDialogViewHolder, ChatManagerListener, ChatMessageListener, DialogInvitationHandlerListener {

    public static final String END_DIALOG_MESSAGE = "END_DIALOG_MESSAGE";

    public DialogApp app;

    @Bind(R.id.tactile_area)
    SurfaceLayout tactileArea;

    @Bind(R.id.dialog_partner_state)
    TextView dialogPartnerStateView;

    @Bind(R.id.dialog_invitations)
    TextView dialogInvitationsView;

    private AbstractXMPPConnection conn;
    private ChatManager chatManager;
    private Chat chatOut;
    private Chat chatIn;

    private AlertDialog endDialog;

    private InvitationResultHandler invitationResultHandler;
    private String partnerJID;
    private boolean partnerConnected = false;
    private String lastMessage = "Newer sent before";

    private OPTIONS options;
    private Runnable updateTouchesRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tactile_dialog);
        ButterKnife.bind(this);

        app = (DialogApp) getApplication();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        options = app.GetOptions();

        tactileArea.setDialogViewHolder(this);
        tactileArea.setWillNotDraw(false);

        Intent i = getIntent();
        partnerJID = i.getStringExtra("PARTNER");
        conn = app.getConn();
        chatManager = ChatManager.getInstanceFor(conn);
        chatOut = chatManager.createChat(partnerJID + "/Smack");
        chatManager.addChatListener(this);

        if (app.hasInvited(partnerJID)) {
            // accept invitation
            app.acceptInvitation(partnerJID);
            setPartnerConnected(true);
        } else {
            // send invitation
            //IQ invitation = new IQ("invitation", "http://binova-it.fr/tactile-dialog/invitation") {
            IQ invitation = new IQ("invitation", "www.intertact.net/invitation") {
                @Override
                protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
                    xml.setEmptyElement();
                    return xml;
                }
            };
            invitation.setTo(partnerJID + "/Smack");
            invitation.setStanzaId("invitation" + UUID.randomUUID().toString());

            invitationResultHandler = new InvitationResultHandler();
            try {
                conn.sendIqWithResponseCallback(invitation, invitationResultHandler);
            } catch (SmackException.NotConnectedException e) {
            }
        }

        app.sendOccupiedPresence();
        updateTouchesRunnable = new Runnable() {
            @Override
            public void run() {
                tactileArea.invalidate();
            }
        };
        app.getDialogInvitationHandler().addListener(this);

    }

    private void setPartnerConnected(boolean partnerConnected) {
        this.partnerConnected = partnerConnected;
        dialogPartnerStateView.setText(partnerConnected ? R.string.your_partner_is_here : R.string.waiting_for_your_partner);
        tactileArea.invalidate();
    }

    @Override
    public OPTIONS GetOptions(){return options;}

    @Override
    public void onDialogTouch(DialogTouchEvent event) {
        // send to partner
        String message = event.makeMessage();
        if (!lastMessage.equals(message)) {
            lastMessage = message;

            try {
                chatOut.sendMessage(message);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally)
    {
        if (!createdLocally) {
            chatIn = chat;
            chatIn.addMessageListener(this);
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        if (message.getFrom().equals(partnerJID + "/Smack")) {
            final String messageBody = message.getBody();

            if (END_DIALOG_MESSAGE.equals(messageBody)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endDialog = new AlertDialog.Builder(TactileDialogActivity.this)
                                .setTitle(R.string.end_dialog_title)
                                .setMessage(R.string.end_dialog_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TactileDialogActivity.this.finish();
                                    }
                                })
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        TactileDialogActivity.this.finish();
                                    }
                                })
                                .create();
                        endDialog.show();
                    }
                });
                return;
            }

            Log.e("DIALOG_MESSAGE", messageBody.length() > 0 ? messageBody : " ");

            try {
                Set<PointF> otherTouchPos = new HashSet<>();

                JSONObject json = new JSONObject(messageBody);

                JSONArray touchpos = json.getJSONArray("touchPos");
                int len = touchpos.length();
                for (int i = 0; i < len; i++) {
                    JSONObject pos = touchpos.getJSONObject(i);
                    otherTouchPos.add(new PointF((float)pos.getDouble("x"), (float)pos.getDouble("y")));
                }

                JSONArray param = json.getJSONArray("Param");
                len = param.length();
                float OtherRadius = -1;
                if( len > 0)
                    OtherRadius =  (float) param.getJSONObject(0).getDouble("r");
                tactileArea.setPartnerRadius(OtherRadius);
                tactileArea.setPartnerTouch(otherTouchPos);
                runOnUiThread(updateTouchesRunnable);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        app.getDialogInvitationHandler().removeListener(this);
        if (chatOut != null) {
            try {
                chatOut.sendMessage(END_DIALOG_MESSAGE);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        if (chatOut != null) {
            chatOut.close();
        }
        if (chatIn != null) {
            chatIn.close();
        }
        if (chatManager != null) {
            chatManager.removeChatListener(this);
        }
        conn.removeAsyncStanzaListener(invitationResultHandler);
        app.updatePresence();

        super.onDestroy();
    }

    private class InvitationResultHandler implements StanzaListener {
        @Override
        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
            IQ iq = (IQ)packet;
            if (iq.getType() == IQ.Type.result && iq.getFrom().equals(partnerJID + "/Smack")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPartnerConnected(true);
                    }
                });
            }
        }
    }

    @Override
    public void onUserInteraction() {
        app.resetIdleTimer("TactileDialogActivity");
    }

    @Override
    public void onInvitationReceived(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogInvitationsView.setVisibility(View.VISIBLE);
                dialogInvitationsView.setText((name.endsWith("/Smack") ? name.substring(0, name.length() - 6) : name) + " " + getResources().getString(R.string.invited_you));
            }
        });
    }
}

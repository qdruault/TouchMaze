package fr.UTC_CosTech_CRED.SimpleTouch.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PresenceTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import fr.UTC_CosTech_CRED.SimpleTouch.LoginActivity;
import fr.UTC_CosTech_CRED.SimpleTouch.R;
import fr.UTC_CosTech_CRED.SimpleTouch.util.touch.TactileDialogViewHolder;


/**
 * Created by Binova on 10/12/2015.
 */
public class DialogApp extends Application {
    public static final String HOST = /* "server.binova-services.com" */ "servgsp.utc.fr";
    public static final String SERVICE = /* "server.binova-services.com" */ "servgsp.utc.fr";
    public static final int PORT = 8080;

    /**     *  IMPORTANT. Values of these constants MUST correspond to those in pref_settings.xml
     *  */
    public static final String PREF_TOUCH_SIZE = "pref_touch_size";
    public static final String PREF_MY_POS = "pref_show_my_touch";
    public static final String PREF_PARTNER_POS = "pref_show_parthner_touch";
    public static final String PREF_MODE_TOUCHTHROUGH = "pref_ModeTT";
    public static final String PREF_TT_OUTPUT = "pref_signal_TT";
    public static final String PREF_DISCONNECT_TIMEOUT = "pref_disconnect_timeout";
    public static final String PREF_OLD_LOGIN = "pref_old_login";


    private AbstractXMPPConnection connection;

    private String status ;

    private Presence.Mode mode;

    private DialogInvitationHandler dialogInvitationHandler;

    private Map<String, IQ> invitations;

    private SharedPreferences prefs;

    public AbstractXMPPConnection realConnection(String username, String password) throws SmackException, IOException, XMPPException {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder()
                .setServiceName(DialogApp.SERVICE)
                .setHost(DialogApp.HOST)
                .setPort(DialogApp.PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)//@todo: no security here!
                .setConnectTimeout(10000);

        if (username != null && password != null) {
            builder.setUsernameAndPassword(username, password);
        }

        AbstractXMPPConnection conn = new XMPPTCPConnection(builder.build());
        conn.connect();

        if (username != null && password != null) {
            conn.login();

            conn.registerIQRequestHandler(getDialogInvitationHandler());
            conn.addAsyncStanzaListener(new SubscribeHandler(conn), PresenceTypeFilter.SUBSCRIBE);
        }

        connection = conn;

        return conn;
    }

    public AbstractXMPPConnection connect(String username, String password) throws SmackException, IOException, XMPPException
    {
        return realConnection(username, password);
    }

    public AbstractXMPPConnection connect() throws SmackException, IOException, XMPPException
    {
        return realConnection(null, null);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        invitations = new HashMap<String, IQ>();

        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        status = prefs.getString("STATUS", "Disponible");
        mode = Presence.Mode.values()[prefs.getInt("MODE", Presence.Mode.available.ordinal())];

        //Gestion des préférences utilisateur
        SharedPreferences.Editor editor = prefs.edit();
        if (!prefs.contains(PREF_TOUCH_SIZE))       {            editor.putInt(PREF_TOUCH_SIZE, 10);        }
        if (!prefs.contains(PREF_MY_POS))           {            editor.putBoolean(PREF_MY_POS, true);        }
        if (!prefs.contains(PREF_PARTNER_POS))      {            editor.putBoolean(PREF_PARTNER_POS, true);        }
        if (!prefs.contains(PREF_MODE_TOUCHTHROUGH)){            editor.putBoolean(PREF_MODE_TOUCHTHROUGH, true);        }
        if (!prefs.contains(PREF_DISCONNECT_TIMEOUT)) {          editor.putString(PREF_DISCONNECT_TIMEOUT, "300");        }

        editor.apply();
    }

    public AbstractXMPPConnection getConn() {
        return connection;
    }

    public DialogInvitationHandler getDialogInvitationHandler() {
        if (dialogInvitationHandler == null) {
            dialogInvitationHandler = new DialogInvitationHandler(this);
        }

        return dialogInvitationHandler;
    }

    public String getStatus() {
        return status;
    }

    public Presence.Mode getMode() {
        return mode;
    }

    public void setStatus(String status)
    {
        this.status = status;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("STATUS", status);
        editor.apply();
        updatePresence();
    }

    public void disconnect() {
        Presence offlinePresence = new Presence(Presence.Type.unavailable);
        try {
            connection.sendStanza(offlinePresence);
        } catch(SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        connection.disconnect();
    }

    public void setMode(Presence.Mode mode)
    {
        this.mode = mode;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("MODE", mode.ordinal());
        editor.apply();
        updatePresence();
    }

    public void updatePresence()
    {
        Presence presence = new Presence(mode == Presence.Mode.dnd ? Presence.Type.unavailable : Presence.Type.available);
        presence.setStatus(status);
        presence.setMode(mode);

        try {
            connection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void sendOccupiedPresence() {
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus(status);
        presence.setMode(Presence.Mode.away);

        try {
            connection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void addInvitation(IQ invitationIQ) {
        invitations.put(invitationIQ.getFrom(), invitationIQ);
    }

    public boolean hasInvited(String bareJID) {
        return invitations.containsKey(bareJID + "/Smack");
    }

    public void acceptInvitation(String partnerJID) {
        IQ invitation = invitations.get(partnerJID + "/Smack");
        invitations.remove(partnerJID + "/Smack");
        //IQ response = new IQ("accept_invitation", "http://binova-it.fr/tactile-dialog/invitation")
        IQ response = new IQ("accept_invitation", "www.intertact.net/invitation")

        {
            @Override
            protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
                xml.setEmptyElement();
                return xml;
            }
        };
        response.setTo(partnerJID + "/Smack");
        response.setType(IQ.Type.result);
        response.setStanzaId(invitation.getStanzaId());
        try {
            connection.sendStanza(response);
        } catch (SmackException.NotConnectedException e) { }
    }

    public TactileDialogViewHolder.OPTIONS GetOptions(){
        TactileDialogViewHolder.OPTIONS o = new TactileDialogViewHolder.OPTIONS();

        o.touchRadius   = prefs.getInt(PREF_TOUCH_SIZE, 10);
        o.IsMyPos       = prefs.getBoolean(PREF_MY_POS, true);
        o.IsParthnerPos = prefs.getBoolean(PREF_PARTNER_POS, true);
        o.IsModeTouchThrough    = prefs.getBoolean(PREF_MODE_TOUCHTHROUGH, true);
        o.TTVisual  = prefs.getStringSet(PREF_TT_OUTPUT, new HashSet<String>()).contains("VISUAL");
        o.TTVibr    = prefs.getStringSet(PREF_TT_OUTPUT, new HashSet<String>()).contains("VIBRATOR");
        o.TTSound   = prefs.getStringSet(PREF_TT_OUTPUT, new HashSet<String>()).contains("SOUND");
        o.DisconnectTimeOut = Integer.parseInt(prefs.getString(PREF_DISCONNECT_TIMEOUT, "300")) * 1000;

        return o;
    }

    public String getOldLogin() { return prefs.getString(PREF_OLD_LOGIN, null); }

    public void saveOldLogin(String newOldLogin)  {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_OLD_LOGIN, newOldLogin);
        editor.apply();
    }

    private static Handler disconnectHandler;

    public void resetIdleTimer(String activity) {
        synchronized (this) {
            if (disconnectHandler == null) {
                disconnectHandler = new Handler(Looper.getMainLooper()) {
                    public void handleMessage(Message msg) { }
                };
            }

            disconnectHandler.removeCallbacks(disconnectCallback);
            int timeout =  GetOptions().DisconnectTimeOut;
            disconnectHandler.postDelayed(disconnectCallback,timeout);
        }
    }

    public void stopIdleTimer() {
        if (disconnectHandler != null) {
            synchronized (this) {
                disconnectHandler.removeCallbacks(disconnectCallback);
            }
        }
    }

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            disconnect();
            disconnectHandler.removeCallbacks(disconnectCallback);

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (!applicationInForeground()) {
                intent.putExtra("EXIT", true);
            }
            startActivity(intent);
        }
    };

    public boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

        return services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(getPackageName().toString());
    }
}

package fr.UTC_CosTech_CRED.SimpleTouch.util;

import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.IQ;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Binova on 12/01/2016.
 */
public class DialogInvitationHandler implements IQRequestHandler {
    DialogApp app;
    Set<DialogInvitationHandlerListener> listeners;

    public DialogInvitationHandler(DialogApp app) {
        this.app = app;
        listeners = new HashSet<DialogInvitationHandlerListener>();
    }

    @Override
    public IQ handleIQRequest(IQ invitationIQ) {
        app.addInvitation(invitationIQ);

        for (DialogInvitationHandlerListener listener: listeners) {
            listener.onInvitationReceived(invitationIQ.getFrom());
        }

        return null;
    }

    public void addListener(DialogInvitationHandlerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DialogInvitationHandlerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Mode getMode() {
        return Mode.async;
    }

    @Override
    public IQ.Type getType() {
        return IQ.Type.get;
    }

    @Override
    public String getElement() { return "invitation"; }

    @Override
    public String getNamespace() {
//        return "http://binova-it.fr/tactile-dialog/invitation";
        return "www.intertact.net/invitation";
    }
}

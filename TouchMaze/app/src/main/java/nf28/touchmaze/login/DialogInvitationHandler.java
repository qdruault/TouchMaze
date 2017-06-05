package nf28.touchmaze.login;

import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.IQ;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Quentin on 24/05/2017.
 */

public class DialogInvitationHandler implements IQRequestHandler {
    DialogHandler app;
    Set<DialogInvitationHandlerListener> listeners;

    public DialogInvitationHandler(DialogHandler app) {
        this.app = app;
        listeners = new HashSet<>();
    }

    @Override
    public IQ handleIQRequest(IQ invitationIQ) {
        app.addInvitation(invitationIQ);

        for (DialogInvitationHandlerListener listener: listeners) {
            listener.onInvitationReceived(invitationIQ.getFrom());
        }

        return null;
    }

    /**
     * Ajoute un listener.
     * @param listener
     */
    public void addListener(DialogInvitationHandlerListener listener) {
        listeners.add(listener);
    }

    /**
     * Enlève un listener.
     * @param listener
     */
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
        return "www.intertact.net/invitation";
    }
}

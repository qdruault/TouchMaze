package nf28.touchmaze.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Baptiste on 13/06/2017.
 */

public class TutoAlertDialogFragment extends DialogFragment {

    public static TutoAlertDialogFragment newInstance(String messageBody) {
        TutoAlertDialogFragment frag = new TutoAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", messageBody);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String messageBody = getArguments().getString("message");

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setTitle("Tutoriel")
                .setMessage(messageBody)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((FragmentAlertDialog)getActivity()).doPositiveClick();
                                //dismiss();
                            }
                        }
                )

                .create();
    }
}

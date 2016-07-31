package inspector.ded.ajman.ajmaninspector;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by nezar on 7/30/16.
 */
public class FireMissilesDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ajman")
                .setItems(R.array.setting, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            startActivity(new Intent(getActivity(), SettingsActivity.class));
                        } else if (which == 1) {
                            MainActivity.mWebView.clearFormData();
                            MainActivity.mWebView.clearHistory();
                            MainActivity.mWebView.reload();
                        }
                    }
                });
        return builder.create();
    }

}
package com.iamraviraj.mang0055.ottawa.ca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Create an AboutDialogFragment displaying your full name and username.
 *
 * TODO all students: please read the following documentation to learn about Dialogs.
 * Reference:
 * Android Dialogs @ http://developer.android.com/guide/topics/ui/dialogs.html
 *
 * @author mang0055@AlgonquinCollege.com
 */

public class AboutDialogFragment extends DialogFragment {
  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.action_about)
        .setMessage(R.string.author)
        .setCancelable(false)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

    // Create the AlertDialog object and return it
    return builder.create();
  }
}

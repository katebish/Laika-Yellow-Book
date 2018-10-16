package com.laika.laika_yellow_book;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class PopupDialog extends AppCompatDialogFragment {
    Context currContext;

    public void setContext(Context context) {
        currContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Entry saved, do you want to create another entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent main = new Intent(currContext, MainActivity.class);
                        currContext.startActivity(main);
                        ((Activity) currContext).finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}


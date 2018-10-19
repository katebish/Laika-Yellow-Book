package com.laika.laika_yellow_book;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TwinCalfDialog extends AppCompatDialogFragment {
    private EditText calfID;
    private EditText calfSex;
    private EditText calfBW;
    private EditText calfCondition;
    private TwinCalfDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.twin_calf_dialog, null);

        calfID = view.findViewById(R.id.edit_TwinCalfID);
        calfSex = view.findViewById(R.id.edit_TwinSex);
        calfBW = view.findViewById(R.id.edit_TwinCalfBW);
        calfCondition = view.findViewById(R.id.edit_TwinCondition);
        List<String> twinData;

        String tag = this.getTag();
        if(tag == "reopen twin 1") {
            twinData = listener.getTwinData(1);
            calfID.setText(twinData.get(0));
            calfSex.setText(twinData.get(1));
            calfBW.setText(twinData.get(2));
            calfCondition.setText(twinData.get(3));
        }
        else if(tag == "reopen twin 2") {
            twinData = listener.getTwinData(2);
            calfID.setText(twinData.get(0));
            calfSex.setText(twinData.get(1));
            calfBW.setText(twinData.get(2));
            calfCondition.setText(twinData.get(3));
        }
        else if(tag == "reopen twin 3"){
            twinData = listener.getTwinData(3);
            calfID.setText(twinData.get(0));
            calfSex.setText(twinData.get(1));
            calfBW.setText(twinData.get(2));
            calfCondition.setText(twinData.get(3));
        }

        builder.setView(view)
            .setTitle("Add Twin Calf")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            })
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int ID = -1;
                    String sex = "";
                    Double BW = 0.0;
                    String condition = "";
                    try {
                        if (!calfID.getText().toString().isEmpty())
                            ID = Integer.parseInt(calfID.getText().toString());
                        if (!calfBW.getText().toString().isEmpty())
                            BW = Double.parseDouble(calfBW.getText().toString());
                        sex = calfSex.getText().toString();
                        if(calfCondition.getText().toString().isEmpty()) condition = "";
                        else condition = calfCondition.getText().toString();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    listener.passData(ID, sex, BW, condition);
                }
            });


        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TwinCalfDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement TwinDialogListener");
        }
    }

    @Override
    public Dialog getDialog() {
        return super.getDialog();
    }


}

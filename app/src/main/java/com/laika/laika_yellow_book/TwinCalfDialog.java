package com.laika.laika_yellow_book;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class TwinCalfDialog extends AppCompatDialogFragment {
    private TwinCalfDialogListener listener;
    private int id;
    private String sex;
    private Double birthweight;
    private String condition;
    private boolean update = false;
    private int index;

    public void setTextFields(int index, int id, String sex, Double birthweight, String condition) {
        update = true;
		this.index = index;
        this.id = id;
        this.sex = sex;
        this.birthweight = birthweight;
        this.condition = condition;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.twin_calf_dialog, null);

        final EditText calfID = view.findViewById(R.id.edit_TwinCalfID);
        final EditText calfSex = view.findViewById(R.id.edit_TwinSex);
        final EditText calfBW = view.findViewById(R.id.edit_TwinCalfBW);
        final EditText calfCondition = view.findViewById(R.id.edit_TwinCondition);

        if(update) {
            calfID.setText(String.valueOf(id));
            calfSex.setText(sex);
            if(birthweight != 0.0)
                calfBW.setText(String.valueOf(birthweight));
            calfCondition.setText(condition);
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
                        int ID = 0;
                        String sex = "";
                        Double BW = 0.0;
                        String condition = "";
                        try {
                            if(!calfID.getText().toString().isEmpty())
                                ID = Integer.parseInt(calfID.getText().toString());
                            if(!calfBW.getText().toString().isEmpty())
                                BW = Double.parseDouble(calfBW.getText().toString());
                            sex = calfSex.getText().toString();
                            condition = calfCondition.getText().toString();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if(update)
                            listener.updateData(index, ID, sex, BW, condition);
                        else
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

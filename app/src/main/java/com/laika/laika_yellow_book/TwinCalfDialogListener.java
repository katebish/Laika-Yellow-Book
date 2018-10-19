package com.laika.laika_yellow_book;

import android.provider.ContactsContract;

import java.util.List;

public interface TwinCalfDialogListener {
    void passData(int calfID, String calfSex, Double calfBW, String calfCondition);
    List<String> getTwinData(Integer twin);

}

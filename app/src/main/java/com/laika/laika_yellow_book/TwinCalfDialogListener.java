package com.laika.laika_yellow_book;

public interface TwinCalfDialogListener {
    void passData(int calfID, String calfSex, Double calfBW, String calfCondition);
    void updateData (int index, int calfID, String calfSex, Double calfBW, String calfCondition);
}

package com.laika.laika_yellow_book;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class CowSearchedAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public CowSearchedAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(CowSearched object){
        list.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}

package com.laika.laika_yellow_book;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        return list.get(position);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        cowSearchedHolder cowSearchedHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_search_result_row,parent,false);
            cowSearchedHolder = new cowSearchedHolder();
            cowSearchedHolder.tx_cowNum = (TextView) row.findViewById(R.id.t_cowNum);
            cowSearchedHolder.tx_sire = (TextView) row.findViewById(R.id.t_sire);
            cowSearchedHolder.tx_calfID = (TextView) row.findViewById(R.id.t_calfID);
            row.setTag(cowSearchedHolder);
        }
        else {
            cowSearchedHolder = (cowSearchedHolder) row.getTag();
        }
        return super.getView(position, convertView, parent);
    }

    static class cowSearchedHolder
    {
        TextView tx_cowNum, tx_sire, tx_calfID;
    }
}

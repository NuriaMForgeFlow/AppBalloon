package com.example.appballoon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appballoon.Class.Passenger;
import com.example.appballoon.R;

import java.util.ArrayList;

public class RecordListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Passenger> recordList;

    public RecordListAdapter(Context context, int layout, ArrayList<Passenger> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView txtName,txtSurname,txtDni,txtTerms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row= convertView;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout,null);
            holder.txtName=row.findViewById(R.id.txtName);
            holder.txtSurname=row.findViewById(R.id.txtSurname);
            holder.txtDni=row.findViewById(R.id.txtDni);
            holder.txtTerms=row.findViewById(R.id.txtTerms);
            row.setTag(holder);
        }
        else{
            holder=(ViewHolder)row.getTag();
        }

        Passenger passenger=recordList.get(position);

        holder.txtName.setText(passenger.getName());
        holder.txtSurname.setText(passenger.getSurname());
        holder.txtDni.setText(passenger.getDni());
        holder.txtTerms.setText(passenger.getTerms());

        return row;
    }
}


package com.example.appballoon.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appballoon.AddPassenger;
import com.example.appballoon.Class.Balloon;
import com.example.appballoon.Documentation;
import com.example.appballoon.MainPassenger;
import com.example.appballoon.R;
import com.example.appballoon.Signature;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class MyAdapterDoc extends RecyclerView.Adapter<MyAdapterDoc.MyViewHolder> {

    Context context;
    ArrayList<Balloon> balloons ;



    public MyAdapterDoc(Context context, ArrayList<Balloon> balloons){

        //context=c;
        //balloons=b;
        this.balloons=balloons;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.balloon_item,viewGroup,false);
        return new MyViewHolder(view,context,balloons);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.license.setText(balloons.get(i).getLicense());
        myViewHolder.cda.setText(balloons.get(i).getCda());
        myViewHolder.cdm.setText(balloons.get(i).getCdm());
        myViewHolder.insurance.setText(balloons.get(i).getInsurance());
        myViewHolder.rcda.setText(balloons.get(i).getRcda());



    }

    @Override
    public int getItemCount() {
        if (balloons != null)
            return balloons.size();
        return 0;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView license,cda,cdm,insurance,rcda;
        ArrayList<Balloon> balloons;
        Context context;
        public MyViewHolder(@NonNull View itemView, Context context, ArrayList<Balloon>balloons){
            super(itemView);
            this.balloons=balloons;
            this.context=context;
            itemView.setOnClickListener(this);
            license=(TextView)itemView.findViewById(R.id.txt_license);
            cda=(TextView)itemView.findViewById(R.id.txt_cda);
            cdm=(TextView)itemView.findViewById(R.id.txt_cdm);
            insurance=(TextView)itemView.findViewById(R.id.txt_insurance);
            rcda=(TextView)itemView.findViewById(R.id.txt_rcda);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final Balloon balloon =this.balloons.get(position);
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.GERMANY);
            Date date_ins = null;
            Date date_rcda=null;
            try {
                date_ins = sdf.parse(balloon.getInsurance());
                date_rcda=sdf.parse(balloon.getRcda());
            } catch (ParseException e) {
                e.printStackTrace();
               Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
            }
            Date today=new Date();
            final String todayAsString = sdf.format(today);


            if (today.after(date_ins)){
                AlertDialog.Builder alert_ins= new AlertDialog.Builder(context);
                alert_ins.setTitle("Warning");
                alert_ins.setMessage("Insurance is expired. Do you want to continue?");
                alert_ins.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent_sign= new Intent(context, Signature.class);
                        intent_sign.putExtra("license",balloon.getLicense());
                        intent_sign.putExtra("cda",balloon.getCda());
                        intent_sign.putExtra("cdm",balloon.getCdm());
                        intent_sign.putExtra("insurance",balloon.getInsurance());
                        intent_sign.putExtra("rcda",balloon.getRcda());
                        intent_sign.putExtra("today",todayAsString);
                        context.startActivity(intent_sign);
                    }
                });
                alert_ins.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert_ins.show();

                }
                else if(today.after(date_rcda)) {
                AlertDialog.Builder alert_rcda= new AlertDialog.Builder(context);
                alert_rcda.setTitle("Warning");
                alert_rcda.setMessage("ARC is expired. Do you want to continue?");
                alert_rcda.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent_sign= new Intent(context,Signature.class);
                        intent_sign.putExtra("license",balloon.getLicense());
                        intent_sign.putExtra("cda",balloon.getCda());
                        intent_sign.putExtra("cdm",balloon.getCdm());
                        intent_sign.putExtra("insurance",balloon.getInsurance());
                        intent_sign.putExtra("rcda",balloon.getRcda());
                        intent_sign.putExtra("today",todayAsString);
                        context.startActivity(intent_sign);

                    }
                });
                alert_rcda.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert_rcda.show();

            }

            else{
                byte [] empty = new byte[1024];
                    try {
                        Documentation.mSQLiteHelperDoc.insertData(
                                balloon.getLicense().trim(),
                                balloon.getCda().trim(),
                                balloon.getCdm().trim(),
                                balloon.getInsurance().trim(),
                                balloon.getRcda().trim(),
                                todayAsString.trim(),
                                empty


                        );
                        Toast.makeText(context,"Added successfully",Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent_passengers=new Intent(context, MainPassenger.class);
                    context.startActivity(intent_passengers);
            }
        }
    }
}

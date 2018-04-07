package com.example.android.uidemo;

/**
 * Created by Honey on 02-Jun-16.
 */

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    public static boolean checkedStatus = false;
    private List<ListContent> Listcontent;

    public ListAdapter(List<ListContent> Listcontent) {
        this.Listcontent = Listcontent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListContent list = Listcontent.get(position);
        holder.Name.setText(list.getFirst_Name() + " " + list.getLast_Name());
        holder.Watch.setText(list.getOrder_Description());
        holder.Price.setText("₹" + list.getTotal_Amount());
        holder.Phone.setText(list.Contact_Number);
        holder.mstatus = list.getStatus();

        if (holder.mstatus.equalsIgnoreCase("y")) {
            holder.Name.setTextColor(Color.parseColor("#FFEB3B"));

        } else if (holder.mstatus.equalsIgnoreCase("r")) {
            holder.Name.setTextColor(Color.parseColor("#F44336"));
        } else if (holder.mstatus.equalsIgnoreCase("g")) {
            holder.Name.setTextColor(Color.parseColor("#4CAF50"));
        } else if (holder.mstatus.equalsIgnoreCase("p")) {
            holder.Name.setTextColor(Color.parseColor("#E91E63"));
        } else if (holder.mstatus.equalsIgnoreCase("b")) {
            holder.Name.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return Listcontent.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, Watch, Price, Phone;
        String mstatus;

        public MyViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.Name);
            Watch = (TextView) view.findViewById(R.id.Watch);
            Price = (TextView) view.findViewById(R.id.Price);
            Phone = (TextView) view.findViewById(R.id.Phone);
        }
    }
}
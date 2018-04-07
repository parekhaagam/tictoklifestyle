package com.example.android.uidemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

public class DelhiveryRecyclerViewAdapter extends RecyclerView.Adapter<DelhiveryRecyclerViewAdapter.SimpleViewHolder> {


    DatabaseAdapter databaseAdapter;
    Activity mainActivity;
    String phone = "", previousStatus = "", CurrentStatus = "";
    private Context mContext;
    private ArrayList<ListContent> Listcontent;

    public DelhiveryRecyclerViewAdapter(Context context, ArrayList<ListContent> objects, Activity mainActivity) {
        this.mContext = context;
        this.Listcontent = objects;
        this.mainActivity = mainActivity;
        databaseAdapter = new DatabaseAdapter(this.mContext);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_row, parent, false);

        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        final ListContent currentListContent = Listcontent.get(position);
        holder.tvName.setText(currentListContent.getFirst_Name() + " " + currentListContent.getLast_Name());
        holder.tvName.setTextColor(0xFFFFEB3B);
        holder.tvWatch.setText(currentListContent.getOrder_Description());
        holder.tvPhone.setText(currentListContent.getContact_Number());
        holder.tvPrice.setText("₹" + currentListContent.getTotal_Amount());
        holder.webView.getSettings().setJavaScriptEnabled(true);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, Detail.class);
                intent.putExtra("phone", currentListContent.getContact_Number());
                mainActivity.startActivity(intent);
            }
        });

        holder.Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.tvPhone.getText().toString()));
                mainActivity.startActivity(intent);
            }
        });

        holder.Whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + holder.tvPhone.getText().toString());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                mainActivity.startActivity(i);
            }
        });

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.detail.getText().equals("Show More")) {
                    holder.detail.setText("Show Less");
                    holder.webView.loadUrl("https://track.delhivery.com/p/" + currentListContent.getTrackingNo().toString().trim());
                    Log.i("Webview", "visible");
                    holder.webView.setVisibility(View.VISIBLE);


                } else {
                    holder.detail.setText("Show More");
                    holder.webView.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return Listcontent.size();
    }


    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView tvName;
        TextView tvWatch;
        TextView tvPhone;
        TextView tvPrice;
        TextView detail;
        WebView webView;
        ImageButton Call, Whatsapp;
        CardView cardView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            tvName = (TextView) itemView.findViewById(R.id.Name);
            tvWatch = (TextView) itemView.findViewById(R.id.Watch);
            tvPhone = (TextView) itemView.findViewById(R.id.Phone);
            tvPrice = (TextView) itemView.findViewById(R.id.Price);
            detail = (TextView) itemView.findViewById(R.id.detail);
            webView = (WebView) itemView.findViewById(R.id.webview);
            Call = (ImageButton) itemView.findViewById(R.id.Call);
            Whatsapp = (ImageButton) itemView.findViewById(R.id.Whatsapp);
        }
    }
}

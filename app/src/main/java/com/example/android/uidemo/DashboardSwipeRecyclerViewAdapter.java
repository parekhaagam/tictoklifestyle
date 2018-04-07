package com.example.android.uidemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

public class DashboardSwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<DashboardSwipeRecyclerViewAdapter.SimpleViewHolder> {


    DatabaseAdapter databaseAdapter;
    Activity mainActivity;
    String phone = "", previousStatus = "", CurrentStatus = "", timeStamp;
    private Context mContext;
    private ArrayList<ListContent> Listcontent;

    public DashboardSwipeRecyclerViewAdapter(Context context, ArrayList<ListContent> objects, Activity mainActivity) {
        this.mContext = context;
        this.Listcontent = objects;
        this.mainActivity = mainActivity;
        databaseAdapter = new DatabaseAdapter(this.mContext);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_swipe_row_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final ListContent item = Listcontent.get(position);

        viewHolder.tvName.setText((item.getFirst_Name()) + " " + item.getLast_Name());
        viewHolder.tvWatch.setText(item.getOrder_Description());
        viewHolder.tvPhone.setText(item.getContact_Number());
        viewHolder.tvPrice.setText("₹" + item.getTotal_Amount());
        viewHolder.tvTimeStamp.setText(item.getTimestamp());

        if (item.getStatus().equalsIgnoreCase("y")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#FFEB3B"));

        } else if (item.getStatus().equalsIgnoreCase("r")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#F44336"));
        } else if (item.getStatus().equalsIgnoreCase("g")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#4CAF50"));
        } else if (item.getStatus().equalsIgnoreCase("p")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#E91E63"));
        } else if (item.getStatus().equalsIgnoreCase("b")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#000000"));
        }

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping0
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        /*viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((((SwipeLayout) v).getOpenStatus() == SwipeLayout.Status.Close)) {
                    //Start your activity

                    Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/
        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListContent listContent = Listcontent.get(position);
                Intent intent = new Intent(mainActivity, Detail.class);
                intent.putExtra("phone", viewHolder.tvPhone.getText().toString());
                mainActivity.startActivity(intent);
                mainActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                mainActivity.finish();
                SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("Navigation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Show", "0&" + listContent.getContact_Number());
                editor.apply();
                //mainActivity.finish();
                //  mainActivity.overridePendingTransition(R.anim.right_out, R.anim.left_in);

            }
        });

        viewHolder.Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + viewHolder.tvPhone.getText().toString()));
                mainActivity.startActivity(intent);
            }
        });
        viewHolder.Whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viewHolder.tvPhone.getText().toString()
                Uri uri = Uri.parse("smsto:" + viewHolder.tvPhone.getText().toString());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                mainActivity.startActivity(i);
            }
        });
        viewHolder.tvDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = viewHolder.tvPhone.getText().toString();
                timeStamp = viewHolder.tvTimeStamp.getText().toString();
                CurrentStatus = "g";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                databaseAdapter.Update(viewHolder.tvPhone.getText().toString(), "g", viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, v, position);
            }

        });


        viewHolder.tvDispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = viewHolder.tvPhone.getText().toString();
                timeStamp = viewHolder.tvTimeStamp.getText().toString();
                CurrentStatus = "y";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                databaseAdapter.Update(viewHolder.tvPhone.getText().toString(), "y", viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, view, position);
                //Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamp = viewHolder.tvTimeStamp.getText().toString();
                phone = viewHolder.tvPhone.getText().toString();
                CurrentStatus = "r";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                databaseAdapter.Update(viewHolder.tvPhone.getText().toString(), "r", viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, view, position);
                //Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.tvNt_Available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamp = viewHolder.tvTimeStamp.getText().toString();
                phone = viewHolder.tvPhone.getText().toString();
                CurrentStatus = "p";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                databaseAdapter.Update(viewHolder.tvPhone.getText().toString(), "p", viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, view, position);
            }
        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }

    public void refresh(final SimpleViewHolder viewHolder, View view, final int index) {
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
        Listcontent.remove(index);
        notifyItemRemoved(index);
        //this.Listcontent.clear();
        //this.Listcontent.addAll(databaseAdapter.getAllDataBlank("b"));
        notifyDataSetChanged();

        Snackbar.make(view, "Updated Sucessfully", Snackbar.LENGTH_LONG).
                setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(phone + "    " + previousStatus + "    " + index);
                        databaseAdapter.Update(phone, previousStatus, timeStamp);
                        //getData(view);
                        Listcontent.clear();
                        Listcontent.addAll(databaseAdapter.getAllDataBlank("b"));
                        notifyDataSetChanged();
                        //refresh(viewHolder,view,index);
                    }
                }).show();

        //notifyItemRangeChanged(position, Listcontent.size());
        mItemManger.closeAllItems();
    }

    @Override
    public int getItemCount() {
        return Listcontent.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView tvName;
        TextView tvWatch;
        TextView tvPhone;
        TextView tvPrice;
        TextView tvTimeStamp;

        TextView tvDeliver;
        TextView tvDispatch;
        TextView tvNt_Available;
        TextView tvCancel;

        ImageButton Call, Whatsapp;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            tvName = (TextView) itemView.findViewById(R.id.Name);
            tvWatch = (TextView) itemView.findViewById(R.id.Watch);
            tvPhone = (TextView) itemView.findViewById(R.id.Phone);
            tvPrice = (TextView) itemView.findViewById(R.id.Price);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.TimeStamp);

            tvCancel = (TextView) itemView.findViewById(R.id.tvCancel);
            tvDispatch = (TextView) itemView.findViewById(R.id.tvDispatch);
            tvNt_Available = (TextView) itemView.findViewById(R.id.tvNt_Available);
            tvDeliver = (TextView) itemView.findViewById(R.id.tvDeliver);

            Call = (ImageButton) itemView.findViewById(R.id.Call);
            Whatsapp = (ImageButton) itemView.findViewById(R.id.Whatsapp);
        }
    }
}

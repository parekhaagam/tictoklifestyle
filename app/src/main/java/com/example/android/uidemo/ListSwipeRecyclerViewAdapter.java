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

public class ListSwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<ListSwipeRecyclerViewAdapter.SimpleViewHolder> {


    DatabaseAdapter databaseAdapter;
    Activity mainActivity;
    String phone = "", previousStatus = "", CurrentStatus = "", timeStamp = "";
    private Context mContext;
    private ArrayList<ListContent> Listcontent;

    public ListSwipeRecyclerViewAdapter(Context context, ArrayList<ListContent> objects, Activity mainActivity) {
        this.mContext = context;
        this.Listcontent = objects;
        this.mainActivity = mainActivity;
        databaseAdapter = new DatabaseAdapter(this.mContext);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_swipe_row_item, parent, false);
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
            settvOptions(viewHolder.tvOption1, "Deliver", 0xFF76FF03);//green
            settvOptions(viewHolder.tvOption2, "Cancel", 0xFFB71C1C);//red
            settvOptions(viewHolder.tvOption3, "Nt Avail", 0xFFE91E63);//pink
        } else if (item.getStatus().equalsIgnoreCase("r")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#F44336"));
            settvOptions(viewHolder.tvOption1, "Deliver", 0xFF76FF03);//green
            settvOptions(viewHolder.tvOption2, "Dispatch", 0xFFFFFF00);//yellow
            settvOptions(viewHolder.tvOption3, "Nt Avail", 0xFFE91E63);//pink

        } else if (item.getStatus().equalsIgnoreCase("g")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#4CAF50"));
            settvOptions(viewHolder.tvOption1, "Dispatch", 0xFFFFFF00);//yellow
            settvOptions(viewHolder.tvOption2, "Cancel", 0xFFB71C1C);//red
            settvOptions(viewHolder.tvOption3, "Nt Avail", 0xFFE91E63);//pink
        } else if (item.getStatus().equalsIgnoreCase("p")) {
            viewHolder.tvName.setTextColor(Color.parseColor("#E91E63"));
            settvOptions(viewHolder.tvOption1, "Deliver", 0xFF76FF03);//green
            settvOptions(viewHolder.tvOption2, "Dispatch", 0xFFFFFF00);//yellow
            settvOptions(viewHolder.tvOption3, "Cancel", 0xFFB71C1C);//red
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
                intent.putExtra("phone", listContent.getContact_Number());
                mainActivity.startActivity(intent);
                mainActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("Navigation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Show", "1&" + listContent.getContact_Number());
                editor.apply();
                mainActivity.finish();
                // mainActivity.overridePendingTransition(R.anim.right_out, R.anim.left_in);
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
                mainActivity.startActivity(Intent.createChooser(i, ""));
            }
        });
        viewHolder.tvDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamp = item.getTrackingNo();
                phone = viewHolder.tvPhone.getText().toString();
                CurrentStatus = "b";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                databaseAdapter.Update(viewHolder.tvPhone.getText().toString(), "b", viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, v, position);
            }

        });

        viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamp = item.getTrackingNo();
                Snackbar.make(v, "Want to Delete Record forever", Snackbar.LENGTH_LONG).
                        setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                //System.out.println(viewHolder.tvPhone.getText().toString() + "    " + previousStatus + "    " + index);
                                databaseAdapter.DeleteRecord(viewHolder.tvPhone.getText().toString(), viewHolder.tvTimeStamp.getText().toString());
                                //getData(view);
                                Listcontent.clear();
                                Listcontent.addAll(databaseAdapter.getAllData());
                                notifyDataSetChanged();
                                //refresh(viewHolder,view,index);
                                mItemManger.closeAllItems();
                            }
                        }).show();
            }
        });
        viewHolder.tvOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamp = item.getTrackingNo();
                phone = viewHolder.tvPhone.getText().toString();
                CurrentStatus = "y";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                UpdateOptionStatus(viewHolder.tvPhone.getText().toString(), viewHolder.tvOption1.getText().toString(), viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, view, position);
                //Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.tvOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamp = item.getTrackingNo();
                phone = viewHolder.tvPhone.getText().toString();
                CurrentStatus = "r";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                UpdateOptionStatus(viewHolder.tvPhone.getText().toString(), viewHolder.tvOption2.getText().toString(), viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, view, position);
                //Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.tvOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamp = item.getTrackingNo();
                phone = viewHolder.tvPhone.getText().toString();
                CurrentStatus = "p";
                previousStatus = databaseAdapter.getStatus(viewHolder.tvPhone.getText().toString());
                UpdateOptionStatus(viewHolder.tvPhone.getText().toString(), viewHolder.tvOption3.getText().toString(), viewHolder.tvTimeStamp.getText().toString());
                refresh(viewHolder, view, position);
            }
        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }

    public void settvOptions(TextView tv, String displayText, int color) {
        tv.setText(displayText);
        tv.setBackgroundColor(color);
    }

    public void UpdateOptionStatus(String phone, String tvText, String timeStamp1) {
        String status = "";
        if (tvText.equalsIgnoreCase("Deliver")) {
            status = "g";
        } else if (tvText.equalsIgnoreCase("Dispatch")) {
            status = "y";
        } else if (tvText.equalsIgnoreCase("Cancel")) {
            status = "r";
        } else if (tvText.equalsIgnoreCase("Nt Avail")) {
            status = "p";
        } else if (tvText.equalsIgnoreCase("DashBoard")) {
            status = "b";
        }

        databaseAdapter.Update(phone, status, timeStamp1);
    }

    public void refresh(final SimpleViewHolder viewHolder, View view, final int index) {
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);

        this.Listcontent.clear();
        refreshList();
        notifyDataSetChanged();

        Snackbar.make(view, "Updated Sucessfully", Snackbar.LENGTH_LONG).
                setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(phone + "    " + previousStatus + "    " + index);
                        databaseAdapter.Update(phone, previousStatus, timeStamp);
                        //getData(view);
                        refreshList();
                        ListFragment.spinner.getSelectedItem();

                        notifyDataSetChanged();
                        //refresh(viewHolder,view,index);
                        Snackbar.make(view, "UNDO Sucessfully", Snackbar.LENGTH_LONG).show();
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

    public void refreshList() {
        String spinnerStatus = "";
        if (ListFragment.spinner.getSelectedItem().toString().equals("By All")) {
            spinnerStatus = "a";
        } else if (ListFragment.spinner.getSelectedItem().toString().equals("By Delivery")) {
            spinnerStatus = "g";
        } else if (ListFragment.spinner.getSelectedItem().toString().equals("By Dispatch")) {
            spinnerStatus = "y";
        } else if (ListFragment.spinner.getSelectedItem().toString().equals("By Cancel")) {
            spinnerStatus = "r";
        } else if (ListFragment.spinner.getSelectedItem().toString().equals("By Nt Avail")) {
            spinnerStatus = "p";
        }

        Listcontent.clear();
        if (spinnerStatus == "a") {
            Listcontent.addAll(databaseAdapter.getAllDataExcept("b"));
        } else {
            Listcontent.addAll(databaseAdapter.getAllDataBlank(spinnerStatus));
        }

    }

    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView tvName;
        TextView tvWatch;
        TextView tvPhone;
        TextView tvPrice;
        TextView tvTimeStamp;
        TextView tvDashBoard;
        TextView tvOption1;
        TextView tvOption3;
        TextView tvOption2;

        ImageButton Delete;
        ImageButton Call, Whatsapp;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            tvName = (TextView) itemView.findViewById(R.id.Name);
            tvWatch = (TextView) itemView.findViewById(R.id.Watch);
            tvPhone = (TextView) itemView.findViewById(R.id.Phone);
            tvPrice = (TextView) itemView.findViewById(R.id.Price);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.TimeStamp);

            tvOption1 = (TextView) itemView.findViewById(R.id.tvOption1);
            tvOption2 = (TextView) itemView.findViewById(R.id.tvOption2);
            tvOption3 = (TextView) itemView.findViewById(R.id.tvOption3);
            tvDashBoard = (TextView) itemView.findViewById(R.id.tvDashBoard);

            Delete = (ImageButton) itemView.findViewById(R.id.delete);
            Call = (ImageButton) itemView.findViewById(R.id.Call);
            Whatsapp = (ImageButton) itemView.findViewById(R.id.Whatsapp);
        }
    }
}

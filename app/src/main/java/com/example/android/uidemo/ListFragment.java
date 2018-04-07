package com.example.android.uidemo;

/**
 * Created by Honey on 17-Jun-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    /*    View rootView;
        private List<ListContent> Listcontent = new ArrayList<>();
        private RecyclerView recyclerView;
        private ListAdapter mAdapter;
        DatabaseAdapter databaseAdapter;
        Activity mainActivity;*/

    public static Spinner spinner;
    Activity mainActivity;
    ListSwipeRecyclerViewAdapter mAdapter;
    DatabaseAdapter databaseAdapter;
    Spinner Searchspinner;
    TextView Search;
    String data, searchQuery, status;
    TextWatcher textWatcher;
    private RecyclerView mRecyclerView;
    private ArrayList<ListContent> listContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  /*      this.mainActivity=getActivity();
        this.rootView = inflater.inflate(R.layout.attachment_fragment, container, false);
        databaseAdapter=new DatabaseAdapter(mainActivity);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new ListAdapter(Listcontent);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mainActivity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL));



        // set the adapter
        //recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mainActivity.getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ListContent listContent = Listcontent.get(position);
                Intent intent = new Intent(getActivity(), Detail.class);
                intent.putExtra("phone",listContent.getContact_Number());
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), movie.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
               /* ListContent listContent = Listcontent.get(position);
                         // updateDetailFragment(listContent.getContact_Number());
                Snackbar.make(view, listContent.getFirst_Name(), Snackbar.LENGTH_LONG).show();
            }
        }));
*/
        this.mainActivity = getActivity();
        final View rootView = inflater.inflate(R.layout.list_swipe_recyclerview, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        databaseAdapter = new DatabaseAdapter(mainActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL));

        this.listContent = new ArrayList<ListContent>();


        textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Searching();
            }
        };
        Search = (TextView) rootView.findViewById(R.id.Search);
        Search.addTextChangedListener(textWatcher);

        // loadData();
        this.mAdapter = new ListSwipeRecyclerViewAdapter(mainActivity, this.listContent, getActivity());
        this.spinner = (Spinner) rootView.findViewById(R.id.spinner1);
        this.Searchspinner = (Spinner) rootView.findViewById(R.id.search);
        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((ListSwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        /* Scroll Listeners */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (spinner.getSelectedItem().toString().equals("By All")) {
                    refresh();
                    status = "a";
                } else if (spinner.getSelectedItem().toString().equals("By Delivery")) {
                    refresh("g");
                } else if (spinner.getSelectedItem().toString().equals("By Dispatch")) {
                    refresh("y");
                } else if (spinner.getSelectedItem().toString().equals("By Cancel")) {
                    refresh("r");
                } else if (spinner.getSelectedItem().toString().equals("By Nt Avail")) {
                    refresh("p");
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        this.listContent.clear();
        refresh();
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("Navigation", Context.MODE_PRIVATE);
        String show = sharedPreferences.getString("Show", "empty");
        Log.i("show", show);
        if (!show.equals("empty") && show.contains("&") && show.startsWith("1")) {
         //   refresh("a");
            show = show.substring(show.indexOf("&") + 1, show.length());
            Log.i("show num", show+"\tSize:"+listContent.size());
            for (int i = 0; i < listContent.size(); i++) {
                Log.i("Phone", listContent.get(i).getContact_Number());
                if (show.trim().equals(listContent.get(i).getContact_Number().trim()))
                    this.mRecyclerView.smoothScrollToPosition(i);
            }
        }

        return rootView;
    }

    public void speech() {
        SharedPreferences sharedPreferences = this.mainActivity.getSharedPreferences("Speech", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("Status", false)) {
            Log.i("list", "created");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Status", false);
            editor.commit();
            String data = sharedPreferences.getString("Find", "");
            Log.i("data", data);
            data = data.trim();
            for (int i = 0; i < data.length(); i++) {
                spinner.setSelection(2);
                Search.setText(Search.getText().toString() + data.charAt(i));
            }
        }
    }

    public void Searching() {
        data = Search.getText().toString();
        searchQuery = Searchspinner.getSelectedItem().toString();
        int index = 0;
        if (searchQuery.equals("By Name")) {
            index = 1;
        } else if (searchQuery.equals("By Phone")) {
            index = 3;
        } else if (searchQuery.equals("By Watch")) {
            index = 11;
        }

        System.out.println(data + ":" + index + ":" + status);
        listContent.clear();
        listContent.addAll(databaseAdapter.Search(data, index, status));
        mAdapter.notifyDataSetChanged();
    }

    public void refresh(String status) {
        this.status = status;
        System.out.println("Length:" + Search.getText().toString().length());
        if (Search.getText().toString().length() > 0) {
            Searching();
        } else {
            this.listContent.clear();
            try {
                this.status = status;
                listContent.addAll(databaseAdapter.getAllDataBlank(status));
                this.mAdapter.notifyDataSetChanged();
            } catch (NullPointerException n) {
                Log.d("Null pointer", n.getMessage());
            }
        }
        speech();
    }

    public void refresh() {
        if (Search.getText().toString().length() > 0) {
            this.listContent.clear();
            Searching();
        } else {
            this.listContent.clear();
            try {
                listContent.addAll(databaseAdapter.getAllDataExcept("b"));
                this.mAdapter.notifyDataSetChanged();
            } catch (NullPointerException n) {
                Log.d("Null pointer", n.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        mAdapter.notifyDataSetChanged();
        super.onDestroy();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}

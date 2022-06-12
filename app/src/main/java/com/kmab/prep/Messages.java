package com.kmab.prep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    RecyclerView rvQuestions;
    List list;
    AdapterQuestions adapterQuestions;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(this, R.style.mainToolbarText);
        setSupportActionBar(toolbar);

        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            w.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        rvQuestions = findViewById(R.id.rvQuestions);
        list = new ArrayList();

        SetterQuestions setter = new SetterQuestions("", "", "Auto responses", "", true);
        SetterQuestions setter1 = new SetterQuestions("", "", "How to", "", true);
        SetterQuestions setter2 = new SetterQuestions("", "", "Surveys", "", true);
        SetterQuestions setter3 = new SetterQuestions("", "", "Maps locations", "", true);
        SetterQuestions setter4 = new SetterQuestions("", "", "Logout", "", true);

        list.add(setter);
        list.add(setter1);
        list.add(setter2);
        list.add(setter3);
        list.add(setter4);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvQuestions.setLayoutManager(layoutManager);
        rvQuestions.setHasFixedSize(true);

        adapterQuestions = new AdapterQuestions(list);
        rvQuestions.setAdapter(adapterQuestions);
    }

    class AdapterQuestions extends RecyclerView.Adapter<AdapterQuestions.Holder> {

        private List listAdapter;
        Toast toast;

        public AdapterQuestions(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_questions;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterQuestions) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName;

            public Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
            }

            void bind(SetterQuestions setter) {
                tvName.setText(setter.getName());
                tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SetterQuestions setterQuestions = (SetterQuestions) listAdapter.get(getAdapterPosition());
                        handleClicks(setterQuestions, getAdapterPosition());
                    }
                });
            }
        }

        private void handleClicks(SetterQuestions setterQuestions, int position) {
            if (position == 4) {
                editor = prefs.edit();
                editor.putBoolean(AppInfo.BOOL_ADMIN, false);
                editor.apply();

                Toast.makeText(Messages.this, "Logout out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Messages.this, MainActivity.class));
                finish();
                return;
            } else if (position == 0)
                startActivity(new Intent(Messages.this, AutoResponses.class));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(Messages.this, setterQuestions.getName(), Toast.LENGTH_SHORT);

            toast.show();
        }
    }

}

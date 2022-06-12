package com.kmab.prep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvQuestions;
    List list;
    AdapterQuestions adapterQuestions;
    ImageView ivProgress;


    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(this, R.style.mainToolbarText);
        setSupportActionBar(toolbar);

        FirebaseAuth.getInstance().signInAnonymously();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        if (prefs.getBoolean(AppInfo.BOOL_ADMIN, false)) {
            startActivity(new Intent(MainActivity.this, Messages.class));
            finish();
            return;
        }
        rvQuestions = findViewById(R.id.rvQuestions);
        list = new ArrayList();
        ivProgress = findViewById(R.id.ivProgress);

        SetterQuestions setter = new SetterQuestions("", "", "Get Started", "", true);
        SetterQuestions setter1 = new SetterQuestions("", "", "How to", "", true);
        SetterQuestions setter2 = new SetterQuestions("", "", "Maps", "", true);
        SetterQuestions setter3 = new SetterQuestions("", "", "Take a survey", "", true);

        list.add(setter);
        list.add(setter1);
        list.add(setter2);
        list.add(setter3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvQuestions.setLayoutManager(layoutManager);
        rvQuestions.setHasFixedSize(true);

        adapterQuestions = new AdapterQuestions(list);
        rvQuestions.setAdapter(adapterQuestions);
        //ivProgress.setRotation(90);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, Login.class));
        } else if (id == R.id.action_logout) {
            editor = prefs.edit();
            editor.putBoolean(AppInfo.BOOL_ADMIN, false);
            editor.apply();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
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
                Toast.makeText(getApplicationContext(), "Item: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                tvName.setText(setter.getName());
                tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SetterQuestions setterQuestions = (SetterQuestions) listAdapter.get(getAdapterPosition());
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(MainActivity.this, setterQuestions.getName(), Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });
            }
        }
    }

}

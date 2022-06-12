package com.kmab.prep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AutoResponses extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etText;
    ImageView ivResponse, ivQuestion, ivProgress;
    ConstraintLayout constMain;
    CardView cardInput;
    List list;
    List listKeys;

    DBOperations dbOperations;
    //AdapterQuestions adapterQuestions;
    SQLiteDatabase db;
    Handler handler;

    boolean isQuestion = false;
    boolean isQuestionOpen = false;
    final String databaseRoot = "questions";

    String key, parentKey = "mainQuestion", name, link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_responses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            w.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        try {
            key = getIntent().getExtras().getString("key");
            parentKey = getIntent().getExtras().getString("parentKey");
        } catch (Exception e) {
            //
        }
        handler = new Handler();
        dbOperations = new DBOperations(this);
        db = dbOperations.getWritableDatabase();
        constMain = findViewById(R.id.constMain);
        recyclerView = findViewById(R.id.recyclerView);
        ivResponse = findViewById(R.id.ivResponse);
        ivQuestion = findViewById(R.id.ivQuestion);
        etText = findViewById(R.id.etText);
        cardInput = findViewById(R.id.cardInput);
        ivProgress = findViewById(R.id.ivProgress);
        list = new ArrayList();
        listKeys = new ArrayList();

        //FirebaseAuth.getInstance().signInAnonymously();
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        new BackTask().execute();
    }

    public void addQuestion(View view) {
        TransitionManager.beginDelayedTransition(constMain);
        if (!isQuestionOpen) {
            isQuestionOpen = true;
            cardInput.setVisibility(View.VISIBLE);
            switch (view.getId()) {
                case R.id.ivResponse:
                    etText.setHint("Type your response");
                    isQuestion = false;
                    break;
                case R.id.ivQuestion:
                    etText.setHint("Type your question");
                    isQuestion = true;
                    break;
            }
        } else {
            isQuestionOpen = false;
            cardInput.setVisibility(View.GONE);
        }
    }

    public void saveToDB(View view) {
        if (!etText.getText().toString().equals("")) {
            String name = etText.getText().toString();
            key = FirebaseDatabase.getInstance().getReference().child(databaseRoot).push().getKey();

            handler.post(runnableProgress);
            final SetterQuestions setter = new SetterQuestions(key, parentKey, name, "", isQuestion);
            FirebaseDatabase.getInstance().getReference()
                    .child(databaseRoot)
                    .child(key)
                    .setValue(setter)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            etText.setText("");
                            handler.removeCallbacks(runnableProgress);
                            new Insert(AutoResponses.this).execute(setter);

                            list.add(setter);
                            listKeys.add(setter.getKey());

                            //adapterQuestions = new AdapterQuestions(list);
                            //recyclerView.setAdapter(adapterQuestions);
                        }
                    });
        } else
            Toast.makeText(getApplicationContext(), "Field should not be empty", Toast.LENGTH_SHORT).show();
    }

    private class BackTask extends AsyncTask<Void, SetterQuestions, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getQuestion(db);
            int count = cursor.getCount();

            String key, parentKey, name, link;
            boolean question;

            while (cursor.moveToNext()) {
                
                key = cursor.getString(cursor.getColumnIndex(DBContract.Questions.KEY));
                parentKey = cursor.getString(cursor.getColumnIndex(DBContract.Questions.PARENT_KEY));
                name = cursor.getString(cursor.getColumnIndex(DBContract.Questions.NAME));
                link = cursor.getString(cursor.getColumnIndex(DBContract.Questions.LINK));
                String quest = cursor.getString(cursor.getColumnIndex(DBContract.Questions.QUESTION));

                question = quest.equals("yes");

                SetterQuestions setter = new SetterQuestions(key, parentKey, name, link, question);

                publishProgress(setter);
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterQuestions... values) {
            list.add(values[0]);
            listKeys.add(values[0].getKey());
        }

        @Override
        protected void onPostExecute(Integer count) {
            if (count != 0) {
                //adapterQuestions = new AdapterQuestions(list);
                //recyclerView.setAdapter(adapterQuestions);
            }
            //firebaseLoad();
        }
    }

    private void firebaseLoad() {
        FirebaseDatabase.getInstance().getReference()
                .child(databaseRoot)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterQuestions setter = snapshot.getValue(SetterQuestions.class);

                            if (listKeys.contains(setter.getKey()))
                                continue;

                            list.add(setter);
                            new Insert(AutoResponses.this).execute(setter);
                        }
                        //adapterQuestions = new AdapterQuestions(list);
                        //recyclerView.setAdapter(adapterQuestions);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private class Insert extends AsyncTask<SetterQuestions, Void, Void> {

        Context context;

        private Insert(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(SetterQuestions... params) {
            SetterQuestions setter = params[0];

            ContentValues values = new ContentValues();
            values.put(DBContract.Questions.KEY, setter.getKey());
            values.put(DBContract.Questions.PARENT_KEY, setter.getParentKey());
            values.put(DBContract.Questions.NAME, setter.getName());
            values.put(DBContract.Questions.LINK, setter.getLink());
            values.put(DBContract.Questions.QUESTION, setter.isQuestion() ? "yes": "no");

            if (!listKeys.contains(setter.getKey()))
                db.insert(DBContract.Questions.TABLE_NAME, null, values);
            else
                db.update(DBContract.Questions.TABLE_NAME, values,
                        DBContract.Questions.KEY + "='" + setter.getKey() + "'", null);

            return null;
        }

    }



*/


























    final Runnable runnableProgress = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 80);
                flickTabs();
            } catch (Exception e){
                //
            }
        }
    };

    Toast toast;
    static int x = 0;
    static int y = 0;
    private void flickTabs() {
        TransitionManager.beginDelayedTransition(constMain);

        ivProgress.setRotation(y += 5);

        /*if (toast != null)
            toast.cancel();
        toast = Toast.makeText(getApplicationContext(), "Count: " + x++, Toast.LENGTH_SHORT);
        toast.show();*/
    }

}

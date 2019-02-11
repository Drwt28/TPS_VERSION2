package com.example.deepanshu.school_demo_2.Student;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.adapters.studentRemarkAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class StudentRemarkActivity extends AppCompatActivity {

    private FirebaseAuth user;

    private studentRemarkAdapter adapter;

    private CollectionReference collectionReference;

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_remark);

        toolbar = findViewById(R.id.remarksToolBar);

        toolbar.setTitle("Student Remarks");

        toolbar.setLogo(R.drawable.ic_description_black_24dp);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        //Setting up Refference


        progressBar =findViewById(R.id.progressBarRemark);

        recyclerView = findViewById(R.id.studentRemarkRecycylerView);



   //Setting up database
        user = FirebaseAuth.getInstance();
        collectionReference = FirebaseFirestore.getInstance()
                .collection("VSchool")
                .document(user.getCurrentUser().getEmail().toString())
        .collection("Students");

        ShowProgress();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(manager);
        collectionReference
                .orderBy(Add_Student_Activity.STUDENT_NAME_KEY)
                .get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            CancelProgress();
                            List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                            adapter = new studentRemarkAdapter(StudentRemarkActivity.this,documentSnapshots);

                            toolbar.setSubtitle("No of Students" + String.valueOf(documentSnapshots.size()));

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                        else{
                            CancelProgress();
                            task.getException();

                        }


                    }
                });





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void CancelProgress(){
        progressBar.setVisibility(View.GONE);
    }
}


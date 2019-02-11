package com.example.deepanshu.school_demo_2.Student;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.TeacherActivity.Class_Activity;
import com.example.deepanshu.school_demo_2.adapters.studentMarksAdapter;
import com.example.deepanshu.school_demo_2.adapters.studentMarksEnterAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class student_marks_activity extends AppCompatActivity {

    private Toolbar toolbar;
    private Map<String,Object> SubjectMap=new HashMap();
    private studentMarksEnterAdapter marksAdapter;
    private RecyclerView marks;
    private RecyclerView enterMarks;
    private ProgressBar progressBar;
    private List subjectNames = new ArrayList();
    private studentMarksAdapter adapter;
    private Map subjects =new HashMap();
    private List namesList =new ArrayList();
    private List fatherNamesList = new ArrayList();
    private FirebaseAuth user = FirebaseAuth.getInstance();
    private CollectionReference studentCollection;
    private String LoginId;
    private Button resultUploadButton,cancelMarkButton;
    private LinearLayout layoutMarks;

    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void cancelProgress(){
        progressBar.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marks_activity);

        toolbar=findViewById(R.id.marksToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar =findViewById(R.id.marks_uploadid);
        toolbar.setTitle("Marks Record");
        //Set up datBase Reffrences
        LoginId = user.getCurrentUser().getEmail();
        studentCollection = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId).collection("Students");
     //set up reffrences

        layoutMarks =findViewById(R.id.marksLayout);
        layoutMarks.setVisibility(View.GONE);
        resultUploadButton = findViewById(R.id.uploadmarksButton);
        cancelMarkButton = findViewById(R.id.CancelmarksButton);

        showProgress();
        marks = findViewById(R.id.recylerViewMarks);
        enterMarks=findViewById(R.id.recylerViewEnterMarks);

        LinearLayoutManager manager = new LinearLayoutManager(student_marks_activity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        marks.setLayoutManager(manager);
        LinearLayoutManager manager1 = new LinearLayoutManager(student_marks_activity.this);
                manager1.setOrientation(LinearLayoutManager.VERTICAL);
        enterMarks.setLayoutManager(manager1);


        //getting data


        studentCollection
                .orderBy(Add_Student_Activity.STUDENT_NAME_KEY)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for (DocumentSnapshot documentSnapshot:documentSnapshots){


                    namesList.add(documentSnapshot.get(Add_Student_Activity.STUDENT_NAME_KEY));
                    fatherNamesList.add(documentSnapshot.get(Add_Student_Activity.STUDENT_FATHER_NAME));
                    subjectNames = (ArrayList)documentSnapshot.get(Class_Activity.SUBJECT_NAME_KEY);
                    subjects = (Map<String,Object>)documentSnapshot.get(Class_Activity.SUBJECT_KEY);

                    }
                marksAdapter = new studentMarksEnterAdapter(resultUploadButton,layoutMarks,subjectNames,subjects);
                enterMarks.setAdapter(marksAdapter);
                marksAdapter.notifyDataSetChanged();

                    adapter = new studentMarksAdapter(layoutMarks,namesList,fatherNamesList,getApplicationContext());
                    cancelProgress();
                    marks.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                cancelProgress();
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });




        cancelMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMarks.setVisibility(View.GONE);
            }
        });

        resultUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                resultUploadButton.setEnabled(false);

           int pos= adapter.getPosition();

           studentCollection.whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,fatherNamesList.get(pos))
                   .whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,namesList.get(pos))
                   .get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {

                           if(task.isSuccessful())
                           {


                               DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);
                               DocumentReference reference = snapshot.getReference();
                               reference.update(Class_Activity.SUBJECT_KEY,marksAdapter.getSubject());

                               resultUploadButton.setEnabled(true);
                               cancelProgress();

                                marksAdapter.notifyDataSetChanged();
                                layoutMarks.setVisibility(View.GONE);

                           }
                           else{

                               resultUploadButton.setEnabled(true);
                               progressBar.setVisibility(View.GONE);
                               Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG)
                                       .show();
                           }

                       }
                   });


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parents,menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}

package com.example.deepanshu.school_demo_2.Parents;

import android.content.Intent;
import android.os.LocaleList;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.Student.Add_Student_Activity;
import com.example.deepanshu.school_demo_2.TeacherActivity.Class_Activity;
import com.example.deepanshu.school_demo_2.adapters.studentInfoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentsResult extends AppCompatActivity {

    private DocumentSnapshot studentDocument;
    private Intent intent;
    private DocumentReference teacherDocument;
    private TextView ScoreView;
    private RecyclerView recyclerView;
    private CollectionReference teacherreference;
    private String name,fatherName,LoginId;
    private studentInfoAdapter adapter;
    private List SubjectNames=new ArrayList(),SubjectMarks=new ArrayList();
    private Toolbar toolbar;
    private Map<String,Object> Subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_result);
        intent = getIntent();
        name = intent.getStringExtra(Login_Activity.STUDENT_NAME_KEY);
        fatherName = intent.getStringExtra(Login_Activity.STUDENT_FATHER_NAME_LEY);
        LoginId = intent.getStringExtra(Login_Activity.STUDENT_TEACHER_LOGIN_ID_KEY);
        teacherreference = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId).collection("Students");
        //toolbar
        toolbar = findViewById(R.id.resultToolbar);

        recyclerView = findViewById(R.id.resultRecyclerView);

        ScoreView = findViewById(R.id.TextViewPercentage);

        LinearLayoutManager manager = new LinearLayoutManager(ParentsResult.this);

        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(manager);

        teacherDocument = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId);

        teacherDocument.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        toolbar.setTitle(task.getResult().get(Class_Activity.RESULT_NAME).toString());

                    }
                });

        teacherreference.whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,fatherName)
                .whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.getDocuments().size() > 0) {
                            studentDocument = documentSnapshots.getDocuments().get(0);
                            SubjectNames = (ArrayList) studentDocument.get(Class_Activity.SUBJECT_NAME_KEY);
                            Map map = new HashMap();
                            map = (Map<String,Object>)studentDocument.get(Class_Activity.SUBJECT_KEY);
                            for(int i=0;i<SubjectNames.size();i++){
                                SubjectMarks.add(Integer.parseInt(map.get(SubjectNames.get(i)).toString()));
                            }

                            int average=0;
                            for(Object temp : SubjectMarks){

                                average = Integer.parseInt(temp.toString())+average;
                            }
                            float result = (float)average/SubjectMarks.size();



                            ScoreView.setText(String.valueOf(result)+"%");
                            //set up Adapter

                            adapter = new studentInfoAdapter(SubjectNames,SubjectMarks);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                });

    }
}

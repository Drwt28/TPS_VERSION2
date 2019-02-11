package com.example.deepanshu.school_demo_2.Student;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.TextAppearanceSpan;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deepanshu.school_demo_2.Decoration.recyclerViewItemDecoration;
import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.TeacherActivity.Class_Activity;
import com.example.deepanshu.school_demo_2.adapters.studentInfoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import  com.example.deepanshu.school_demo_2.adapters.studentListAdapter;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student_Info_Activity extends AppCompatActivity {

    private Intent intent;
    private recyclerViewItemDecoration decoration;
    private TextView Textname,Textfather,Textattendence,TextrollNo,TextNotice;
    private RecyclerView recyclerViewSubject;
    private studentInfoAdapter adapter;
    private FirebaseAuth user=FirebaseAuth.getInstance();
    private String LoginId;
    private DocumentSnapshot studentDocument;
    private String studentFather,studentName;
    private CollectionReference studentCollection;
    private List subjectName=new ArrayList<>();
    private List<Object> subjectMarks = new ArrayList();
    private Toolbar toolbar;
    private TextView TextRemark,TextSubjectScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__info_);
        intent =getIntent();
        //getting reffrences
        toolbar=findViewById(R.id.student_info_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewSubject= findViewById(R.id.recyclerViewSubjects);
        Textname=findViewById(R.id.student_name_textView);
        Textfather=findViewById(R.id.student_father_textView);
        Textattendence=findViewById(R.id.student_attendence_textView);
        TextrollNo=findViewById(R.id.student_rollNo_textView);
        TextNotice=findViewById(R.id.student_notice_textView);
        TextRemark=findViewById(R.id.student_remark_textView);
        TextSubjectScore =findViewById(R.id.student_subjectscore_textView);
//setup Recycler View
        decoration = new recyclerViewItemDecoration(1);

        recyclerViewSubject.addItemDecoration(decoration);
        recyclerViewSubject.setNestedScrollingEnabled(false);
        LinearLayoutManager manager =new LinearLayoutManager(Student_Info_Activity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSubject.setLayoutManager(manager);

       LoginId= user.getCurrentUser().getEmail();
        studentCollection=FirebaseFirestore.getInstance().collection("VSchool").document(LoginId).collection("Students");
        studentName =intent.getStringExtra(studentListAdapter.STUDENT_DOCUMENT_NAME_KEY );
        studentFather=intent.getStringExtra(studentListAdapter.STUDENT_DOCUMENT_FATHER_KEY);
       //setting toolbar
        toolbar.setTitle(studentName);
        setSupportActionBar(toolbar);

        DocumentReference teacherDocument = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId);

        teacherDocument.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            TextNotice.append(
                                    task.getResult().get(Class_Activity.TEACHER_NOTICE_KEY)
                                    .toString()
                            );
                        }
                    }
                });

        studentCollection.whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,studentFather)
                .whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,studentName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.getDocuments().size() > 0) {
                            studentDocument = documentSnapshots.getDocuments().get(0);
                            subjectName = (ArrayList) studentDocument.get(Class_Activity.SUBJECT_NAME_KEY);
                            Map map = new HashMap();
                            map = (Map<String,Object>)studentDocument.get(Class_Activity.SUBJECT_KEY);
                            for(int i=0;i<subjectName.size();i++){
                                subjectMarks.add(Integer.parseInt(map.get(subjectName.get(i)).toString()));
                            }

                            int average=0;
                            for(Object temp : subjectMarks){

                                average = Integer.parseInt(temp.toString())+average;
                            }
                            float result = (float)average/subjectMarks.size();

                            TextSubjectScore.append(String.valueOf(result)+"%  out of 100");
                            Textname.setText("");
                            Textfather.append(studentDocument.get(Add_Student_Activity.STUDENT_FATHER_NAME).toString());
                            TextrollNo.append(studentDocument.get(Add_Student_Activity.STUDENT_ROLL_KEY).toString());
                            Textattendence.append(
                                    studentDocument.get(Add_Student_Activity.STUDENT_ATTENDENCE_KEY).toString()
                                    +"\n Attendence Date :" + studentDocument.get(Add_Student_Activity.STUDENT_ATTENDENCE_TIME_KEY)
                                            .toString().substring(0,17)
                            );
                            TextRemark.append(studentDocument.get(Add_Student_Activity.STUDENT_REMARK_KEY).toString());
                            //set up Adapter


                            adapter = new studentInfoAdapter(subjectName,subjectMarks);
                            recyclerViewSubject.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),subjectMarks.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
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
}

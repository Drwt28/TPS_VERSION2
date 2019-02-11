package com.example.deepanshu.school_demo_2.Student;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.TeacherActivity.Class_Activity;
import com.example.deepanshu.school_demo_2.adapters.studenceAttendenceAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class student_attendence_activity extends AppCompatActivity {

    private CollectionReference studentsCollection;
    private FirebaseAuth user = FirebaseAuth.getInstance();
    private DocumentSnapshot studentDocument;
    private RecyclerView recyclerViewAttendence;
    private Toolbar toolbar;
    private DocumentReference teacherRefrences;
    private int position=0;
    int size=0;
    private int totalAttendence;
    private Date attendenceDate;
    private AlertDialog progressDialog;
    private AlertDialog.Builder builder;
    private QuerySnapshot documentStudentSnapshots;
    private  studenceAttendenceAdapter adapter;
    private Button takeAttendenceBtn;
    private ProgressBar progressBar;
    private List studentNameList=new ArrayList();
    private List studentRollNo =new ArrayList();
    private String LoginId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendence_activity);


        //Setting Up Reffrences
        progressBar = findViewById(R.id.attendenceProgressBar);
        toolbar =findViewById(R.id.attendeceToolbar);
        takeAttendenceBtn=findViewById(R.id.takeAttendenceButton);
        recyclerViewAttendence=findViewById(R.id.attendenceRecyclerView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress();

        //setting up Databases
        LoginId= user.getCurrentUser().getEmail();

        studentsCollection = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId).collection("Students");

        teacherRefrences = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId);
        //Setting up recyclerView





        studentsCollection
                .orderBy(Add_Student_Activity.STUDENT_NAME_KEY)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                for(DocumentSnapshot studentDocument : documentSnapshots)
                {
                    studentNameList.add(studentDocument.get(Add_Student_Activity.STUDENT_NAME_KEY));
                    studentRollNo.add(studentDocument.get(Add_Student_Activity.STUDENT_ROLL_KEY));
                }

                toolbar.setSubtitle("\t No of Students\n\t"+String.valueOf(studentNameList.size()));

                adapter = new studenceAttendenceAdapter(studentNameList,studentRollNo);
                recyclerViewAttendence.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(student_attendence_activity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewAttendence.setLayoutManager(layoutManager);

                cancelProgress();

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                cancelProgress();
            }
        });
        takeAttendenceBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Uploadattendence();

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

    public void Uploadattendence() {

        size = 0;

        final List<Boolean> attendenceList;
        attendenceList = adapter.getAttendenceList();
        if (attendenceList.size() > 0) {
            for(int i=0;i<studentNameList.size();i++)
            {
                if(attendenceList.get(i))
                {
                    size=size+1;
                }

            }

            studentsCollection
                    .orderBy(Add_Student_Activity.STUDENT_NAME_KEY)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful()){

                                for(int pos=0;pos <task.getResult().size();pos++)
                                {
                                    if(attendenceList.get(pos))
                                    {
                                        DocumentReference reference = task.getResult().getDocuments()
                                                .get(pos).getReference();
                                        ArrayList<Date> list = (ArrayList)task.getResult().getDocuments().get(pos).get(Add_Student_Activity.STUDENT_PRESENT_DATE_LISTKEY);
                                       list.add(new Date());


                                        int a = (Integer.parseInt( task.getResult()
                                            .getDocuments().get(pos).get(Add_Student_Activity.STUDENT_ATTENDENCE_KEY).toString()));
                                        a=a+1;

                                        reference.update(Add_Student_Activity.STUDENT_PRESENT_DATE_LISTKEY,list);
                                        reference.update(Add_Student_Activity.STUDENT_ATTENDENCE_KEY,a);
                                        reference.update(Add_Student_Activity.STUDENT_ATTENDENCE_TIME_KEY,FieldValue.serverTimestamp());

                                    }else{

                                        DocumentReference documentReference = task.getResult().getDocuments()
                                                .get(pos).getReference();
                                        ArrayList<Date> list = (ArrayList) task.getResult().getDocuments().get(pos).get(Add_Student_Activity.STUDENT_ABSENT_DATE_LISTKEY);
                                      list.add(Calendar.getInstance().getTime());
                                      documentReference.update(Add_Student_Activity.STUDENT_ABSENT_DATE_LISTKEY,list);
                                    }
                                }
                            }
                        }

                    });


            teacherRefrences.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int b = Integer.parseInt(documentSnapshot.get(Class_Activity.TEACHER_ATTENDENCE_KEY)
                            .toString());
                    b = b + 1;


                    documentSnapshot.getReference()
                            .update(Class_Activity.STUDENT_KEY, size);
                    documentSnapshot.getReference()
                            .update(Class_Activity.TEACHER_ATTENDENCE_KEY, b);
                    documentSnapshot.getReference()
                            .update(Class_Activity.TEACHER_ATTENDENCE_DATE_KEY, new Date());

                    takeAttendenceBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    takeAttendenceBtn.setEnabled(true);
                    cancelProgress();

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            });

        }


    }
    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void cancelProgress(){
        progressBar.setVisibility(View.GONE);
    }



}

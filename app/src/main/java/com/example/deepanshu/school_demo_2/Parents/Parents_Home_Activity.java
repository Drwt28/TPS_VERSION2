package com.example.deepanshu.school_demo_2.Parents;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.deepanshu.school_demo_2.BackGroundNotification;
import com.example.deepanshu.school_demo_2.Decoration.recyclerViewItemDecoration;
import com.example.deepanshu.school_demo_2.MainActivity;
import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.Student.Add_Student_Activity;
import com.example.deepanshu.school_demo_2.TeacherActivity.Class_Activity;
import com.example.deepanshu.school_demo_2.adapters.studentInfoAdapter;
import com.example.deepanshu.school_demo_2.adapters.studentMarksAdapter;
import com.example.deepanshu.school_demo_2.app;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.deepanshu.school_demo_2.app.CHANNEL_ID_1;

public class Parents_Home_Activity extends AppCompatActivity {

    private String teacherNotice;
    private Intent intent;
    private Intent serviceIntent;
    private int TeacherAttendenceFlag, StudentAttendenceFlag;
    private Toolbar toolbar;
    private TextView FatherName,Dob,Remark,Notice,TeacherName,Attendence;
    private String teacherName, StudentName;
    private Button viewResult,viewAttendence,viewFee;
    private RecyclerView subjectListview;
    private DocumentReference studentDocument = null, teacherDocument;
    private CollectionReference teacherRefrence;
    private ArrayList subjectName = new ArrayList();
    private Notification notification;
    private Intent AttendenceIntent,ResultIntent;
    private NotificationManagerCompat notificationManager;
    private ArrayList subjectMarks = new ArrayList();
    private String LoginId, name, fatherName;
    private Button AttendencViewBtn,ResultViewBtn;
    public static final String REMARK_KEY = "remarkkey";
    public static final String NOTICE_KEY = "noticeKey";

    private SharedPreferences notificationdataPrefrences;
    private studentInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents__home_);

        //Setting Up reffrences
        TeacherName = findViewById(R.id.TextView_TeacherName);
        Remark = findViewById(R.id.TextView_Remark);
        Attendence = findViewById(R.id.TextView_Attendence);
        Notice  = findViewById(R.id.TextView_Notice);
        FatherName = findViewById(R.id.TextView_Father_Name);
        Dob = findViewById(R.id.TextView_DOB);
        toolbar = findViewById(R.id.studentToolbar);
        AttendencViewBtn = findViewById(R.id.AttendenceView);
        ResultViewBtn = findViewById(R.id.ResultViewButton);
        //getting data From past Activities
        notificationManager = NotificationManagerCompat.from(this);
        notificationdataPrefrences = this.getSharedPreferences("Notification", MODE_PRIVATE);
        serviceIntent = new Intent(Parents_Home_Activity.this, BackGroundNotification.class);

        intent = getIntent();
        AttendenceIntent = new Intent(Parents_Home_Activity.this,ParentAttendence.class);
        ResultIntent = new Intent(Parents_Home_Activity.this,ParentsResult.class);

        LoginId=intent.getStringExtra(Login_Activity.STUDENT_TEACHER_LOGIN_ID_KEY);
        name = intent.getStringExtra(Login_Activity.STUDENT_NAME_KEY);
        fatherName=intent.getStringExtra(Login_Activity.STUDENT_FATHER_NAME_LEY);
        //SetUp Attendence Activity
        AttendenceIntent.putExtra(Login_Activity.STUDENT_FATHER_NAME_LEY,fatherName);
        AttendenceIntent.putExtra(Login_Activity.STUDENT_NAME_KEY,name);
        AttendenceIntent.putExtra(Login_Activity.STUDENT_TEACHER_LOGIN_ID_KEY,LoginId);
        //Setup Result Activity
        ResultIntent.putExtra(Login_Activity.STUDENT_FATHER_NAME_LEY,fatherName);
        ResultIntent.putExtra(Login_Activity.STUDENT_NAME_KEY,name);
        ResultIntent.putExtra(Login_Activity.STUDENT_TEACHER_LOGIN_ID_KEY,LoginId);
        //Setup Result Activity
        teacherRefrence = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId).collection("Students");
        teacherDocument = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId);


        setSupportActionBar(toolbar);
        toolbar.setTitle(name);


        teacherRefrence.whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,name)
                .whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,fatherName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()&&task.getResult().getDocuments().size()>0)
                        {
                           DocumentSnapshot snapshot =  task.getResult().getDocuments().get(0);

                           studentDocument = snapshot.getReference();
                           String dob = snapshot.get(Add_Student_Activity.STUDENT_DATEOFBIRTH_KEY).toString();

                           Dob.setText("Date Of Birth: "+dob);

                           String r=snapshot.get(Add_Student_Activity.STUDENT_REMARK_KEY).toString()
                          ;
                           Remark.setText(" Diary: "+r);

                           sendNotification(3,"School Diary",r);

                           FatherName.setText("Father's Name :"+fatherName);

                           String a= snapshot.get(Add_Student_Activity.STUDENT_ATTENDENCE_KEY).toString()
                                   ;
                           Attendence.setText("Total Attendence:"+a);

                           ArrayList<Date> list = (ArrayList<Date>) snapshot.get(Add_Student_Activity.STUDENT_ABSENT_DATE_LISTKEY);

                           Date d = Calendar.getInstance().getTime();
                           for(int i =0 ; i <list.size();i++)
                           {
                               if(list.get(i).toString().substring(0,6)==d.toString().substring(0,6))
                               {
                                   sendNotification(5,"Attendence","Your student is absent Today");
                               }
                           }

                           String roll = snapshot.get(Add_Student_Activity.STUDENT_ROLL_KEY).toString();


                        }
                    }
                });

        BackGroundNotification.setReferences(teacherDocument,studentDocument);

        startService(serviceIntent);
        teacherDocument.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot snapshot=task.getResult();

                           toolbar.setSubtitle( snapshot.get(Class_Activity.CLASS_NAME_KEY).toString());

                           Notice.setText("-Notice-\n"+
                          snapshot.get( Class_Activity.TEACHER_NOTICE_KEY).toString());

                           sendNotification(1,"Teacher Notice",snapshot.get(Class_Activity.TEACHER_NOTICE_KEY).toString());
                           String tn = snapshot.get(Class_Activity.NAME_KEY).toString();
                           TeacherName.setText("Class Teacher: "+tn);
                        } }
                });

        AttendencViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AttendenceIntent);
            }
        });

        ResultViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ResultIntent);
            }
        });

    }


    public void DownloadData()
    {

        teacherRefrence.whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,name)
                .whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,fatherName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()&&task.getResult().getDocuments().size()>0)
                        {
                            DocumentSnapshot snapshot =  task.getResult().getDocuments().get(0);

                            studentDocument = snapshot.getReference();
                            String dob = snapshot.get(Add_Student_Activity.STUDENT_DATEOFBIRTH_KEY).toString();

                            Dob.setText("Date Of Birth: "+dob);

                            String r=snapshot.get(Add_Student_Activity.STUDENT_REMARK_KEY).toString()
                                    ;
                            Remark.setText(" Diary: "+r);

                            FatherName.setText("Father's Name :"+fatherName);

                            String a= snapshot.get(Add_Student_Activity.STUDENT_ATTENDENCE_KEY).toString()
                                    ;
                            Attendence.setText("Total Attendence:"+a);

                            String roll = snapshot.get(Add_Student_Activity.STUDENT_ROLL_KEY).toString();


                        }
                    }
                });

        BackGroundNotification.setReferences(teacherDocument,studentDocument);

        startService(serviceIntent);
        teacherDocument.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot snapshot=task.getResult();

                            toolbar.setSubtitle( snapshot.get(Class_Activity.CLASS_NAME_KEY).toString());

                            Notice.setText("-Notice-\n"+
                                    snapshot.get( Class_Activity.TEACHER_NOTICE_KEY).toString());

                            String tn = snapshot.get(Class_Activity.NAME_KEY).toString();
                            TeacherName.setText("Class Teacher: "+tn);
                        } }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id==R.id.LogOutId)
        {
            Login_Activity.preferences.edit().clear().apply();
            MainActivity.datapref.edit().clear().apply();
            finish();
            startActivity(new Intent(Parents_Home_Activity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.parents_menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {



        teacherDocument.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                documentSnapshot.get(Class_Activity.TEACHER_ATTENDENCE_KEY);
                String temp = documentSnapshot.get(Class_Activity.TEACHER_NOTICE_KEY).toString();
                sendNotification(1,"Teacher Notice",temp);
                DownloadData();
            }
        });
        if(studentDocument!=null) {

            studentDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                    Date presentDate = new Date();
                    ArrayList<Date> absentlist =
                            (ArrayList<Date>) documentSnapshot.get(Add_Student_Activity.STUDENT_ABSENT_DATE_LISTKEY);

                    for (int i = 0; i < absentlist.size(); i++) {
                        if (absentlist.get(i) == presentDate) {
                            sendNotification(5, "Attendence","your child is absent in the class");
                        }
                    }
                    String r = String.valueOf(documentSnapshot.get(Add_Student_Activity.STUDENT_REMARK_KEY));
                    sendNotification(3, "School Diary",r);

                }
            });
        }
        super.onStart();
    }
    public void sendNotification(int id,String title,String data)
    {
       Notification notification =  new NotificationCompat.Builder(this, app.CHANNEL_ID_1 )
               .setSmallIcon(R.drawable.ic_announcement_black_24dp)
               .setContentTitle(title)
               .setContentText(data)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setCategory(NotificationCompat.CATEGORY_ERROR)
               .build();

       notificationManager.notify(id,notification);

    }
}


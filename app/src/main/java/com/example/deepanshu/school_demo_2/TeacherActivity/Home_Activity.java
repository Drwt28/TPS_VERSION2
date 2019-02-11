package com.example.deepanshu.school_demo_2.TeacherActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.MainActivity;
import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.Student.Add_Student_Activity;
import com.example.deepanshu.school_demo_2.Student.StudentRemarkActivity;
import com.example.deepanshu.school_demo_2.Student.Student_list;
import com.example.deepanshu.school_demo_2.Student.student_attendence_activity;
import com.example.deepanshu.school_demo_2.Student.student_marks_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Home_Activity extends AppCompatActivity {
//Collection Name = School/Teacher_id/Students;
    private FirebaseAuth userAuth=FirebaseAuth.getInstance();
    private DocumentReference documentReference;
    private TextView titleTextView;
    private Boolean decide = false;
    private Button NoticeBtn,sendNoticeBtn,cancelNoticeBtn,remarksButton,startMarksActivityBtn;
    private EditText NoticeText,ResultNameEditText;
    private Button cancelMarksActivityBtn;
    private CardView NoticeLayout;
    private RelativeLayout AddStudentBtn,MarksBtn,AttendenceBtn,studentListBtn;
    private Toolbar toolbar;
    private String loginId;
    private CardView ResultNameDialog;
    private LinearLayout layoutTop,layoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        toolbar = findViewById(R.id.actionbar_id);
        layoutTop = findViewById(R.id.homelayoutTopId);
        layoutBottom = findViewById(R.id.homelayoutBottomId);

        titleTextView = findViewById(R.id.textview_title);
        setSupportActionBar(toolbar);
        AttendenceBtn = findViewById(R.id.Button_Attendence);
        remarksButton=findViewById(R.id.teacherRemarkButton);
        AddStudentBtn = findViewById(R.id.Button_Add);
        studentListBtn = findViewById(R.id.Button_List);
        MarksBtn = findViewById(R.id.Button_Marks);
        NoticeBtn = findViewById(R.id.teacherNoticeButton);
        ResultNameDialog=findViewById(R.id.ResultNameDialog);
        startMarksActivityBtn = findViewById(R.id.EnterMarksActivityBtn);
        cancelMarksActivityBtn = findViewById(R.id.CancelMarksActivityBtn);
        ResultNameEditText = findViewById(R.id.ResultNameEditText);
        //Setting  up Notice Layout

        NoticeLayout = findViewById(R.id.teacherNoticeLayout);
        NoticeText = findViewById(R.id.teacherNoticeEditText);
        sendNoticeBtn = findViewById(R.id.sendNoticeBtn);
        cancelNoticeBtn = findViewById(R.id.CancelNoticeBtn);

        if (userAuth.getCurrentUser().getEmail().toString() != null) {

            loginId = userAuth.getCurrentUser().getEmail().toString();
        }
        documentReference = FirebaseFirestore.getInstance().collection("VSchool")
                .document(loginId);

        UpdateData();
        AddStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Activity.this, Add_Student_Activity.class));
            }
        });
        studentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Activity.this, Student_list.class));
            }
        });
        AttendenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Activity.this, student_attendence_activity.class));

            }
        });
        MarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResultNameDialog.setVisibility(View.VISIBLE);


            }
        });
        startMarksActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r=ResultNameEditText.getText().toString().trim();

                if(!r.isEmpty())
                {
                    documentReference.update(Class_Activity.RESULT_NAME,r);
                    ResultNameDialog.setVisibility(View.GONE);
                    startActivity(new Intent(Home_Activity.this, student_marks_activity.class));
                }else{

                    ResultNameEditText.setError("Enter Exam Name");
                    ResultNameEditText.setText("");
                }
            }
        });

        cancelMarksActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultNameDialog.setVisibility(View.GONE);
            }
        });


        remarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Home_Activity.this,StudentRemarkActivity.class));

            }
        });


//        Working Notice Board

        NoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!decide) {
                    NoticeLayout.setVisibility(View.VISIBLE);
                    NoticeLayout.animate().alphaBy(1.0f).setDuration(1400);
                    decide = true;
                    layoutTop.setVisibility(View.GONE);
                    layoutBottom.setVisibility(View.GONE);


                } else {
                    layoutTop.setVisibility(View.VISIBLE);
                    layoutBottom.setVisibility(View.VISIBLE);

                }
            }
        });

        sendNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String text= NoticeText.getText().toString().trim();

               if(text!=null){

                   documentReference.update(Class_Activity.TEACHER_NOTICE_KEY,text).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           if(task.isSuccessful()){
                               if(decide) {
                                   NoticeLayout.animate().alphaBy(0.0f).setDuration(1000);
                                   NoticeLayout.setVisibility(View.GONE);
                                   decide = false;
                                   layoutTop.setVisibility(View.VISIBLE);
                                   layoutBottom.setVisibility(View.VISIBLE);
                               }
                               Toast.makeText(getApplicationContext(),"Succesfully",Toast.LENGTH_LONG)
                                       .show();
                           }
                           else{
                               layoutBottom.setVisibility(View.VISIBLE);
                               layoutTop.setVisibility(View.VISIBLE);
                           }

                       }
                   });
               }
            }
        });

        cancelNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(decide) {
                    NoticeLayout.animate().alphaBy(0.0f).setDuration(1000);
                    NoticeLayout.setVisibility(View.GONE);
                    decide = false;
                    layoutTop.setVisibility(View.VISIBLE);
                    layoutBottom.setVisibility(View.VISIBLE);

                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.log_out_menu){
             userAuth.signOut();
             Toast.makeText(getApplicationContext(),"Succesfully Log Out",Toast.LENGTH_LONG).show();
             finish();
             Class_Activity.sharedPreferences.edit().clear().apply();
             MainActivity.datapref.edit().clear().apply();
             startActivity(new Intent(Home_Activity.this, MainActivity.class));
             return true;
        }else if(id == R.id.notification_menu ) {

            Toast.makeText(getApplicationContext(),"No Notifications",Toast.LENGTH_LONG).show();
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        UpdateData();

    }
    public void UpdateData()
    {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map<String,Object> data = new HashMap<>();
                data= documentSnapshot.getData();

                String display="Teacher :"+data.get(Class_Activity.NAME_KEY).toString()
                        +"\n\nLast Attendence\t:"+data.get(Class_Activity.TEACHER_ATTENDENCE_DATE_KEY).toString().substring(0,9)
                        + "\n\nTotal no of Students  \t:"+data.get(Class_Activity.TOTAL_STUDENT_KEY)
                        +"\n\nPresent student \t:" + data.get(Class_Activity.STUDENT_KEY)
                        +"\n\nNotice\t:"+data.get(Class_Activity.TEACHER_NOTICE_KEY)
                        ;
                titleTextView.setText(display);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });



    }
}

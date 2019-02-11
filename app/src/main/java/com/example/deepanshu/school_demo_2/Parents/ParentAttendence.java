package com.example.deepanshu.school_demo_2.Parents;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.Student.Add_Student_Activity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ParentAttendence extends AppCompatActivity

{
    private Toolbar toolbar;
    private Boolean decide =false;
    private Intent intent;
    private String name,fatherName,LoginId;
    private Button PresentBtn,AbsentBtn;
    private ArrayList<Date> PresentList,AbsentList;
    private CompactCalendarView calendarView;
    private CollectionReference teacherReference;


    public String getMonthName(int m)
    {
        String month="";
        switch (m) {
            case 0: {
                month= "January";
                break;

            }
            case 1: {
                month= "Feb";
                        break;

            }
            case 2: {
                month= "Mar";
                break;

            }
            case 3: {
                month = "Apr";
                        break;


            }
            case 4: {
                month = "June";
                break;

            }
            default:
            {
                break;
            }
        }
        return month;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_attendence);

        intent = getIntent();
        name = intent.getStringExtra(Login_Activity.STUDENT_NAME_KEY);
        fatherName = intent.getStringExtra(Login_Activity.STUDENT_FATHER_NAME_LEY);
        LoginId = intent.getStringExtra(Login_Activity.STUDENT_TEACHER_LOGIN_ID_KEY);

        //Setting up refrences
        PresentBtn = findViewById(R.id.PresentBtn);
        AbsentBtn  = findViewById(R.id.AbsentBtn);
        toolbar = findViewById(R.id.toolbarId);
        calendarView = findViewById(R.id.compactcalendar_view);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(getMonthName(Calendar.getInstance().get(Calendar.MONTH)));
        toolbar.setSubtitle(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        teacherReference = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId).collection("Students");

        teacherReference.whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,name)
                .whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,fatherName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()&&task.getResult().size()>0) {

                            PresentList = (ArrayList<Date>) task.getResult().getDocuments().get(0).get(Add_Student_Activity.STUDENT_PRESENT_DATE_LISTKEY);
                            AbsentList = (ArrayList<Date>) task.getResult().getDocuments().get(0)
                                    .get(Add_Student_Activity.STUDENT_ABSENT_DATE_LISTKEY);

                            Date d = new Date();
                            List<Event> events = new ArrayList<>();
                            for (int i = 0; i < PresentList.size(); i++) {
                                d = PresentList.get(i);
                                events.add(new Event(Color.GREEN, d.getTime()));

                            }
                            calendarView.addEvents(events);
                        }else{

                            PresentList = new ArrayList<Date>();
                            AbsentList  =  new ArrayList<Date>();
                        }
                    }
                });

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                toolbar.setTitle(firstDayOfNewMonth.toString().substring(4,8));

            }
        });


        PresentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.removeAllEvents();
                Date present=null;
                for(int i =0 ; i < PresentList.size();i++) {
                    present = PresentList.get(i);
                    Event e = new Event(Color.GREEN, present.getTime());
                    calendarView.addEvent(e);
                }

            }
        });

        AbsentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            calendarView.removeAllEvents();
                Date d=new Date() ;
                for(int i =0 ; i < AbsentList.size();i++)
                {
                    d = AbsentList.get(i);
                    Event e = new Event(Color.RED, d.getTime());
                    calendarView.addEvent(e);
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

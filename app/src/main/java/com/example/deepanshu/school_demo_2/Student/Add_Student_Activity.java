package com.example.deepanshu.school_demo_2.Student;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.TeacherActivity.Class_Activity;
import com.example.deepanshu.school_demo_2.TeacherActivity.Home_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class Add_Student_Activity extends AppCompatActivity {
    private Button addStudentBtn;
    private ProgressBar progressBar;
    private int decideStudent = 0;
    public static final String STUDENT_PRESENT_DATE_LISTKEY="present";
    public static final String STUDENT_ABSENT_DATE_LISTKEY="absent";
    public static final String STUDENT_LEAVE_DATE_LISTKEY="leave";
    public static final String STUDENT_NAME_KEY="Name";
    public static final String STUDENT_FATHER_NAME="Father's Name";
    public static final String STUDENT_ROLL_KEY="Roll No";
    public static final String STUDENT_REMARK_KEY="Remarks";
    public static final String STUDENT_FEE_INFO="Fee";
    public static final String STUDENT_CLASS_KEY="Class Name ";
    public static final String STUDENT_ATTENDENCE_KEY="No Of Attendence";
    public static final String STUDENT_ATTENDENCE_TIME_KEY=" Attendence Time";
    public static final String  STUDENT_NOTICE_KEY="Notice";
    public static final String STUDENT_DATEOFBIRTH_KEY="Date Of Birth";
    private String sName,sFather,SRollNo;
    private TextView DateOfBirth ;
    private EditText studentName,studentRollNo,studentFather;
    private Map<String,Object> Subjects=new HashMap<>();

    private Map<String,Object> studentDataDocument;
    private String loginId;
    private ArrayList<String> SubjectNames = new ArrayList<>();
    private FirebaseAuth user = FirebaseAuth.getInstance();
    private CollectionReference studentCollection;
    private DocumentReference studentDocument;
    private DocumentReference teacherDocument;
    private DatePickerDialog.OnDateSetListener mDateSetListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__student_);

        //set up refrences
        DateOfBirth = findViewById(R.id.dateOfBirthView);
        studentName = findViewById(R.id.student_name);
        studentFather = findViewById(R.id.student_fatherName);
        studentRollNo = findViewById(R.id.student_rollNo);
        addStudentBtn = findViewById(R.id.student_add);

        progressBar = findViewById(R.id.progress_add_student);
        loginId = user.getCurrentUser().getEmail().toString();

        //Setup database References
        studentCollection = FirebaseFirestore.getInstance().collection("VSchool").document(loginId)
                .collection("Students");
        teacherDocument = FirebaseFirestore.getInstance().collection("VSchool").document(loginId);

        //getting Student Credentials


        //getting Subjects
        teacherDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> data = new HashMap<>();

                data = documentSnapshot.getData();
                Subjects = (Map<String, Object>) data.get(Class_Activity.SUBJECT_KEY);
                SubjectNames = (ArrayList<String>) data.get(Class_Activity.SUBJECT_NAME_KEY);
            }
        });
        DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);





                DatePickerDialog dialog = new DatePickerDialog(
                        Add_Student_Activity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        mDateSetListner,
                        year,month,day);
                dialog.show();
            }
        });
        mDateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String date = String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+
                        String.valueOf(year);
                DateOfBirth.setText(date);
            }
        };
        //uploading student buttton

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                 sName = studentName.getText().toString().trim().toUpperCase();
                sFather = studentFather.getText().toString().trim().toUpperCase();
                String date = DateOfBirth.getText().toString().trim();
                String sRoll = studentRollNo.getText().toString().trim();


                if (!sName.isEmpty() && !sFather.isEmpty()&&!sRoll.isEmpty()&&!date.isEmpty()) {

                    studentCollection.whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,sFather)

                            .whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,sName)

                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        decideStudent = task.getResult().size();
                                    }
                                    else

                                        {

                                        decideStudent=0;
                                    }
                                }
                            });

                   if(decideStudent>0){

                       new AlertDialog.Builder(Add_Student_Activity.this)
                               .setTitle("Unable to add student")
                               .setMessage(sName+" is already exist in the class")
                               .setPositiveButton("ok",null)
                               .show();
                       progressBar.setVisibility(View.GONE);
                   }
                   else {


                       studentDocument = studentCollection.document(sName.toUpperCase()+sFather.toUpperCase());

                       studentDataDocument = new HashMap<String, Object>();
                       studentDataDocument.put(Class_Activity.SUBJECT_KEY, Subjects);
                       studentDataDocument.put(Class_Activity.SUBJECT_NAME_KEY, SubjectNames);
                       studentDataDocument.put(Add_Student_Activity.STUDENT_NAME_KEY, sName);
                       studentDataDocument.put(STUDENT_FATHER_NAME, sFather);
                       studentDataDocument.put(STUDENT_ROLL_KEY, sRoll);
                       studentDataDocument.put(STUDENT_REMARK_KEY, "");
                       studentDataDocument.put(STUDENT_NOTICE_KEY, "");
                       studentDataDocument.put(STUDENT_ATTENDENCE_KEY, 0);
                       studentDataDocument.put(STUDENT_DATEOFBIRTH_KEY,date);
                       studentDataDocument.put(STUDENT_CLASS_KEY, loginId);
                       studentDataDocument.put(STUDENT_PRESENT_DATE_LISTKEY,new ArrayList<Date>());
                       studentDataDocument.put(STUDENT_ABSENT_DATE_LISTKEY,new ArrayList<Date>());
                       studentDataDocument.put(STUDENT_LEAVE_DATE_LISTKEY,new ArrayList<Date>());
                       studentDataDocument.put(STUDENT_ATTENDENCE_TIME_KEY, FieldValue.serverTimestamp());

                       studentDocument.set(studentDataDocument).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               progressBar.setVisibility(View.INVISIBLE);
                               new AlertDialog.Builder(Add_Student_Activity.this)
                                       .setTitle("Succesfully added a new Student")
                                       .setMessage(sFather + "\n" + sName + "\n" + SubjectNames.toString())
                                       .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               startActivity(new Intent(Add_Student_Activity.this, Home_Activity.class));
                                           }
                                       }).setNegativeButton("Add Another", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       studentName.setText("");
                                       studentFather.setText("");
                                       studentRollNo.setText("");
                                   }
                               }).show();


                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               progressBar.setVisibility(View.GONE);
                               Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                           }
                       });
                   }
                }
                else{

                    progressBar.setVisibility(View.GONE);
                    studentFather.setError("Enter Father Name");
                    studentName.setError("Enter Name");
                    studentRollNo.setError("Enter Roll No");
                    studentName.setText("");
                    studentFather.setText("");
                    studentRollNo.setText("");
                }
            }
        });

        }
}

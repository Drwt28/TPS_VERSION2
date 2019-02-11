package com.example.deepanshu.school_demo_2.TeacherActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Class_Activity extends AppCompatActivity {

    Map subjectList = new HashMap<String,Object>();
   private FirebaseFirestore db = FirebaseFirestore.getInstance();
   public static final String NAME_KEY="name";
   private ArrayList<String> SubjectNames=new ArrayList<>();
   public static final String STUDENT_KEY="student";
   public static SharedPreferences sharedPreferences;
   public static final String SUBJECT_NAME_KEY="Sname";
   public static final String SUBJECT_KEY ="subject";
   public static final String CLASS_NAME_KEY="Class";
   public static final String RESULT_NAME="result";
   private static final String CLASS_LOGIN_DECIDE_KEY="classLoginDecide";
   public static final String TOTAL_STUDENT_KEY="total Students";
   public static final String TEACHER_ATTENDENCE_KEY="totol attendence";
   public static final String TEACHER_ATTENDENCE_DATE_KEY="Date Of Attendence";
   public static final String TEACHER_NOTICE_KEY="Notice key";
   private ProgressBar progressBar;
   private Map<String,Object> data = new HashMap<>();
   private FirebaseAuth user = FirebaseAuth.getInstance();
   private DocumentReference ClassDocument;
   private ScrollView mScrollView;
   private  EditText teacherName,noOfStudent;
   private Spinner ClassNameSpinner ;
   public void showProgress(){
       progressBar.setVisibility(View.VISIBLE);
       mScrollView.setVisibility(View.GONE);
   }
   public void cancelProgress(){
       progressBar.setVisibility(View.GONE);
       mScrollView.setVisibility(View.VISIBLE);

   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_);
        progressBar = findViewById(R.id.progressBar);
        teacherName = findViewById(R.id.edit_text_TecherName);
        ClassNameSpinner = findViewById(R.id.spinnerClassName);
        noOfStudent = findViewById(R.id.edit_text_Students_list);
        mScrollView = findViewById(R.id.class_scrollView_id);
        FirebaseUser Teacher  = user.getCurrentUser();
        String loginId= Teacher.getEmail().toString();
        ClassDocument =FirebaseFirestore.getInstance().collection("VSchool").document(loginId);

        showProgress();

        new CountDownTimer(5000,1000){
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                cancelProgress();

            }
        }.start();

        //enable auto Mode
      sharedPreferences = this.getSharedPreferences("decide",MODE_PRIVATE);
        if(sharedPreferences.getBoolean(CLASS_LOGIN_DECIDE_KEY,false)){
           startActivity(new Intent(Class_Activity.this,Home_Activity.class));

        }

        ClassDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    sharedPreferences.edit().putBoolean(CLASS_LOGIN_DECIDE_KEY,true).apply();
                    startActivity(new Intent(Class_Activity.this,Home_Activity.class));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                cancelProgress();
            }
        });


    }



    public void dataUpload(View v) {


        String n, className;
        String c ;
        c= noOfStudent.getText().toString().trim();
        n = teacherName.getText().toString().trim();
        className = ClassNameSpinner.getSelectedItem().toString();
        progressBar.setVisibility(View.VISIBLE);
        data.put(NAME_KEY, n);
        data.put(CLASS_NAME_KEY, className);
        data.put(SUBJECT_KEY, subjectList);
        data.put(SUBJECT_NAME_KEY, SubjectNames);
        data.put(TEACHER_ATTENDENCE_KEY, 0);
        data.put(RESULT_NAME,"");
        data.put(TOTAL_STUDENT_KEY,c);
        data.put(TEACHER_ATTENDENCE_DATE_KEY, FieldValue.serverTimestamp());
        data.put(TEACHER_NOTICE_KEY, "");

        if (!n.isEmpty()&&!className.isEmpty()&&!c.isEmpty())
        {
            ClassDocument.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Succesfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Class_Activity.this, Home_Activity.class));
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else{
            progressBar.setVisibility(View.GONE);
            teacherName.setError("Enter Name");
            teacherName.setText("");
        }
    }
    public void SubjectChecked(View v){

            CheckBox subjectSelected = (CheckBox) v;
            if (subjectSelected.isChecked()) {
                String temp= subjectSelected.getText().toString();
                SubjectNames.add(temp.toUpperCase());
                subjectList.put(subjectSelected.getText().toString().toUpperCase(), 0);
            }


    }

}

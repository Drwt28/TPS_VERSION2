package com.example.deepanshu.school_demo_2.Parents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.Student.Add_Student_Activity;
import com.example.deepanshu.school_demo_2.TeacherActivity.Class_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Login_Activity extends AppCompatActivity {
    private DocumentReference studentreffrence;
    private FirebaseAuth user;
    public  static SharedPreferences preferences;
    private CollectionReference collectionReference;
    public static final String [] LOGIN_ID = {"class_one@tpsshastri.com","class_two@tpsshahstri.com","class_three@tpsshahstri.com"};
    private EditText nameText,fatherNameText;
    private Button loginParentsBtn;
    private Spinner loginIdSelector;
    private Toolbar toolbar;
    private String loginId;
    private CountDownTimer timer;
    private ProgressBar progressBar;
    private TextInputLayout fatherInput,nameInput;
    private Intent intent;
    public static final String SHARED_PREFRENCES_PARENTS_LOGIN_KEY="keyparents";
    private Boolean decide =false;
    public static final String STUDENT_NAME_KEY="namekey";
    public static final String STUDENT_FATHER_NAME_LEY="fathernamekey";
    public static final String STUDENT_TEACHER_LOGIN_ID_KEY="LoginIdkey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        intent = new Intent(Login_Activity.this,Parents_Home_Activity.class);
        preferences = this.getSharedPreferences("userData",MODE_PRIVATE);

        String n = preferences.getString("name",null);
        String f=preferences.getString("fname",null);
        String lid=preferences.getString("login",null);
        Boolean d = preferences.getBoolean("parent",false);

        if(n!=null && f!=null && lid !=null && d==true){

            intent.putExtra(Login_Activity.STUDENT_NAME_KEY, n);
            intent.putExtra(Login_Activity.STUDENT_FATHER_NAME_LEY, f);
            intent.putExtra(Login_Activity.STUDENT_TEACHER_LOGIN_ID_KEY,lid);
            startActivity(intent);


        }

        timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                {

                    new AlertDialog.Builder(Login_Activity.this)
                            .setTitle("Message")
                            .setMessage("Enter your Class and Check ur Internet Connection")
                            .setPositiveButton("Ok",null).show();
                    fatherInput.setError("Enter  Name");
                    nameInput.setError("Enter Valid Name");
                    nameText.setText("");
                    fatherNameText.setText("");
                    loginParentsBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);

                }

            }
        };

        user = FirebaseAuth.getInstance();
        //setting Up reffrences
        progressBar = findViewById(R.id.loginProgressBar);
        fatherInput = findViewById(R.id.father_input_layout);
        nameInput=findViewById(R.id.name_input_layout);
        toolbar = findViewById(R.id.parentsToolbar);
        nameText = findViewById(R.id.editTextStudentName);
        fatherNameText=findViewById(R.id.editTextStudentFatherName);
        loginIdSelector=findViewById(R.id.LoginSpinnerText);
        loginParentsBtn = findViewById(R.id.loginParentButton);


        setSupportActionBar(toolbar);
        //stting up Database


        Toast.makeText(getApplicationContext(),loginId,Toast.LENGTH_SHORT).show();

        loginParentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginId = loginIdSelector.getSelectedItem().toString().trim();
//
//                if (loginId.equalsIgnoreCase("Class 7A"))
//                {
//
//                    loginId = "class7a@tpsshastri.com";
//
//                }
//                if (loginId.equalsIgnoreCase("Class 7B")) {
//
//                    loginId = "class7b@tpsshastri.com";
//
//                }
//                if (loginId.equalsIgnoreCase("Class 7C"))
//                {
//                    loginId = "class7c@tpsshastri.com";
//                }
//
//                if (loginId.equalsIgnoreCase("Class 8A"))
//                {
//
//                    loginId = "class8a@tpsshastri.com";
//
//                }
//                if (loginId.equalsIgnoreCase("Class 8B"))
//                {
//
//                    loginId = "class8b@tpsshastri.com";
//
//                }
//                if (loginId.equalsIgnoreCase("Class 8C"))
//                {
//                    loginId = "class8c@tpsshastri.com";
//                }
//                if (loginId.equalsIgnoreCase("Class 8D"))
//                {
//                    loginId = "class8d@tpsshastri.com";
//                }


                //tps vaishali id
                if (loginId.equalsIgnoreCase("Class 7A"))
                {

                    loginId = "class7a@tpsvaishali.com";

                }
                if (loginId.equalsIgnoreCase("Class 7B")) {

                    loginId = "class7b@tpsvaishali.com";

                }
                if (loginId.equalsIgnoreCase("Class 7C"))
                {
                    loginId = "class7c@tpsvaishali.com";
                }

                if (loginId.equalsIgnoreCase("Class 8A"))
                {

                    loginId = "class8a@tpsvaishali.com";

                }
                if (loginId.equalsIgnoreCase("Class 8B"))
                {

                    loginId = "class8b@tpsvaishali.com";

                }
                if (loginId.equalsIgnoreCase("Class 8C"))
                {
                    loginId = "class8c@tpsvaishali.com";
                }
                if (loginId.equalsIgnoreCase("Class 8D"))
                {
                    loginId = "class8d@tpsvaishali.com";
                }
                progressBar.setVisibility(View.VISIBLE);
                loginParentsBtn.setEnabled(false);
                String n = nameText.getText().toString().toUpperCase().trim();
                String fn = fatherNameText.getText().toString().toUpperCase().trim();

                    CollectionReference studentrefrence = FirebaseFirestore.getInstance()
                            .collection("VSchool").document(loginId).collection("Students");
                    DocumentReference reference = FirebaseFirestore.getInstance()
                            .collection("VSchool").document(loginId);

                    if (!n.isEmpty() && !fn.isEmpty() && !loginId.isEmpty()) {

                        timer.start();

                        studentrefrence.whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY, n)
                                .whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME, fn)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isSuccessful()){



                                            for (DocumentSnapshot snapshot : task.getResult()) {

                                                timer.cancel();
                                                String a = snapshot.get(Add_Student_Activity.STUDENT_NAME_KEY).toString();
                                                String b = snapshot.get(Add_Student_Activity.STUDENT_FATHER_NAME).toString();
                                                Toast.makeText(getApplicationContext(), a + b, Toast.LENGTH_LONG)
                                                        .show();
                                                intent.putExtra(Login_Activity.STUDENT_NAME_KEY, a);
                                                intent.putExtra(Login_Activity.STUDENT_FATHER_NAME_LEY, b);
                                                intent.putExtra(Login_Activity.STUDENT_TEACHER_LOGIN_ID_KEY,loginId);

                                                preferences.edit().putBoolean("parent",true).apply();
                                                preferences.edit().putString("login",loginId).apply();
                                                preferences.edit().putString("name",a).apply();
                                                preferences.edit().putString("fname",b).apply();


                                                loginParentsBtn.setEnabled(true);
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(intent);

                                            }
                                            }else{



                                            timer.cancel();
                                            task.getException().getMessage();
                                            Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG)
                                                    .show();
                                            loginParentsBtn.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);

                                        }

                                        }


                                });


                    } else {

                        timer.cancel();
                        fatherInput.setError("Enter  Name");
                        nameInput.setError("Enter Valid Name");
                        nameText.setText("");
                        fatherNameText.setText("");
                        loginParentsBtn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);

                    }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

      Boolean decide= preferences.getBoolean("parent",false);
        if(decide){

            startActivity(intent);
        }

    }
}

package com.example.deepanshu.school_demo_2.TeacherActivity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Teacher_Login extends AppCompatActivity {

    private AlertDialog dialog;
    private EditText passwordText,loginText;
    private TextInputLayout passwordLayout;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth user = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__login);
        loginText = (EditText)findViewById(R.id.login_id);
        loginBtn = findViewById(R.id.login_btn);
        passwordLayout = (TextInputLayout)findViewById(R.id.password_layout_id);
        passwordText = (EditText)findViewById(R.id.login_password);
        progressBar = findViewById(R.id.progressBar2);
        passwordLayout.setCounterMaxLength(8);


    loginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            String loginId = loginText.getText().toString().trim();
            final String password = passwordText.getText().toString().trim();
            if (!loginId.isEmpty() && !password.isEmpty()) {

                dialog = new AlertDialog.Builder(Teacher_Login.this)
                        .setTitle("Your Screen is Loading ")
                        .setMessage("Please Wait.....")
                        .setPositiveButton("Ok",null).show();





                user.signInWithEmailAndPassword(loginId, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                dialog.show();
                                showProgress();
                                startActivity(new Intent(Teacher_Login.this,Class_Activity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialog.cancel();
                                passwordText.setError(" ");
                                    loginText.setError("");
                                    new AlertDialog.Builder(Teacher_Login.this)
                                            .setTitle("Unable to login")
                                            .setIcon(R.drawable.ic_error_black_24dp)
                                            .setMessage("Enter right login id and check your Password")
                                            .setPositiveButton("ok",null)
                                            .show();
                                    cancelProgress();

                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                            }
                        });
            }else{

                loginText.setError("Enter Id");
                passwordText.setError("Enter Password");
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    });



    }

    public void showProgress(){

        progressBar.setVisibility(View.VISIBLE);
    }
    public void cancelProgress()
    {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(user.getCurrentUser()!=null){
            startActivity(new Intent(Teacher_Login.this,Home_Activity.class));
        }
    }


}

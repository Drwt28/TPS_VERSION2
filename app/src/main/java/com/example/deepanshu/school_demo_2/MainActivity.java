package com.example.deepanshu.school_demo_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.deepanshu.school_demo_2.Parents.Login_Activity;
import com.example.deepanshu.school_demo_2.TeacherActivity.Teacher_Login;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private ImageView splashLogo;
    private TextView splashtext;
    private ProgressBar progressBar;
    private Button parentBtn,teacherBtn;
    public static SharedPreferences datapref;
    private Boolean isTeacherStarted=false;
    private Boolean isParentStarted=false;
    private RelativeLayout SplashScreen;
    public static final String TEACHER_KEY="keyuser";
    public static final String PARENTS_KEY="parentsskeyuser";
    private SharedPreferences sharedPreferences;
   private Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentBtn =findViewById(R.id.parentBtn);
        teacherBtn = findViewById(R.id.teacherBtn);
        toolbar = findViewById(R.id.main_toolbar);

        splashLogo=findViewById(R.id.imageLogoId);
        splashtext=findViewById(R.id.textSplash);
        SplashScreen =findViewById(R.id.spalahLayout_id);

        datapref = this.getPreferences(MODE_PRIVATE);
        setSupportActionBar(toolbar);



        if(datapref.getBoolean(PARENTS_KEY,false)){

            finish();
            startActivity(new Intent(MainActivity.this,Login_Activity.class));
        }
        else{
            startSplashScreen();
        }
        if(datapref.getBoolean(TEACHER_KEY,false)){
            finish();
            startActivity(new Intent(MainActivity.this,Teacher_Login.class));
        }
        else{
            startSplashScreen();
        }


        teacherBtn.animate().alpha(1.0f).setDuration(1500);
        parentBtn.animate().alpha(1.0f).setDuration(1000);
        teacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isTeacherStarted =true;


                datapref.edit().putBoolean(TEACHER_KEY,true).apply();
                startActivity(new Intent(MainActivity.this,Teacher_Login.class));
            }
        });
        parentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                datapref.edit().putBoolean(PARENTS_KEY,true).apply();
                startActivity(new Intent(MainActivity.this, Login_Activity.class));

            }
        });


    }
    public void startSplashScreen(){

        splashtext.setAlpha(0.0f);
        splashLogo.setTranslationY(200);
        splashLogo.setScaleY(2.0f);
        splashLogo.setScaleX(2.0f);
       parentBtn.setVisibility(View.GONE);
       teacherBtn.setVisibility(View.GONE);

        splashLogo.animate().scaleX(1.0f).scaleY(1.0f).translationY(-200).setDuration(3000);
        splashtext.animate().alpha(1.0f).setDuration(3000);
        new CountDownTimer(5000,1000){
            @Override
            public void onFinish() {
                parentBtn.setVisibility(View.VISIBLE);
                teacherBtn.setVisibility(View.VISIBLE);

                SplashScreen.setVisibility(View.GONE);
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }


    //method Slowing Click so Avoided By Drawat
//    public void teacher_Select(View v){
//
//        Button view = (Button)v;
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(MainActivity.this,Teacher_Login.class);
//                startActivity(intent);
//            }
//        });
//    }
}

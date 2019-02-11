package com.example.deepanshu.school_demo_2.Student;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.deepanshu.school_demo_2.Decoration.recyclerViewItemDecoration;
import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.adapters.studentListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class Student_list extends AppCompatActivity {

    private studentListAdapter adapter;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ArrayList<Object> studentNamelist = new ArrayList<>();
    private ArrayList<Object> studentfatherlist = new ArrayList<>();
    private ArrayList<Object> studentRollNoList=new ArrayList<>();
    private RecyclerView studentListView;
    private CollectionReference studentCollection;
    private FirebaseAuth user = FirebaseAuth.getInstance();
    private List studentDocumentList = new ArrayList();


    public void showProgress(){

        progressBar.setVisibility(View.VISIBLE);
    }
    public void cancelProgress(){

        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        //getting Teacher Details
        String LoginId = user.getCurrentUser().getEmail().toString();
        studentCollection = FirebaseFirestore.getInstance().collection("VSchool").document(LoginId)
                .collection("Students");


            progressBar = findViewById(R.id.studentListProgressBarId);
            toolbar = findViewById(R.id.student_list_actionbar);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setLogo(R.drawable.student_marks);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            showProgress();
            studentListView = findViewById(R.id.recyclerView_student_list);


            studentCollection
                    .orderBy(Add_Student_Activity.STUDENT_ROLL_KEY)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {

                    studentDocumentList = documentSnapshots.getDocuments();
                    for (int i = 0; i < studentDocumentList.size(); i++) {
                        DocumentSnapshot data = (DocumentSnapshot) studentDocumentList.get(i);
                        studentNamelist.add(data.get(Add_Student_Activity.STUDENT_NAME_KEY));
                        studentfatherlist.add(data.get(Add_Student_Activity.STUDENT_FATHER_NAME));
                        studentRollNoList.add(data.get(Add_Student_Activity.STUDENT_ROLL_KEY));
                        }
                    //setting Recickler View
                    LinearLayoutManager manager = new LinearLayoutManager(Student_list.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    studentListView.setLayoutManager(manager);
                    adapter = new studentListAdapter(studentRollNoList,studentNamelist, studentfatherlist,Student_list.this);
                    studentListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    studentListView.addItemDecoration(new recyclerViewItemDecoration(8));
                    toolbar.setSubtitle("No of Students :" + studentNamelist.size());

                   cancelProgress();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cancelProgress();

                }
            });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
                finish();}

        return super.onOptionsItemSelected(item);
    }

}
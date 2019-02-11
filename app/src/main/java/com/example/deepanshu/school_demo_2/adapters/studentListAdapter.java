package com.example.deepanshu.school_demo_2.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.Student.Add_Student_Activity;
import com.example.deepanshu.school_demo_2.Student.Student_Info_Activity;
import com.example.deepanshu.school_demo_2.Student.Student_list;
import com.example.deepanshu.school_demo_2.viewHolders.studentListViewHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class studentListAdapter extends RecyclerView.Adapter<studentListViewHolder> {

    private CollectionReference studentCollection;
    private FirebaseAuth user=FirebaseAuth.getInstance();
    private String LoginId;
    private Context context;
    private Intent studentInfointent ;
    public static final String STUDENT_DOCUMENT_NAME_KEY="studentinfoDocument";
    public static final String STUDENT_DOCUMENT_FATHER_KEY="studentDocumentFather";
    private DocumentSnapshot studentDocument;
    private List<String> studentNameList=new ArrayList();
    private List<String> studentFatherList=new ArrayList<>();
    private List<String> studentRollList = new ArrayList<>();


    public studentListAdapter(List studentRollList,List studentNameList,List studentFatherList,Context context){
        this.studentNameList = studentNameList;
        this.studentFatherList=studentFatherList;
        this.context = context;
        this.studentRollList=studentRollList;
     LoginId=   user.getCurrentUser().getEmail();
        studentCollection = FirebaseFirestore.getInstance().collection("VSchool")
                .document(LoginId).collection("Students");

    }
    @NonNull

    @Override
    public studentListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_list_vew,viewGroup,false);

    return new studentListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull studentListViewHolder Holder, int i) {


        Holder.updateUI(studentRollList.get(i),studentNameList.get(i));

        final int position = i;
        Holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               HandleClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentNameList.size();
    }

public void HandleClick(int i){

    final int position = i;
    studentCollection.whereEqualTo(Add_Student_Activity.STUDENT_NAME_KEY,studentNameList.get(position))
            .whereEqualTo(Add_Student_Activity.STUDENT_FATHER_NAME,studentFatherList.get(position))
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    studentDocument = documentSnapshots
                            .getDocuments()
                            .get(0);
                    studentInfointent = new Intent(context, Student_Info_Activity.class);
                    studentInfointent
                            .putExtra(STUDENT_DOCUMENT_NAME_KEY,studentDocument.getString(Add_Student_Activity.STUDENT_NAME_KEY));
                    studentInfointent
                            .putExtra(STUDENT_DOCUMENT_FATHER_KEY,studentDocument.getString(Add_Student_Activity.STUDENT_FATHER_NAME));
                    context.startActivity(studentInfointent);
                    }


            });


}
}

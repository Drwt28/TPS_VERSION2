package com.example.deepanshu.school_demo_2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.Student.Add_Student_Activity;
import com.example.deepanshu.school_demo_2.viewHolders.studentRemarkViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class studentRemarkAdapter extends RecyclerView.Adapter<studentRemarkViewHolder> {

    private List<String> studentName= new ArrayList<>();

    private List<String> studentRollNo = new ArrayList<>();

    private List<DocumentSnapshot> snapshots ;

    private Context context;

    public studentRemarkAdapter(Context context ,List<DocumentSnapshot> documentSnapshots)
    {
        this.context = context;
        this.snapshots = documentSnapshots;
        if(documentSnapshots.size()>0) {
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {

                studentName.add(documentSnapshot.get(Add_Student_Activity.STUDENT_NAME_KEY).toString());

                studentRollNo.add(documentSnapshot.get(Add_Student_Activity.STUDENT_ROLL_KEY).toString());
            }
        }

    }

    @NonNull
    @Override
    public studentRemarkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_remark_view,viewGroup,false);

        return new studentRemarkViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final studentRemarkViewHolder Holder, int pos) {

        Holder.updateUi(studentName.get(pos),studentRollNo.get(pos));

        final int i =pos;

        Holder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              final String text= Holder.remarkEditText.getText().toString();

               if(!text.isEmpty())
               {
                   snapshots.get(i).getReference().update(Add_Student_Activity.STUDENT_REMARK_KEY,text)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {

                                  if(task.isSuccessful()){

                                      new AlertDialog.Builder(context)
                                              .setTitle("Successfully Sent ")
                                              .setMessage(text)
                                              .setPositiveButton("ok",null)
                                              .show();
                                      Holder.remarkEditText.setText("");

                                  }
                                  else{

                                      new AlertDialog.Builder(context)
                                              .setTitle("Error")
                                              .setMessage(task.getException().toString())
                                              .setPositiveButton("ok",null)
                                              .show();
                                  }
                               }
                           });
               }
               else {

                   Holder.remarkEditText.setError("Enter Remark");

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return snapshots.size();
    }
}

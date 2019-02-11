package com.example.deepanshu.school_demo_2.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.deepanshu.school_demo_2.R;

public class studentInfoViewHolder extends RecyclerView.ViewHolder {

    private ProgressBar progressBar;
    private TextView textView;

    public studentInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.textViewSubjectName);
       progressBar= itemView.findViewById(R.id.progressSubjectMarks);
    }
    public void updateUI(String subjectName,int subjectMarks){
        textView.setText(subjectName+"\t\t:"+String.valueOf(subjectMarks));
        progressBar.setProgress(subjectMarks);

    }
}

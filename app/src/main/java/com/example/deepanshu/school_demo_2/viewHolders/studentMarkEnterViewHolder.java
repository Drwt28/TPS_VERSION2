package com.example.deepanshu.school_demo_2.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.deepanshu.school_demo_2.R;

public class studentMarkEnterViewHolder extends RecyclerView.ViewHolder {

  private TextView subjectText;
  public EditText subjectNumber;

    public studentMarkEnterViewHolder(@NonNull View itemView) {
        super(itemView);

        subjectText  = (TextView) itemView.findViewById(R.id.SubjectNameTextView);
        subjectNumber= (EditText) itemView.findViewById(R.id.EditTextStudentMark);

    }

    public void UpdateUi(String pos,String subjectName){

        subjectText.setText(pos+" :"+subjectName);
    }
}

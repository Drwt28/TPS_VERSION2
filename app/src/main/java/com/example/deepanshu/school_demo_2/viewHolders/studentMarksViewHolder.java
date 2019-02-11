package com.example.deepanshu.school_demo_2.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.deepanshu.school_demo_2.R;

public class studentMarksViewHolder extends RecyclerView.ViewHolder{


    private TextView nameView,fNameView;
    private Button EnterMarksButton;
    public studentMarksViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView =itemView.findViewById(R.id.textViewStudentName);
        fNameView=itemView.findViewById(R.id.textViewStudentFather);
        EnterMarksButton =itemView.findViewById(R.id.buttonEnterMarks);
    }
    public void UpdateUi(int position,String name,String fName){

        nameView.setText("\t"+String.valueOf(position)+"\t"+name);
        fNameView.setText(fName);


    }
}

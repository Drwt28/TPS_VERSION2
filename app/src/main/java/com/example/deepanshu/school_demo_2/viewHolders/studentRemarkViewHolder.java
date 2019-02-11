package com.example.deepanshu.school_demo_2.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.deepanshu.school_demo_2.R;

public class studentRemarkViewHolder extends RecyclerView.ViewHolder {

    public EditText remarkEditText;
    private TextView studentNameText;
    public Button sendButton;

    public studentRemarkViewHolder(@NonNull View itemView) {
        super(itemView);
        remarkEditText = itemView.findViewById(R.id.editTextRemark);

        studentNameText = itemView.findViewById(R.id.textViewNameRemark);

        sendButton = itemView.findViewById(R.id.send_remark_button);

    }

    public void updateUi(String name,String rollNo){

        studentNameText.setText("\t"+rollNo+"\t"+name);

        remarkEditText.setHint("Enter remark for "+ name);

    }

}

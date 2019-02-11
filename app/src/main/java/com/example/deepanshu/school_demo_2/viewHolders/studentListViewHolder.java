package com.example.deepanshu.school_demo_2.viewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.deepanshu.school_demo_2.R;

public class studentListViewHolder extends RecyclerView.ViewHolder {

    private TextView NameTextView,SerialNo;
    public studentListViewHolder(@NonNull View itemView) {
        super(itemView);
        NameTextView= (TextView)itemView.findViewById(R.id.cardViewTextTitle);
        SerialNo = itemView.findViewById(R.id.cardViewTextViewSerialNo);
    }
    public void updateUI(String sNo,String title){
      NameTextView.setText(title);
      SerialNo.setText(sNo);
    }
}

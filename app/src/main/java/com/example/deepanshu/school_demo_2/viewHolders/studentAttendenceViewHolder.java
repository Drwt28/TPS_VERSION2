package com.example.deepanshu.school_demo_2.viewHolders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.R;

import java.util.ArrayList;
import java.util.List;

public class studentAttendenceViewHolder extends RecyclerView.ViewHolder {


    private Boolean decide = false;
    public Button attendenceButton;
    public TextView attendenceNameText;

    public studentAttendenceViewHolder(@NonNull View itemView) {
        super(itemView);
        attendenceButton = itemView.findViewById(R.id.attendenceStampButton);
        attendenceNameText = itemView.findViewById(R.id.attendencetextview);

    }

    public void UpdateUi(String name) {

        attendenceNameText.setText(name);
    }

    public Boolean isAttendenceUpdate() {

        if (attendenceButton.getText().toString().toUpperCase().trim().equals("A")) {

            attendenceButton.setText("P");
            attendenceButton.setTextColor(Color.BLUE);
            return true;
        }
        if (attendenceButton.getText().toString().toUpperCase().trim().equals("P")) {

            attendenceButton.setTextColor(Color.RED);
            attendenceButton.setText("A");
            return false;

        }
        return false;

    }
}




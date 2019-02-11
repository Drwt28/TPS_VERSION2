package com.example.deepanshu.school_demo_2.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.viewHolders.studentMarksViewHolder;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class studentMarksAdapter extends RecyclerView.Adapter<studentMarksViewHolder> {

    private List namesList = new ArrayList<>();
    private List fatherNamesList = new ArrayList();
    private studentMarksViewHolder holder;
    private LinearLayout linearLayout;
    private QuerySnapshot documentSnapshots;
    private Context context;
    private Intent intent;
    private int position;


    public studentMarksAdapter( LinearLayout layout, List namesList, List fatherNamesList , Context context) {
        this.namesList = namesList;
        this.linearLayout = layout;
        this.fatherNamesList = fatherNamesList;
        this.context=context;

    }

    @NonNull
    @Override
    public studentMarksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_marks_view,viewGroup,false);

        return new studentMarksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull studentMarksViewHolder Holder, int i) {

        final int j = i;


        holder = Holder;
        holder.UpdateUi(i+1,String.valueOf(namesList.get(i)),String.valueOf(fatherNamesList.get(i)));
        Holder.itemView.findViewById(R.id.buttonEnterMarks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           linearLayout.setVisibility(View.VISIBLE);
           linearLayout.setScaleY(0);
           linearLayout.animate().scaleY(1).setDuration(1000);

                position=j;
            }
        });



    }
    public int getPosition()
    {
        return this.position;
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }
}

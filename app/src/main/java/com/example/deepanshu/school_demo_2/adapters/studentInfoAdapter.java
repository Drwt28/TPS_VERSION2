package com.example.deepanshu.school_demo_2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.viewHolders.studentInfoViewHolder;

import java.util.ArrayList;
import java.util.List;

public class studentInfoAdapter extends RecyclerView.Adapter<studentInfoViewHolder> {
    private List subjectNames=new ArrayList();
    private List<Object> subjectMarks=new ArrayList();

    public studentInfoAdapter(List subjectNames, List subjectMarks) {
        this.subjectNames = subjectNames;
        this.subjectMarks = subjectMarks;
    }

    @NonNull
    @Override
    public studentInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_subject_view,viewGroup,false);

        return new studentInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull studentInfoViewHolder Holder, int i) {

        Holder.updateUI(subjectNames.get(i).toString(),Integer.parseInt((subjectMarks.get(i)).toString()));

    }

    @Override
    public int getItemCount() {
        return subjectNames.size();
    }
}

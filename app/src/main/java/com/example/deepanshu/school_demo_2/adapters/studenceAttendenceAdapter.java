package com.example.deepanshu.school_demo_2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.viewHolders.studentAttendenceViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class studenceAttendenceAdapter extends RecyclerView.Adapter<studentAttendenceViewHolder> {

    private List namesList ;
    private List rollList ;
    private List<Boolean> attendenceList;
    private int position=-1;
    private Context context;

    private studentAttendenceViewHolder holder;
    public studenceAttendenceAdapter(List namesList,List rollList)
    {
        this.namesList = namesList;
        this.rollList = rollList;

        attendenceList = new ArrayList<>();

        for(int i=0;i<namesList.size();i++)
        {
          attendenceList.add(i,true);
        }
    }



    @NonNull
    @Override
    public studentAttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_attendece_view,viewGroup,false);

        return new studentAttendenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull studentAttendenceViewHolder Holder, int i)


    {

        String temp = rollList.get(i)+"\t"+namesList.get(i);
        Holder.UpdateUi(temp);
       final studentAttendenceViewHolder  holder = Holder;
        final int position=i;

       Holder.attendenceButton.setOnClickListener(new View.OnClickListener()
       {
            @Override
            public void onClick(View v) {
                Boolean decide =  holder.isAttendenceUpdate();
               attendenceList.add(position,decide);
            }
        });
    }


    @Override
    public int getItemCount() {

        return namesList.size();
    }

    public List getAttendenceList()
    {
        return this.attendenceList;
    }
}


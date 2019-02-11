package com.example.deepanshu.school_demo_2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.deepanshu.school_demo_2.R;
import com.example.deepanshu.school_demo_2.viewHolders.studentMarkEnterViewHolder;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class studentMarksEnterAdapter extends RecyclerView.Adapter<studentMarkEnterViewHolder> {

   private Button resultUploadBtn;
   private LinearLayout layout;
   private Map<String,Object> subject = new HashMap();
   private List subjectNames = new ArrayList();

    public studentMarksEnterAdapter(Button resultUploadBtn,LinearLayout layout, List subjectNames,Map<String,Object> subject) {

        this.subject = subject;
        this.layout = layout;
        this.resultUploadBtn = resultUploadBtn;
        this.subjectNames = subjectNames;
    }

    @NonNull
    @Override
    public studentMarkEnterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.student_enter_marks_view,viewGroup,false);

        return  new studentMarkEnterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull studentMarkEnterViewHolder Holder, int pos) {
        Holder.UpdateUi(String.valueOf(pos+1),String.valueOf(subjectNames.get(pos)));

        final int position = pos;
        Holder.subjectNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int marks = 0;
                String temp=s.toString();
                if (!temp.isEmpty()) {
                    if (s.toString().equalsIgnoreCase("100")) {

                        marks = 100;
                    } else {
                        if (s.toString().length() > 2) {

                            marks = Integer.parseInt(s.toString().substring(0, 2));
                        } else {

                            marks = Integer.parseInt(s.toString());
                        }


                    }

                    subject.put(subjectNames.get(position).toString(), marks);
                }
            }
        });

    }

public Map<String,Object> getSubject()
{
    return this.subject;
}
    @Override
    public int getItemCount() {
        return subjectNames.size();
    }
}

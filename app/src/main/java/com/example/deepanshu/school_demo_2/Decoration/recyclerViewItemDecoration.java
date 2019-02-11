package com.example.deepanshu.school_demo_2.Decoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class recyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    int spacer;

    public recyclerViewItemDecoration(int spacer) {
        this.spacer = spacer;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(2,spacer,2,0);
    }
}

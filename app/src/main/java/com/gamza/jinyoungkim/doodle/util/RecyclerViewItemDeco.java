package com.gamza.jinyoungkim.doodle.util;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewItemDeco extends RecyclerView.ItemDecoration{
    int spanCount;
    float spacing;
    boolean includeEdge;

    public RecyclerViewItemDeco(int spanCount, float spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int spanIndex = ((GridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanIndex();

        if(includeEdge){
            outRect.left = (int)spacing;
            outRect.right = (int)(spacing/spanCount);
        }else{
            outRect.left = (int)(spanIndex/spanCount);
            outRect.right = (int)spacing;
        }

        if(position<spanCount){
            outRect.top = (int)spacing;
        }
        outRect.bottom = (int)spacing;
    }
}

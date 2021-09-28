package io.praveen.typenote.SQLite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    @Nullable
    private ClickListener clickListener;
    @Nullable
    private GestureDetector gestureDetector;

    @Override // android.support.v7.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    @Override // android.support.v7.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public RecyclerTouchListener(Context context, @Nullable ClickListener clickListener2) {
        this.clickListener = clickListener2;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            /* class io.praveen.typenote.SQLite.RecyclerTouchListener.AnonymousClass1 */

            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.OnItemTouchListener
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (this.gestureDetector == null || findChildViewUnder == null || this.clickListener == null || !this.gestureDetector.onTouchEvent(motionEvent)) {
            return false;
        }
        this.clickListener.onClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder));
        return false;
    }
}

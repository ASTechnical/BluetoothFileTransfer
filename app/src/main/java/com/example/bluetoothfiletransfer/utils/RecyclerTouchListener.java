package com.example.bluetoothfiletransfer.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private ClickListener clickListener;
    private GestureDetector gestureDetector;

    public interface ClickListener {
        void onClick(View view, int i);

        void onLongClick(View view, int i);
    }

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener2) {
        this.clickListener = clickListener2;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            public void onLongPress(MotionEvent motionEvent) {
                ClickListener clickListener;
                View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (findChildViewUnder != null && (clickListener = clickListener2) != null) {
                    clickListener.onLongClick(findChildViewUnder, recyclerView.getChildAdapterPosition(findChildViewUnder));
                }
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null || this.clickListener == null || !this.gestureDetector.onTouchEvent(motionEvent)) {
            return false;
        }
        this.clickListener.onClick(findChildViewUnder, recyclerView.getChildAdapterPosition(findChildViewUnder));
        return false;
    }
}

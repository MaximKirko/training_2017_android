package com.github.maximkirko.training_2017_android.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

public abstract class CursorRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Cursor cursor;

    public CursorRecyclerAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if(position != 0) {
            cursor.moveToPosition(position - 1);
        }
        onBindViewHolder(holder, cursor);
    }

    public abstract void onBindViewHolder(VH holder, Cursor cursor);

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
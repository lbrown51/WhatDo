package com.ad340.whatdo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TagListDialog extends Dialog {

    RecyclerView tagRecycler;
    RecyclerView.Adapter<TagRecyclerAdapter.TagItemViewHolder> adapter;
    Activity activity;

    public TagListDialog(Activity activity, RecyclerView.Adapter<TagRecyclerAdapter.TagItemViewHolder> adapter) {
        super(activity);
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.all_tags_dialog);
        tagRecycler = findViewById(R.id.tag_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        tagRecycler.setLayoutManager(mLayoutManager);
        tagRecycler.setAdapter(adapter);
    }

}

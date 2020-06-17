package com.ad340.whatdo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.util.ArrayList;

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.TagItemViewHolder> {

    private static final String TAG = TagRecyclerAdapter.class.getSimpleName();
    private ArrayList<String> tags;
    private TagListener tagListener;
    private int requestCode;
    private TextView tagText;
    private Todo todo;

    TagRecyclerAdapter(ArrayList<String> tags, int requestCode, @Nullable TextView tagText, @Nullable Todo todo, TagListener tagListener) {
        this.tags = tags;
        this.tagText = tagText;
        this.todo = todo;
        this.requestCode = requestCode;
        this.tagListener = tagListener;
    }

    @NonNull
    @Override
    public TagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: vh create");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        TagItemViewHolder vh = new TagItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TagItemViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + tags);
        if (tags != null && position < tags.size()) {
            String tag = tags.get(position);
            holder.tagTitle.setText(tag);
            holder.tagTitle.setOnClickListener(v -> {
                try {
                    tagListener.tagListSelect(tag, requestCode, tagText, todo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    static class TagItemViewHolder extends RecyclerView.ViewHolder {
        TextView tagTitle;
        TagItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "TagItemViewHolder: ");
            tagTitle = itemView.findViewById(R.id.tag_item_title);

        }
    }

    void setTags(ArrayList<String> tags) {
        Log.d(TAG, "setTags: ");
        this.tags = new ArrayList<>();
        this.tags = tags;
        notifyDataSetChanged();
    }

}

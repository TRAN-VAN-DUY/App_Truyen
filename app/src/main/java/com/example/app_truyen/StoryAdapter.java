package com.example.app_truyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private final List<StoryItem> storyItems;

    public StoryAdapter(List<StoryItem> storyItems) {
        this.storyItems = storyItems;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_card, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        StoryItem item = storyItems.get(position);
        holder.title.setText(item.title);
        holder.chapter.setText(item.chapter);
    }

    @Override
    public int getItemCount() {
        return storyItems.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView chapter;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textStoryTitle);
            chapter = itemView.findViewById(R.id.textStoryChapter);
        }
    }
}

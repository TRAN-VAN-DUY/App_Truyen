package com.example.app_truyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopStoryAdapter extends RecyclerView.Adapter<TopStoryAdapter.TopStoryViewHolder> {

    private static final int VIRTUAL_ITEM_COUNT = 10000;
    private final List<StoryItem> storyItems;

    public TopStoryAdapter(List<StoryItem> storyItems) {
        this.storyItems = storyItems;
    }

    @NonNull
    @Override
    public TopStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_top_story, parent, false);
        return new TopStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStoryViewHolder holder, int position) {
        if (storyItems.isEmpty()) {
            holder.title.setText("");
            return;
        }
        StoryItem item = storyItems.get(position % storyItems.size());
        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        if (storyItems.isEmpty()) {
            return 0;
        }
        return VIRTUAL_ITEM_COUNT;
    }

    static class TopStoryViewHolder extends RecyclerView.ViewHolder {
        final TextView title;

        TopStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTopStoryTitle);
        }
    }
}

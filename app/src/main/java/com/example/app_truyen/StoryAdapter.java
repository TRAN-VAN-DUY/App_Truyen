package com.example.app_truyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private final List<StoryItem> stories = new ArrayList<>();

    public void submitList(List<StoryItem> items) {
        stories.clear();
        stories.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_card, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        StoryItem item = stories.get(position);
        holder.title.setText(item.getTitle());

        bindLine(holder.tag, "Tag", item.getCategory());
        bindLine(holder.chapter, "Chapter", item.getChapter());
        bindLine(holder.author, "Tác giả", item.getAuthor());
        bindLine(holder.status, "Trạng thái", item.getStatus());
        bindLine(holder.score, "Đánh giá", item.getScore());
        bindLine(holder.views, "Số lượt đọc", item.getViews());

        holder.favorite.setText(item.isFavorite() ? "❤" : "♡");
        holder.favorite.setTextColor(holder.itemView.getContext().getColor(
                item.isFavorite() ? R.color.footer_active : R.color.search_heart_inactive));
    }

    private void bindLine(TextView view, String label, String value) {
        if (value == null || value.trim().isEmpty()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(label + " : " + value);
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView favorite;
        final TextView tag;
        final TextView chapter;
        final TextView author;
        final TextView status;
        final TextView score;
        final TextView views;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textStoryTitle);
            favorite = itemView.findViewById(R.id.textStoryFavorite);
            tag = itemView.findViewById(R.id.textStoryTag);
            chapter = itemView.findViewById(R.id.textStoryChapter);
            author = itemView.findViewById(R.id.textStoryAuthor);
            status = itemView.findViewById(R.id.textStoryStatus);
            score = itemView.findViewById(R.id.textStoryScore);
            views = itemView.findViewById(R.id.textStoryViews);
        }
    }
}

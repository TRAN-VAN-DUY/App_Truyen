package com.example.app_truyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchStoryAdapter extends RecyclerView.Adapter<SearchStoryAdapter.SearchStoryViewHolder> {

    private final List<StoryItem> storyItems;

    public SearchStoryAdapter(List<StoryItem> storyItems) {
        this.storyItems = storyItems;
    }

    @NonNull
    @Override
    public SearchStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_story_card, parent, false);
        return new SearchStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchStoryViewHolder holder, int position) {
        StoryItem item = storyItems.get(position);
        holder.title.setText(item.title);
        holder.author.setText(item.author);
        holder.chapter.setText(holder.itemView.getContext().getString(R.string.search_chapter_prefix, item.chapter));
        holder.genre.setText(item.genre);
    }

    @Override
    public int getItemCount() {
        return storyItems.size();
    }

    static class SearchStoryViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView author;
        final TextView chapter;
        final TextView genre;

        SearchStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textSearchStoryTitle);
            author = itemView.findViewById(R.id.textSearchStoryAuthor);
            chapter = itemView.findViewById(R.id.textSearchStoryChapter);
            genre = itemView.findViewById(R.id.textSearchStoryGenre);
        }
    }
}

package com.example.app_truyen;

import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

public class SearchStoryAdapter extends RecyclerView.Adapter<SearchStoryAdapter.SearchStoryViewHolder> {

    private final List<StoryResponse> storyItems;

    public SearchStoryAdapter(List<StoryResponse> storyItems) {
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
        StoryResponse item = storyItems.get(position);
        holder.title.setText(item.title == null ? "(Không có tiêu đề)" : item.title);
        holder.author.setText(item.author == null ? "(Chưa rõ tác giả)" : item.author);
        holder.chapter.setText(holder.itemView.getContext().getString(R.string.search_chapter_prefix,
                String.valueOf(item.chapterCount)));
        holder.genre.setText(item.category == null ? "Khác" : item.category);

        String coverUrl = toAbsoluteCoverUrl(item.coverImage);
        Glide.with(holder.itemView.getContext())
                .load(coverUrl)
                .placeholder(R.color.home_thumb_placeholder)
                .error(R.color.home_thumb_placeholder)
                .into(holder.coverImage);
    }

    private String toAbsoluteCoverUrl(String coverImage) {
        if (coverImage == null || coverImage.trim().isEmpty()) {
            return null;
        }

        String trimmed = coverImage.trim();
        String lower = trimmed.toLowerCase(Locale.ROOT);
        if (lower.startsWith("http://") || lower.startsWith("https://")) {
            return trimmed;
        }

        String base = ApiClient.getBaseUrl();
        if (trimmed.startsWith("/")) {
            return base + trimmed.substring(1);
        }
        return base + trimmed;
    }

    @Override
    public int getItemCount() {
        return storyItems.size();
    }

    static class SearchStoryViewHolder extends RecyclerView.ViewHolder {
        final ImageView coverImage;
        final TextView title;
        final TextView author;
        final TextView chapter;
        final TextView genre;

        SearchStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.imageSearchCover);
            title = itemView.findViewById(R.id.textSearchStoryTitle);
            author = itemView.findViewById(R.id.textSearchStoryAuthor);
            chapter = itemView.findViewById(R.id.textSearchStoryChapter);
            genre = itemView.findViewById(R.id.textSearchStoryGenre);
        }
    }
}

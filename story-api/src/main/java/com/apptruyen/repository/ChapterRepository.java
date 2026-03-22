package com.apptruyen.repository;

import com.apptruyen.entity.Chapter;
import com.apptruyen.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    Optional<Chapter> findByStoryAndChapterNumber(Story story, Integer chapterNumber);

    @Query("SELECT c FROM Chapter c WHERE c.story = :story ORDER BY c.chapterNumber ASC")
    List<Chapter> findAllByStoryOrderByChapterNumberAsc(@Param("story") Story story);

    @Query("SELECT c.id, c.chapterNumber, c.title, c.wordCount, c.createdAt " +
           "FROM Chapter c WHERE c.story.id = :storyId ORDER BY c.chapterNumber ASC")
    List<Object[]> findChapterListByStoryId(@Param("storyId") Integer storyId);

    Optional<Chapter> findFirstByStoryOrderByChapterNumberDesc(Story story);
}

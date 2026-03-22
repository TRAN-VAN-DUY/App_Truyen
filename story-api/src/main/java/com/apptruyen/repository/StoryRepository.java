package com.apptruyen.repository;

import com.apptruyen.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Integer> {

    Optional<Story> findBySlug(String slug);

    Page<Story> findAll(Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.status = :status")
    Page<Story> findByStatus(@Param("status") Story.StoryStatus status, Pageable pageable);

    @Query("SELECT DISTINCT s FROM Story s JOIN s.genres g WHERE g.slug = :genreSlug")
    Page<Story> findByGenreSlug(@Param("genreSlug") String genreSlug, Pageable pageable);

    @Query("SELECT DISTINCT s FROM Story s JOIN s.genres g WHERE g.slug = :genreSlug AND s.status = :status")
    Page<Story> findByGenreSlugAndStatus(
        @Param("genreSlug") String genreSlug,
        @Param("status") Story.StoryStatus status,
        Pageable pageable
    );

    @Query("SELECT s FROM Story s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Story> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Story s SET s.views = s.views + 1 WHERE s.id = :id")
    void incrementViews(@Param("id") Integer id);
}

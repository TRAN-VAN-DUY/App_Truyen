package com.apptruyen.repository;

import com.apptruyen.entity.Favorite;
import com.apptruyen.entity.Story;
import com.apptruyen.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    Optional<Favorite> findByUserAndStory(User user, Story story);

    Page<Favorite> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    boolean existsByUserAndStory(User user, Story story);

    void deleteByUserAndStory(User user, Story story);
}

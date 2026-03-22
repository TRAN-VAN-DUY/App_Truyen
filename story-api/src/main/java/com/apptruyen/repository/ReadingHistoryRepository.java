package com.apptruyen.repository;

import com.apptruyen.entity.ReadingHistory;
import com.apptruyen.entity.Story;
import com.apptruyen.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Integer> {

    Optional<ReadingHistory> findByUserAndStory(User user, Story story);

    Page<ReadingHistory> findAllByUserOrderByUpdatedAtDesc(User user, Pageable pageable);
}

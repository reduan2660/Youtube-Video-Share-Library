package com.ytvideoshare.backend.repo;

import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Reaction;
import com.ytvideoshare.backend.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReactionRepo extends PagingAndSortingRepository<Reaction, Long> {

    Optional<Reaction> findById(Long id);
    Optional<Reaction> findByVideoAndReactor(Video video, AppUser reactor);
    List<Reaction> findAllByVideoAndLikedIsTrue(Video video, Pageable pageable);
    List<Reaction> findAllByVideoAndDislikedIsTrue(Video video, Pageable pageable);

    @Modifying
    @Transactional
    void deleteReactionById(Long id);

    @Modifying @Transactional @Query("UPDATE Reaction set liked=true, disliked=false where id=:id")
    void likeVideo(Long id);

    @Modifying @Transactional @Query("UPDATE Reaction set liked=false, disliked=true where id=:id")
    void dislikeVideo(Long id);

}

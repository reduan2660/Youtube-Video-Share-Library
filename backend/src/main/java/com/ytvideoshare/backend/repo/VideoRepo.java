package com.ytvideoshare.backend.repo;

import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VideoRepo extends PagingAndSortingRepository<Video, Long> {
    Video findVideoById(Long id);
    List<Video> findAllByPublishedIsTrue(Pageable pageable);
    List<Video> findAllByPublishedIsTrueAndNameIsLike(String name, Pageable pageable) ;
    List<Video> findAllByOwnerIs(AppUser owner, Pageable pageable);

    @Modifying @Transactional @Query("UPDATE Video set likes=likes+1 where id=:id")
    void increaseLike(Long id);

    @Modifying @Transactional @Query("UPDATE Video set likes=likes-1 where id=:id")
    void decreaseLike(Long id);

    @Modifying @Transactional @Query("UPDATE Video set dislikes=dislikes+1 where id=:id")
    void increaseDislike(Long id);

    @Modifying @Transactional @Query("UPDATE Video set dislikes=dislikes-1 where id=:id")
    void decreaseDislike(Long id);

    @Modifying @Transactional @Query("UPDATE Video set views=views+1 where id=:id")
    void incrementViews(Long id);
}


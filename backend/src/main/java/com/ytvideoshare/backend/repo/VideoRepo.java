package com.ytvideoshare.backend.repo;

import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VideoRepo extends PagingAndSortingRepository<Video, Long> {
    Video findVideoById(Long id);
    List<Video> findAllByPublishedIsTrue(Pageable pageable);
    List<Video> findAllByOwnerIs(AppUser owner, Pageable pageable);
}

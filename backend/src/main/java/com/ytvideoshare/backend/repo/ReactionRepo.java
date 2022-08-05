package com.ytvideoshare.backend.repo;

import com.ytvideoshare.backend.domain.Reaction;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReactionRepo extends PagingAndSortingRepository<Reaction, Long> {
}

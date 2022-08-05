package com.ytvideoshare.backend.dto;

import com.ytvideoshare.backend.domain.Reaction;
import com.ytvideoshare.backend.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReactionResponse {
    private Long id;
    private String reactor;
    private Boolean liked;
    private Boolean disliked;

    public ReactionResponse(Reaction reaction){
        this.id = reaction.getId();
        this.reactor = reaction.getReactor().getName();
        this.liked = reaction.getLiked();
        this.disliked = reaction.getDisliked();
    }

}

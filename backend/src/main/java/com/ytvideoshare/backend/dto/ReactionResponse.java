package com.ytvideoshare.backend.dto;

import com.ytvideoshare.backend.domain.Reaction;
import com.ytvideoshare.backend.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReactionResponse {
    private Long id;
//    private Video video;
    private AppUserResponse reactor;

    private Boolean liked;
    private Boolean disliked;

    ReactionResponse(Reaction reaction){
        this.id = reaction.getId();
//        this.video = reaction.getVideo();
        this.reactor = new AppUserResponse(reaction.getReactor());
        this.liked = reaction.getLiked();
        this.disliked = reaction.getDisliked();
    }

}

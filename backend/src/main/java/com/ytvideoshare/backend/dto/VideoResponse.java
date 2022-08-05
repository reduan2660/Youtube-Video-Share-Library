package com.ytvideoshare.backend.dto;

import com.ytvideoshare.backend.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class VideoResponse {

    private Long id;
    private String name;
    private String ytlink;
    private Boolean published;
    private Long views, likes, dislikes;

    public VideoResponse(Video video){
        this.id = video.getId();
        this.name = video.getName();
        this.ytlink = video.getYtlink();
        this.published = video.getPublished();
        this.views = video.getViews();
        this.likes = video.getLikes();
        this.dislikes = video.getDislikes();
    }
}

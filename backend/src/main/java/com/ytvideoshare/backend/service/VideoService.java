package com.ytvideoshare.backend.service;

import com.ytvideoshare.backend.Exception.ResourceNotFound;
import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Video;
import com.ytvideoshare.backend.dto.VideoRequest;
import com.ytvideoshare.backend.dto.VideoResponse;
import com.ytvideoshare.backend.repo.VideoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class VideoService {
    private final VideoRepo videoRepo;

    /**
     * Returns a video given ID and increases view counter
     * @param videoID Long
     * @return VideoResponse
     * @throws ResourceNotFound if video id is invalid
     */
    public VideoResponse getVideo(Long videoID) throws ResourceNotFound {
        Video video = videoRepo.findVideoById(videoID);
        if(video == null) throw new ResourceNotFound("Invalid Video ID");
        videoRepo.incrementViews(video.getId());
        return new VideoResponse(video);
    }

    /**
     * Saves a video
     * @param videoRequest Video Request containing name, ytlink
     * @param owner AppUser who uploaded the video
     * @return VideoResponse
     */
    public VideoResponse saveVideo(VideoRequest videoRequest, AppUser owner) throws AccessDeniedException {

        if(owner.isVerified()) {
            Video video = Video.builder()
                    .name(videoRequest.getName())
                    .ytlink(videoRequest.getYtlink())
                    .owner(owner)
                    .published(true)
                    .likes(0L)
                    .dislikes(0L)
                    .views(0L)
                    .build();
            Video savedVideo = videoRepo.save(video);

            return new VideoResponse(savedVideo);
        }
        else{
            throw new AccessDeniedException("Verify your email to share a video.");
        }
    }

    /**
     * Get all published videos
     * @param page page no
     * @param size size no
     * @param sortBy which field to sort by
     * @return List<VideoResponse>
     */
    public List<VideoResponse> getPublishedVideos(int page, int size, String sortBy, String search){
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,sortBy));
        List<VideoResponse> videoResponses = new ArrayList<>();

        String nameLike = "%" + search +"%";

        videoRepo.findAllByPublishedIsTrueAndNameIsLike(nameLike, paging).forEach(video -> {
            videoResponses.add(new VideoResponse(video));
        });

        return videoResponses;
    }

    /**
     * Get owners videos
     * @param owner the AppUser who requested
     * @param page page no
     * @param size size no
     * @param sortBy which field to sort by
     * @return List of VideoResponses
     */
    public List<VideoResponse> getOwnersVideos( AppUser owner, int page, int size, String sortBy){
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,sortBy));
        List<VideoResponse> videoResponses = new ArrayList<>();

        videoRepo.findAllByOwnerIs(owner, paging).forEach(video -> {
            videoResponses.add(new VideoResponse(video));
        });

        return videoResponses;
    }
}

package com.ytvideoshare.backend.service;

import com.ytvideoshare.backend.Exception.ResourceNotFound;
import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Reaction;
import com.ytvideoshare.backend.domain.Video;
import com.ytvideoshare.backend.dto.ReactionResponse;
import com.ytvideoshare.backend.repo.ReactionRepo;
import com.ytvideoshare.backend.repo.VideoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class ReactionService {
    private final VideoRepo videoRepo;
    private final ReactionRepo reactionRepo;

    /**
     * React to a video
     * @param reactor AppUser who wants to react
     * @param videoID The Video to be reacted
     * @param reactionType "like" or "dislike"
     * @return ReactionResponse or null if reaction is removed
     */
    public ReactionResponse reactToVideo(AppUser reactor, Long videoID, String reactionType) throws ResourceNotFound {
        Video video = videoRepo.findVideoById(videoID);
        if(video == null) throw new ResourceNotFound("Invalid Video ID");
//        null check for vide
        Optional<Reaction> existingReaction = reactionRepo.findByVideoAndReactor(video, reactor);
        boolean liked, disliked;
        if(existingReaction.isPresent()){

//            User wants to like a video
            if(Objects.equals(reactionType, "like")){
//                check if user already liked the video - then remove like
                if(existingReaction.get().getLiked()){
                    videoRepo.decreaseLike(video.getId());
                    reactionRepo.deleteReactionById(existingReaction.get().getId());
                    return null;
                }
                else{
                    videoRepo.increaseLike(video.getId());
                    videoRepo.decreaseDislike(video.getId());
                    reactionRepo.likeVideo(existingReaction.get().getId());
                    return new ReactionResponse(reactionRepo.findById(existingReaction.get().getId()).get());
                }
            }

            else{
//                check if user already disliked the video - then remove like
                if(existingReaction.get().getDisliked()){
                    videoRepo.decreaseDislike(video.getId());
                    reactionRepo.deleteReactionById(existingReaction.get().getId());
                    return null;
                }
                else{
                    videoRepo.increaseDislike(video.getId());
                    videoRepo.decreaseLike(video.getId());
                    reactionRepo.dislikeVideo(existingReaction.get().getId());
                    return new ReactionResponse(reactionRepo.findById(existingReaction.get().getId()).get());
                }
            }
        }
        else{
            if(Objects.equals(reactionType, "like")){
                liked = true;
                disliked = false;
                videoRepo.increaseLike(video.getId());
            }
            else{
                liked = false;
                disliked = true;
                videoRepo.increaseDislike(video.getId());
            }

            Reaction reaction = Reaction.builder()
                    .video(video)
                    .reactor(reactor)
                    .liked(true)
                    .disliked(disliked)
                    .build();

            Reaction savedReaction = reactionRepo.save(reaction);
            return new ReactionResponse(savedReaction);
        }
    }

    /**
     * Returns user's reaction to a video
     * @param reactor AppUser reactor
     * @param videoID Long Vide id
     * @return ReactionResponse or null if it does not exist
     */
    public ReactionResponse userReaction(AppUser reactor, Long videoID) throws ResourceNotFound {

        Video video = videoRepo.findVideoById(videoID);
        if(video == null) throw new ResourceNotFound("Invalid Video ID");

        Optional<Reaction> reaction = reactionRepo.findByVideoAndReactor(video, reactor);
        return reaction.map(ReactionResponse::new).orElse(null);

    }

    /**
     * Returns a video's reaction
     * @param videoID video id
     * @param reactionType like or dislike
     * @param page page no
     * @param size page size
     * @return List of ReactionResponse
     */
    public List<ReactionResponse> videoReactions(Long videoID, String reactionType, int page, int size) throws ResourceNotFound {
        Pageable paging = PageRequest.of(page, size);

        Video video = videoRepo.findVideoById(videoID);
        if(video == null) throw new ResourceNotFound("Invalid Video ID");

        List<ReactionResponse> reactionResponses = new ArrayList<>();
        if (Objects.equals(reactionType, "like")) {
            reactionRepo.findAllByVideoAndLikedIsTrue(video, paging).forEach(reaction -> {
                reactionResponses.add(new ReactionResponse(reaction));
            });
        } else {
            reactionRepo.findAllByVideoAndDislikedIsTrue(video, paging).forEach(reaction -> {
                reactionResponses.add(new ReactionResponse(reaction));
            });
        }
        return reactionResponses;

    }
}

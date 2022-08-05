package com.ytvideoshare.backend.api;

import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Video;
import com.ytvideoshare.backend.dto.ReactionResponse;
import com.ytvideoshare.backend.service.AppUserService;
import com.ytvideoshare.backend.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor @Slf4j
public class ReactionController {
    private final ReactionService reactionService;
    private final AppUserService appUserService;

    @PostMapping("/video/like/{videoID}")
    public ReactionResponse likeVideo(@PathVariable Long videoID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser reactor = appUserService.getUser((String) auth.getPrincipal());
        return reactionService.reactToVideo(reactor, videoID, "like");
    }

    @PostMapping("/video/dislike/{videoID}")
    public ReactionResponse dislikeVideo(@PathVariable Long videoID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser reactor = appUserService.getUser((String) auth.getPrincipal());
        return reactionService.reactToVideo(reactor, videoID, "dislike");
    }

    @GetMapping("/video/myreaction/{videoID}")
    public ReactionResponse myReaction(@PathVariable Long videoID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser reactor = appUserService.getUser((String) auth.getPrincipal());
        return reactionService.userReaction(reactor, videoID);
    }

    @GetMapping("/video/likes/{videoID}")
    public List<ReactionResponse> videoLikes(
            @PathVariable Long videoID,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ){
        return reactionService.videoReactions(videoID, "like", page, size);
    }
    @GetMapping("/video/dislikes/{videoID}")
    public List<ReactionResponse> videoDislikes(
            @PathVariable Long videoID,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ){
        return reactionService.videoReactions(videoID, "dislike", page, size);
    }


}

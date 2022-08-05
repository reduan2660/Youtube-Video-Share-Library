package com.ytvideoshare.backend.api;

import com.ytvideoshare.backend.Exception.DuplicateEmailException;
import com.ytvideoshare.backend.dto.AppUserRequest;
import com.ytvideoshare.backend.dto.AppUserResponse;
import com.ytvideoshare.backend.dto.VideoRequest;
import com.ytvideoshare.backend.dto.VideoResponse;
import com.ytvideoshare.backend.service.AppUserService;
import com.ytvideoshare.backend.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor @Slf4j
public class VideoController {
    private final VideoService videoService;
    private final AppUserService appUserService;

    /**
     * Retrieve site url domain
     * @param request A HttpServletRequest object
     * @return A sting literal : siteurl
     */
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    /**
     * Upload a video - ROLE USER
     * @param videoRequest a video request JSON object
     * @param request A HttpServletRequest object
     * @return Saved User Response
     */
    @Operation(summary = "Upload a youtube video.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Uploaded Video",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoResponse.class)) })
    })
    @PostMapping("/video/upload")
    public ResponseEntity<VideoResponse> uploadVideo(@RequestBody @Valid VideoRequest videoRequest, HttpServletRequest request){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/registration").toUriString());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        VideoResponse videoResponse = videoService.saveVideo(videoRequest, appUserService.getUser((String) auth.getPrincipal()));
        return ResponseEntity.created(uri).body(videoResponse);
    }

    /**
     * Get Video List - OPEN FOR ALL
     * @param page page no
     * @param size page size
     * @param sortBy sort by field
     * @return List of VideoResponses
     */
    @Operation(summary = "Returns published videos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns published videos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid token"),
    })
    @GetMapping("/video/explore")
    public ResponseEntity<List<VideoResponse>> exploreVideos(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "likes") String sortBy
    ){
        return  ResponseEntity.ok().body(videoService.getPublishedVideos(page, size, sortBy));
    }

    /**
     * Get Owners video - ROLE USER
     * @param page page no
     * @param size page size
     * @param sortBy sort by field
     * @return List of VideoResponse
     */
    @Operation(summary = "Returns owners videos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns owners videos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid token"),
    })
    @GetMapping("/video/my")
    public ResponseEntity<List<VideoResponse>> myVideos(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "likes") String sortBy
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return  ResponseEntity.ok().body(videoService.getOwnersVideos(  appUserService.getUser((String) auth.getPrincipal()) ,page, size, sortBy));
    }
}

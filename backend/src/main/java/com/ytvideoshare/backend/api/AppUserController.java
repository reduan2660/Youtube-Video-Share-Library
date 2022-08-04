package com.ytvideoshare.backend.api;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytvideoshare.backend.Exception.DuplicateEmailException;
import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Role;
import com.ytvideoshare.backend.dto.AppUserRequest;
import com.ytvideoshare.backend.dto.AppUserResponse;
import com.ytvideoshare.backend.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequiredArgsConstructor @Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @Value("${JWTSECRET}")
    private String jwtSecret;

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
     * Registers a  user
     * @param user a  user - JSON object
     * @param request A HttpServletRequest object
     * @return Saved User Response
     * @throws DuplicateEmailException if user already exists with given email
     */
    @Operation(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Registration",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUserResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = @Content),
    })
    @PostMapping("/user/registration")
    public ResponseEntity<AppUserResponse> registerDonor(@RequestBody @Valid AppUserRequest user, HttpServletRequest request) throws DuplicateEmailException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/registration").toUriString());
        AppUserResponse savedUser = appUserService.saveUser(user, appUserService.getRole("ROLE_USER"), this.getSiteURL(request));
        return ResponseEntity.created(uri).body(savedUser);
    }

    /**
     * Returns User Profile
     * @param request HttpServletRequest object
     * @param response HttpServletRequest object
     * @return User's profile as AppUserResponse
     */
    @Operation(summary = "Profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns profile",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUserResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid token"),
    })
    @GetMapping("/user/profile")
    public ResponseEntity<AppUserResponse> getProfile(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok().body(appUserService.getUserResponse((String) auth.getPrincipal()));
    }

    /**
     * Verify user's email
     * @param code Int value as parameter
     * @param email String value as parameter
     * @return Success Status
     */
    @Operation(summary = "Verify email")
    @ApiResponse(responseCode = "200", description = "Returns status true")
    @PostMapping("/user/verify")
    public ResponseEntity<Map<String, Boolean>> verifyAccount(@RequestParam int code, @RequestParam String email){
        Boolean success = appUserService.verifyUser(email, code);

        Map<String, Boolean> successMsg = new HashMap<>();
        successMsg.put("Success", success);
        return ResponseEntity.ok().body(successMsg);
    }

    /**
     * Refreshes Access Token given verification token
     * @param request HttpServletRequest object
     * @param response HttpServletRequest object
     * @throws IOException if something goes wrong
     */
    @Operation(summary = "Refresh Access token")
    @ApiResponse(responseCode = "200", description = "Refreshed Access token")
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                AppUser user = appUserService.getUser(email);

                Collection<Role> roles = new ArrayList<>();
                roles.add(user.getRole());

                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles", roles.stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
            catch (Exception exception){
                log.error("Error logging in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }


        } else{
            throw new RuntimeException("Refresh token is missing");
        }
    }

}



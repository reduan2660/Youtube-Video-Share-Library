package com.ytvideoshare.backend.dto;

import com.ytvideoshare.backend.domain.AppUser;
import com.ytvideoshare.backend.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppUserResponse{
    public Long id;
    public String name, email;
    public Boolean verified;
    public LocalDateTime created, updated;
    public Role role;

    public AppUserResponse(AppUser user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.verified = user.isVerified();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
        this.role = user.getRole();
    }
}


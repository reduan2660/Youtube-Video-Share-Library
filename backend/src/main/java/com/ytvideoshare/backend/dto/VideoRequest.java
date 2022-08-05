package com.ytvideoshare.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoRequest {
    @NotNull(message = "You must enter name.")
    private String name;

    @NotNull(message = "You must enter a valid youtube link.")
    private String ytlink;
}

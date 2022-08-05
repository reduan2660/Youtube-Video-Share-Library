package com.ytvideoshare.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Reaction {
    @Id
    @SequenceGenerator(
            name = "reaction_sequence",
            sequenceName = "reaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reaction_sequence"
    )
    private Long id;

    @ManyToOne @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne @JoinColumn(name = "reactor_id")
    private AppUser reactor;
    private Boolean liked;
    private Boolean disliked;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;
}

package com.ytvideoshare.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Video {

    @Id
    @SequenceGenerator(
            name = "videodetails_sequence",
            sequenceName = "videodetails_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "videodetails_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String ytlink;

    private Boolean published = true;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    private Long views = Long.valueOf(0);
    private Long likes = Long.valueOf(0);
    private Long dislikes = Long.valueOf(0);

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;
}

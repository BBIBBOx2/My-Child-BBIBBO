package com.publicapi.test.domain.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(targetEntity = Post.class, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(targetEntity = User.class, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean isAnonymous;

    @Column(nullable = false)
    private LocalDateTime createDate;
}

package com.publicapi.test.domain.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post_tag")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(targetEntity = Hashtag.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hashtag_id", insertable = false, updatable = false)
    private Hashtag hashtag;

    @Column(name = "hashtag_id")
    private Long hashtagId;
}

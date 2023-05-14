package com.publicapi.test.domain.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Boards boards;

    @ManyToOne
    private Districts districts;

    @ManyToOne
    private Users author;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<Comments> commentsList;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private Integer hits;
}

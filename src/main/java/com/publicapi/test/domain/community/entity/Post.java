package com.publicapi.test.domain.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @ManyToOne
    private District district;

    @ManyToOne
    private User author;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean isAnonymous;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private Integer hits;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "scrap", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> scrap;

    @Formula("(select count(*) from scrap where scrap.post_id=id)")
    private int scrapCount;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", board=" + board +
                ", district=" + district +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isAnonymous=" + isAnonymous +
                ", postTags=" + postTags +
                ", postImages=" + postImages +
                ", commentList=" + commentList +
                ", createDate=" + createDate +
                ", hits=" + hits +
                ", scrap=" + scrap +
                ", scrapCount=" + scrapCount +
                '}';
    }
}

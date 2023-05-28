package com.publicapi.test.domain.community.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    private String name;

    private String username;

    private String profileImage;

    @Builder
    public User(Long id, String email, String kakaoId, String name, String username, String profileImage) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.kakaoId = kakaoId;
        this.name = name;
        this.profileImage = profileImage;
    }
}

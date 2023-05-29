package com.publicapi.test.domain.user.entity;

import com.publicapi.test.domain.community.entity.Post;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    private String name;

    private String username;

    private Integer bornYear;

    private String profileImage;

    @ManyToMany(mappedBy = "scrap", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Post> scrap;

    @ManyToOne
    private RegionEntity region;

    @Builder
    public UserEntity(Long id, String email, String kakaoId, String name, String username, RegionEntity region, Integer bornYear, String profileImage) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.kakaoId = kakaoId;
        this.name = name;
        this.bornYear = bornYear;
        this.region = region;
        this.profileImage = profileImage;
    }

    public int getAge() {
        int year = LocalDate.now().getYear();
        return year - bornYear;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                '}';
    }
}

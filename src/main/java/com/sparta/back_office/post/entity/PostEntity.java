package com.sparta.back_office.post.entity;

import com.sparta.back_office.post.dto.PostAddRequestDto;
import com.sparta.back_office.post.dto.PostUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends TimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false, length = 20)
    private String title;
    @Column(nullable = false, length = 500)
    private String contents;

    public PostEntity(PostAddRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.author = requestDto.getAuthor();
        this.contents = requestDto.getContent();
    }

    public void update(PostUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.author = requestDto.getAuthor();
        this.contents = requestDto.getContent();
    }
}
package com.sparta.back_office.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.back_office.dto.CommentRequestDto;
import com.sparta.back_office.post.entity.PostEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.awt.Menu;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "comment")
@NoArgsConstructor
@Entity
@Setter
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String text;
    @Column
    private LocalDateTime createTime;
    @Column
    private LocalDateTime modifyTime;

    @ManyToOne
    @JoinColumn(name = "users_id")
    @JsonIgnore
    private User users;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private PostEntity post;


    public Comment(CommentRequestDto commentRequestDto) {
        this.text = commentRequestDto.getText();
        this.createTime = LocalDateTime.now();
    }
    public void updateComment(String text){
        this.text = text;
        this.modifyTime = LocalDateTime.now();
    }



}

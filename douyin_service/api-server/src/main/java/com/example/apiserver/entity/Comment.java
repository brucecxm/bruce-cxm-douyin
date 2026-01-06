package com.example.apiserver.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "用户ID不能为空")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能小于1")
    @Max(value = 5, message = "评分不能大于5")
    private Integer rating;

    @NotBlank(message = "评论内容不能为空")
    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer likes = 0;
    private Integer replies = 0;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
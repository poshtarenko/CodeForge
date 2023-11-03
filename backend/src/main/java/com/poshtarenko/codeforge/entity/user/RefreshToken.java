package com.poshtarenko.codeforge.entity.user;

import com.poshtarenko.codeforge.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refreshtoken")
public class RefreshToken extends BaseEntity {

    @Column
    private String token;

    @Column
    private LocalDateTime expiration;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

}
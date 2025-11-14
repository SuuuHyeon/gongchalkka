package com.project.gongchalkka.match.entity;

import com.project.gongchalkka.field.entity.Field;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위해 추가
@Table(name = "matchs")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private int currentCapacity = 0;    // 기본값 0명으로 세팅

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)        // DB에 string으로 저장
    private MatchStatus matchStatus = MatchStatus.RECRUITING;   // 기본값 '모집중'으로 설정


    // 생성자
    public Match(Field field, LocalDateTime startTime, LocalDateTime endTime, Integer maxCapacity) {
        this.field = field;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxCapacity = maxCapacity;
    }


}

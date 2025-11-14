package com.project.gongchalkka.match.entity;

import com.project.gongchalkka.field.entity.Field;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
// JPA를 위해 추가, 임의 변형 불가하게 protected
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchSubscription> subscriptions = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)        // DB에 string으로 저장
    private MatchStatus status = MatchStatus.RECRUITING;   // 기본값 '모집중'으로 설정


    // 생성자
    public Match(Field field, LocalDateTime startTime, LocalDateTime endTime, Integer maxCapacity) {
        this.field = field;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxCapacity = maxCapacity;
    }

    // 매치 신청
    public boolean addParticipant() {
        // 인원 및 매칭상태 검사
        if (!(this.currentCapacity < this.maxCapacity) || !(this.status == MatchStatus.RECRUITING)) {
            throw new IllegalStateException("참가 신청이 불가능한 상태입니다.");
        }
        this.currentCapacity++;

        // 인원 꽉 차면 매치 성사
        if (this.currentCapacity == this.maxCapacity) {
            this.status = MatchStatus.COMPLETED;
            return true;
        }
        return false;
    }

    // 매치 신청 취소
    public void removeParticipant() {
        if (this.currentCapacity <= 0) {
            throw new IllegalStateException("참가자가 0명입니다.");
        }

        this.currentCapacity--;

        if (this.currentCapacity < this.maxCapacity) {
            this.status = MatchStatus.RECRUITING;
        }
    }


}

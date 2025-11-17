package com.project.gongchalkka.match.entity;

import com.project.gongchalkka.field.entity.Field;
import com.project.gongchalkka.global.exception.BusinessErrorException;
import com.project.gongchalkka.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = {"field", "subscriptions"}) // 연관관계 필드는 제외
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
    private Integer currentCapacity = 0;    // 기본값 0명으로 세팅

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
    public void addParticipant() {
        // 인원 검사
        if (!(this.currentCapacity < this.maxCapacity)) {
            throw new BusinessErrorException(ErrorCode.MATCH_CAPACITY_FULL);
        }

        // 매칭 상태 검사 (모집중)
        if (!(this.status == MatchStatus.RECRUITING)) {
            throw new BusinessErrorException(ErrorCode.MATCH_NOT_RECRUITING);
        }

        // 인원 참가
        this.currentCapacity++;

        if (this.currentCapacity.equals(this.maxCapacity)) {
            this.status = MatchStatus.CONFIRMED;    // 정원마감
        }
    }

    // 매치 신청 취소
    public void removeParticipant() {
        if (this.currentCapacity <= 0) {
            throw new BusinessErrorException(ErrorCode.MATCH_PARTICIPANT_EMPTY);
        }

        this.currentCapacity--;

        if (this.currentCapacity < this.maxCapacity) {
            this.status = MatchStatus.RECRUITING;
        }
    }


}

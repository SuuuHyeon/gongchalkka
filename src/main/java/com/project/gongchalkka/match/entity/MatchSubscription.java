package com.project.gongchalkka.match.entity;

import com.project.gongchalkka.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "match_subscriptions")
public class MatchSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 생성자
    public MatchSubscription(Member member, Match match) {
        this.member = member;
        this.match = match;
    }

    /**
     * JPA 생명주기 훅(Hook)
     * 이 엔티티가 '최초 저장(INSERT)' 되기 '직전'에 자동 호출
     */
    @PrePersist
    public void onPrePersist() {
        this.status = SubscriptionStatus.APPLIED;
        this.createdAt = LocalDateTime.now();
    }

    // 참가 취소 메서드
    public void cancel() {
        this.status = SubscriptionStatus.CANCELED;
    }
}

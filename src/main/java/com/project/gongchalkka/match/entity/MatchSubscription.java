package com.project.gongchalkka.match.entity;

import com.project.gongchalkka.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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
    private SubscriptionStatus status = SubscriptionStatus.APPLIED;     // 기본값 '신청됨'

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 생성자
    public MatchSubscription(Member member, Match match, SubscriptionStatus status, LocalDateTime createdAt) {
        this.member = member;
        this.match = match;
        this.status = status;
        this.createdAt = createdAt;
    }

    // 참가 취소 메서드
    public void cancel() {
        this.status = SubscriptionStatus.CANCELED;
    }
}

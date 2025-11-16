package com.project.gongchalkka.match.repository;

import com.project.gongchalkka.match.entity.Match;
import com.project.gongchalkka.match.entity.MatchSubscription;
import com.project.gongchalkka.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchSubscriptionRepository extends JpaRepository<MatchSubscription, Long> {
    boolean existsByMemberAndMatch(Member member, Match match);

    Optional<MatchSubscription> findByMemberAndMatch(Member member, Match match);
}

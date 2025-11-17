package com.project.gongchalkka.match.service;


import com.project.gongchalkka.global.exception.BusinessErrorException;
import com.project.gongchalkka.global.exception.EntityNotFoundErrorException;
import com.project.gongchalkka.global.exception.ErrorCode;
import com.project.gongchalkka.match.dto.MatchResponse;
import com.project.gongchalkka.match.entity.Match;
import com.project.gongchalkka.match.entity.MatchSubscription;
import com.project.gongchalkka.match.entity.SubscriptionStatus;
import com.project.gongchalkka.match.repository.MatchRepository;
import com.project.gongchalkka.match.repository.MatchSubscriptionRepository;
import com.project.gongchalkka.member.entity.Member;
import com.project.gongchalkka.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MemberRepository memberRepository;
    private final MatchRepository matchRepository;
    private final MatchSubscriptionRepository matchSubscriptionRepository;


    ///  매치 조회 메서드
    public Page<MatchResponse> getAllMatches(Pageable pageable) {

        // 페이징 처리로 필드 정보를 가진 매치 가져오기
        Page<Match> matchPage = matchRepository.findAllWithField(pageable);

        log.info("매치 조회 성공! [Page: {}/{}, Total Elements: {}, Current Elements: {}]",
                matchPage.getNumber() + 1, // spring은 0페이지부터 시작이라 +1
                matchPage.getTotalPages(),
                matchPage.getTotalElements(),
                matchPage.getNumberOfElements()
        );
        return matchPage.map(MatchResponse::fromEntity);
    }


    ///  참가신청 메서드
    @Transactional
    public void applyToMatch(Long matchId, Principal principal) {
        // 유저 정보 검증
        Member member = memberRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.USER_NOT_FOUND)
        );

        // 매치 정보 검증
        Match match = matchRepository.findByIdWithField(matchId).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.MATCH_NOT_FOUND)
        );

        // 중복 신청 조회
        if (matchSubscriptionRepository.existsByMemberAndMatch(member, match)) {
            throw new BusinessErrorException(ErrorCode.MATCH_ALREADY_APPLIED);
        }

        // 참가 신청
        match.addParticipant();

        // 저장
        MatchSubscription matchSubscription = new MatchSubscription(member, match);
        matchSubscriptionRepository.save(matchSubscription);


        log.info("매치 참가 신청 성공! [Member ID: {}, Member Name: {}, Match ID: {}, FieldName: {}]",
                member.getId(),
                member.getNickname(),
                match.getId(),
                match.getField().getFieldName()
        );
    }


    ///  참가 취소 메서드
    @Transactional
    public void cancelMatch(Long matchId, Principal principal) {
        // 유저 정보 검증
        Member member = memberRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.USER_NOT_FOUND)
        );

        // 매치 정보 검증
        Match match = matchRepository.findByIdWithField(matchId).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.MATCH_NOT_FOUND)
        );

        // 신청서 검증
        MatchSubscription matchSubscription = matchSubscriptionRepository.findByMemberAndMatch(member, match)
                .orElseThrow(
                        () -> new EntityNotFoundErrorException(ErrorCode.SUBSCRIPTION_NOT_FOUND)
                );

        if (matchSubscription.getStatus() == SubscriptionStatus.CANCELED) {
            throw new BusinessErrorException(ErrorCode.SUBSCRIPTION_ALREADY_CANCELED);
        }

        // 인원 빼기
        match.removeParticipant();

        // 매치 상태 변경
        matchSubscription.cancel();
        log.info("매치 참가 취소 성공! [Member ID: {}, Member Name: {}, Match ID: {}, FieldName: {}]",
                member.getId(),
                member.getNickname(),
                match.getId(),
                match.getField().getFieldName());
    }
}
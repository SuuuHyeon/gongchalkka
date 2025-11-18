package com.project.gongchalkka.match.controller;


import com.project.gongchalkka.match.dto.MatchCreateRequest;
import com.project.gongchalkka.match.dto.MatchResponse;
import com.project.gongchalkka.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matches") // '매치' 관련 API
public class MatchController {

    private final MatchService matchService;

    // (참고: GET / (매치 목록), GET /{id} (매치 상세) 등 조회 API는 나중에 추가)

    /**
     * 매치 조회 (페이징)
     */
    @GetMapping
    public ResponseEntity<Page<MatchResponse>> getAllMatches(
            // 페이징 기본값 설정
            @PageableDefault(size = 20, sort = "startTime", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<MatchResponse> matches = matchService.getAllMatches(pageable);

        return ResponseEntity.ok(matches);
    }

    /**
     * 매치 조회 (단건)
     */
    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResponse> getMatch(@PathVariable Long matchId) {
        MatchResponse matchResponse = matchService.getMatch(matchId);
        return ResponseEntity.ok(matchResponse);
    }

    /**
     * 매치 신청
     */
    @PostMapping("/{matchId}/apply")
    public ResponseEntity<Void> applyToMatch(
            @PathVariable Long matchId,
            Principal principal
    ) {
        // '실제 로직'은 Service에 위임
        matchService.applyToMatch(matchId, principal);

        // 참가 신청 성공 시, 201 Created (새로운 '신청'이 생성됨)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 매치 취소
     */
    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> cancelMatch(@PathVariable Long matchId, Principal principal) {
        matchService.cancelMatch(matchId, principal);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 매치 생성
     */
    @PostMapping
    /// TODO: 유효성 체크 추가
    public ResponseEntity<MatchResponse> createMatch(@RequestBody MatchCreateRequest request, Principal principal) {
        MatchResponse match = matchService.createMatch(request, principal);

        return ResponseEntity.status(HttpStatus.CREATED).body(match);
    }

    // (추후 '참가 취소' API: DELETE /matches/{matchId}/cancel 구현 예정)
}
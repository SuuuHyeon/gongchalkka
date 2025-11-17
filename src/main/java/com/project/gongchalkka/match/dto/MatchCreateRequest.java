package com.project.gongchalkka.match.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchCreateRequest {

    /// TODO: 유효성 체크 추가
    private Long fieldId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxCapacity;
}

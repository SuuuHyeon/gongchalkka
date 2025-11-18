package com.project.gongchalkka.match.dto;


import com.project.gongchalkka.match.entity.Match;
import com.project.gongchalkka.match.entity.MatchStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MatchResponse {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxCapacity;
    private int currentCapacity;
    private MatchStatus status;

    private Long fieldId;
    private String fieldName;
    private String location;
    private String hostName;


    public static MatchResponse fromEntity(Match match) {
        return MatchResponse.builder()
                .id(match.getId())
                .startTime(match.getStartTime())
                .endTime(match.getEndTime())
                .maxCapacity(match.getMaxCapacity())
                .currentCapacity(match.getCurrentCapacity())
                .status(match.getStatus())
                .fieldId(match.getField().getId())
                .fieldName(match.getField().getFieldName())
                .location(match.getField().getLocation())
                .hostName(match.getHost().getNickname())
                .build();

    }
}

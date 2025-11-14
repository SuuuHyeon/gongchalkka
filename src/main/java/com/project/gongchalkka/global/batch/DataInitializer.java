package com.project.gongchalkka.global.batch;

import com.project.gongchalkka.field.entity.Field;
import com.project.gongchalkka.field.repository.FieldRepository;
import com.project.gongchalkka.match.entity.Match;
import com.project.gongchalkka.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final FieldRepository fieldRepository;
    private final MatchRepository matchRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 중복 방지
        if (fieldRepository.count() > 0) {
            log.info("[DataInitializer] 이미 데이터가 존재하므로, 초기화를 건너뜁니다.");
            return;
        }

        log.info("[DataInitializer] '테스트용' 풋살장 및 매치 데이터 생성 시작");

        Field fieldA = new Field(
                "공찰까A 풋살장",
                "서울 강남구 역삼동",
                10000,
                "02-123-4567"
        );

        fieldRepository.save(fieldA);

        Match match1 = new Match(
                fieldA,
                LocalDateTime.now().plusDays(1).withHour(20).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(1).withHour(22).withMinute(0).withSecond(0).withNano(0),
                12
        );

        Match match2 = new Match(
                fieldA,
                LocalDateTime.now().plusDays(1).withHour(20).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(1).withHour(22).withMinute(0).withSecond(0).withNano(0),
                12
        );

        matchRepository.save(match1);
        matchRepository.save(match2);
    }
}

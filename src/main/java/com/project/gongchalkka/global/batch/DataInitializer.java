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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate; // [⭐️] '날짜' 기준
import java.time.LocalDateTime;
import java.time.LocalTime; // [⭐️] '시간' 기준
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final FieldRepository fieldRepository;
    private final MatchRepository matchRepository;


    @Override
    @Transactional // [⭐️] '일괄 처리'는 반드시 트랜잭션으로 묶어야 함
    public void run(ApplicationArguments args) throws Exception {

        // 중복 방지
        if (fieldRepository.count() > 0) {
            log.info("[DataInitializer] 이미 데이터가 존재하므로, 초기화를 건너뜁니다.");
            return;
        }

        log.info("[DataInitializer] '테스트용' 풋살장 및 (48개) 매치 데이터 생성 시작");

        // [⭐️ 1. 풋살장 2개 생성]
        Field fieldA = new Field(
                "공찰까A 풋살장 (강남)",
                "서울 강남구 역삼동",
                10000,
                "02-123-4567"
        );
        Field fieldB = new Field(
                "공찰까B 풋살장 (안양)",
                "경기도 안양시",
                10000,
                "02-123-4567"
        );

        List<Field> fields = List.of(fieldA, fieldB);
        fieldRepository.saveAll(fields); // (일괄 저장)
        log.info("[DataInitializer] 풋살장 2개 생성 완료.");


        // [⭐️ 2. (핵심) '이틀치', '12개 시간대' 매치 생성 (총 48개)]

        List<Match> matchesToSave = new ArrayList<>(); // '일괄 저장'할 리스트

        // (1) "오늘", "내일" 이틀치 날짜
        List<LocalDate> days = List.of(
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // (2) "0-2시"부터 "22-24시"까지 12개 시간대
        List<LocalTime> startTimes = new ArrayList<>();
        for (int hour = 0; hour <= 22; hour += 2) { // 0, 2, 4, ... 22
            startTimes.add(LocalTime.of(hour, 0));
        }

        // (3) 3중 For-Loop로 48개 매치 생성
        for (LocalDate day : days) { // "이틀치"
            for (LocalTime time : startTimes) { // "시간대별"
                for (Field field : fields) { // "구장별"

                    LocalDateTime startTime = LocalDateTime.of(day, time);
                    LocalDateTime endTime = startTime.plusHours(2);

                    Match match = new Match(
                            field,
                            startTime,
                            endTime,
                            1 // 👈 [요청 사항] maxCapacity = 1
                    );
                    matchesToSave.add(match);
                }
            }
        }

        // [⭐️ 3. (핵심)] 48개의 매치를 '한 방에' DB에 저장 (Batch Insert)
        matchRepository.saveAll(matchesToSave);

        log.info("[DataInitializer] '테스트용' 매치 {}개 생성 완료.", matchesToSave.size());
    }
}
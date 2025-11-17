package com.project.gongchalkka.match.repository;

import com.project.gongchalkka.field.entity.Field;
import com.project.gongchalkka.match.entity.Match;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    /// JPQL 적용


    // 매치 리스트 조회
    @Query(value = "select m from Match m join fetch m.field",
            countQuery = "select count(m) from Match m")
    Page<Match> findAllWithField(Pageable pageable);

    // 매치 단건 조회
    @Query(value = "select m from Match m join fetch m.field where m.id = :matchId")
    Optional<Match> findByIdWithField(@Param("matchId") Long matchId);

    @Query("SELECT COUNT(m) > 0 FROM Match m " +
            "WHERE m.field = :field " +
            "AND m.startTime < :endTime " +
            "AND m.endTime > :startTime")
    boolean existsOverlappingMatch(
            @Param("field") Field field,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}

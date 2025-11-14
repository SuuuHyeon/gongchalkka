package com.project.gongchalkka.match.repository;

import com.project.gongchalkka.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}

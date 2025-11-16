package com.project.gongchalkka.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * [ErrorCode Enum]
 * <p>
 * 1. "A-xxx": Auth (인증/인가) 관련
 * 2. "U-xxx": User (회원) 관련
 * 3. "M-xxx": Match (매치/예약) 관련
 * 4. "F-xxx": Field (풋살장) 관련
 * 5. "G-xxx": Global (공통, 서버) 관련
 */

@Getter
public enum ErrorCode {

    // ===== 1. Auth (A) =====
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A-002", "토큰이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-003", "토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-004", "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "A-005", "리프레시 토큰이 존재하지 않습니다."),
    AUTHORIZATION_FAILED(HttpStatus.FORBIDDEN, "A-006", "권한이 없습니다."),

    // ===== 2. User (U) =====
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U-001", "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U-002", "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U-003", "비밀번호가 일치하지 않습니다."),

    // ===== 3. Match (M) =====
    MATCH_NOT_FOUND(HttpStatus.NOT_FOUND, "M-001", "존재하지 않는 매치입니다."),
    MATCH_ALREADY_APPLIED(HttpStatus.CONFLICT, "M-002", "이미 신청한 매치입니다."),
    MATCH_NOT_RECRUITING(HttpStatus.BAD_REQUEST, "M-003", "모집중인 매치가 아닙니다."),
    MATCH_CAPACITY_FULL(HttpStatus.BAD_REQUEST, "M-004", "정원이 마감된 매치입니다."),
    MATCH_PARTICIPANT_EMPTY(HttpStatus.CONFLICT, "M-005", "참가자가 0명이므로 취소할 수 없습니다."),

    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "M-006", "신청하신 매치가 아닙니다."),
    SUBSCRIPTION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "M-007", "이미 취소하였습니다."),

    // ===== 4. Field (F) =====
    FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "F-001", "존재하지 않는 풋살장입니다."),

    // ===== 5. Global (G) =====
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "G-001", "입력값에 대한 유효성 검사에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G-002", "서버 내부 오류가 발생했습니다.");



    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ///  private 생성자 (enum이 내부적으로 public, protected 막음 -> 임의 수정 금지)
    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
package com.project.gongchalkka.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /// 리소스 에러
    @ExceptionHandler(EntityNotFoundErrorException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundErrorException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("EntityNotFoundErrorException: {}", errorCode.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }


    ///  비즈니스 로직 에러 핸들러
    @ExceptionHandler(BusinessErrorException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogic(BusinessErrorException e) {
        // error 코드 뽑기
        ErrorCode errorCode = e.getErrorCode();
        log.error("BusinessErrorException: {}", errorCode.getMessage());

        // ErrorResponse 생성
        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }


    /// valid 유효성 에러 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> hanValidation(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String firstErrorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
        log.error("ValidationFailed: {}", firstErrorMessage);

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getErrorCode(),
                firstErrorMessage       // firstErrorMessage 값 넣어주기
        );

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }


    ///  기타 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e); // 500에러는 stackTrace를 넘겨야함? TODO

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(errorResponse, ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}

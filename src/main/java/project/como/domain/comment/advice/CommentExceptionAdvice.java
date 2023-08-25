package project.como.domain.comment.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.como.domain.comment.controller.CommentController;
import project.como.domain.comment.exception.CommentForbiddenAccessException;
import project.como.global.common.dto.ExceptionResponse;
import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

import java.time.LocalDateTime;

@RestControllerAdvice // ("project.domain.como.comment.controller")
public class CommentExceptionAdvice {


    @ExceptionHandler(CommentForbiddenAccessException.class)
    public ResponseEntity<ExceptionResponse> forbiddenAccess(ComoException ex){

        return new ResponseEntity<>(ExceptionResponse.builder()
                .title("Comment modify or delete forbidden")
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getDetail())
                .build(), HttpStatus.FORBIDDEN);
    }
}

package com.semicolondevop.suite.exception;
/*
 *@author tobi
 * created on 29/04/2020
 *
 */

import com.cloudinary.api.ApiResponse;
import com.semicolondevop.suite.client.genericresponse.ResponseApi;
import com.semicolondevop.suite.exception.restTemplate.ErrorResponse;
import com.semicolondevop.suite.exception.restTemplate.MyRestTemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected org.springframework.http.ResponseEntity handleMethodArgumentNotValid
            ( MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request ) {

        List<String> errors = new ArrayList<String>();

        ex.getBindingResult().getFieldErrors().stream().forEach( fieldError -> {

            errors.add(fieldError.getField() + ": " +fieldError.getDefaultMessage());
        });

        ex.getBindingResult().getGlobalErrors().stream().forEach( fieldError -> {

            errors.add(fieldError.getObjectName() + ": " +fieldError.getDefaultMessage());
        });

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);


        return super.handleExceptionInternal(ex, responseApi, headers, responseApi.getStatus(), request);
    }

    @Override
    protected org.springframework.http.ResponseEntity handleMissingServletRequestParameter
            (MissingServletRequestParameterException ex, HttpHeaders headers,
             HttpStatus status, WebRequest request) {


        String error = ex.getParameterName()+ "parameter is Missing";

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public org.springframework.http.ResponseEntity
    handleConstrainViolation(ConstraintViolationException ex, WebRequest request){

        List<String> errors = new ArrayList<>();

        ex.getConstraintViolations().stream().forEach( constraintViolation -> {
            errors.add(constraintViolation.getRootBeanClass().getName() + " " +
                    constraintViolation.getPropertyPath() + " : " +  constraintViolation.getMessage());
        });

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus()
                );
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public org.springframework.http.ResponseEntity handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request){

        String error = ex.getRootCause().getClass().getName();

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus()
        );
    }



    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public org.springframework.http.ResponseEntity handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus());
    }


    @Override
    protected org.springframework.http.ResponseEntity handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {


        StringBuilder builder = new StringBuilder();
            builder.append(ex.getMethod());
            builder.append(
                    "method is not supported for this request. Supported methods are    "
            );

            ex.getSupportedHttpMethods().forEach(httpMethod -> builder.append(headers + " "));

        ResponseApi responseApi = new ResponseApi(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage(), builder.toString());

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public org.springframework.http.ResponseEntity handleAll(Exception ex, WebRequest request){

        log.error("Exception throwable value {}", ex.getCause().fillInStackTrace());
        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getCause().fillInStackTrace().getCause().getCause().getMessage(),
                        ex.getCause().fillInStackTrace().getCause().getCause()
                       );

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus());
    }

    @ExceptionHandler({UserAlreadyExistException.class})
    public org.springframework.http.ResponseEntity handleUserAlreadyExistException(UserAlreadyExistException ex, WebRequest request){

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "user aleready exists");

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus());
    }

    @ExceptionHandler({SaverNotFoundByEmail.class})
    public org.springframework.http.ResponseEntity handleSaverNotFoundByEmail(SaverNotFoundByEmail ex, WebRequest request){

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Saver not found");

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus());
    }

    @ExceptionHandler({ResourceNotFound.class})
    public org.springframework.http.ResponseEntity handleResourceNotFoundById(ResourceNotFound ex, WebRequest request){

        ResponseApi responseApi =
                new ResponseApi(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Resource Not found");

        return new org.springframework.http.ResponseEntity(
                responseApi, new HttpHeaders(), responseApi.getStatus());
    }

    @ExceptionHandler(value = MyRestTemplateException.class)
    ResponseEntity<ErrorResponse> handleMyRestTemplateException(MyRestTemplateException ex, HttpServletRequest request) {
        log.error("An error happened while calling {} Downstream API: {}", ex.getError().getMessage(), ex.toString());
        return new ResponseEntity<>(new ErrorResponse(ex, request.getRequestURI()), ex.getStatusCode());
    }

}

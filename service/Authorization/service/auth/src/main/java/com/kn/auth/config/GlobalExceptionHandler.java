package com.kn.auth.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.kn.auth.responses.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private MappingJackson2JsonView jsonView = new MappingJackson2JsonView();

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView(jsonView);
        return modelAndView.addObject("error", ErrorResponse.createErrorResponseFromException(e));
    }
}

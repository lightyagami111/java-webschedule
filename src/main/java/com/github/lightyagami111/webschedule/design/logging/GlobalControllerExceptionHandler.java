/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.logging;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ivaylo
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    
    private static final Logger log = Logger.getLogger(GlobalControllerExceptionHandler.class.getName());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Exception handleUnknown(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ex;
    }
    
}

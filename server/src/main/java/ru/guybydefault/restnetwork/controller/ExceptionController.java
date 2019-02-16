package ru.guybydefault.restnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.ZoneOffset;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler
        implements ErrorController

{

    @ExceptionHandler(value = IllegalArgumentException.class)
    public String handleIllegalArgumentException(HttpServletRequest req, Model model, IllegalArgumentException ex) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
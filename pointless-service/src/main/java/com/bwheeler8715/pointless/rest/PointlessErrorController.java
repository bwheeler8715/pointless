package com.bwheeler8715.pointless.rest;

import com.bwheeler8715.pointless.json.ErrorResponseJson;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.OffsetDateTime;

@Controller
public class PointlessErrorController implements ErrorController {

    @RequestMapping(path = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ErrorResponseJson> handleError(HttpServletRequest pRequest) {
        ServletWebRequest requestAttributes = new ServletWebRequest(pRequest);
        String path = (String) requestAttributes.getAttribute("javax.servlet.error.request_uri", 0);
        ErrorResponseJson response = new ErrorResponseJson();
        response.setStausCode(HttpStatus.NOT_FOUND.value());
        response.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setMessage("Not Found");
        response.setPath(path);
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
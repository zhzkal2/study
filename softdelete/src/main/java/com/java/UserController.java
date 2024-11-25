package com.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/dd")
    public String dd() {

        log.trace("TRACE!!");
        log.debug("DEBUG!!");
        log.info("INFO!!");
        log.warn("WARN!!");
        log.error("ERROR!!");
        return "응애";
    }

}
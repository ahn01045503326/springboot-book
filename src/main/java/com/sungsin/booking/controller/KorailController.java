package com.sungsin.booking.controller;

import com.sungsin.booking.dto.Korail;
import com.sungsin.booking.service.KorailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/korail")
public class KorailController {

    private final KorailService korailService;

    @PostMapping("/book")
    public String booking(@RequestBody Korail korail) throws InterruptedException {
        korailService.booking(korail);
        return "ok";
    }

}

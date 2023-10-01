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

    @PostMapping("/booking")
    public String booking(@RequestBody Korail korail) {
        System.out.println(korail.getId());
        korailService.setting(korail);
        return "ok";
    }

}

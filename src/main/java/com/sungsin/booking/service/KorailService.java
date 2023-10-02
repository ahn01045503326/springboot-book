package com.sungsin.booking.service;

import com.sungsin.booking.dto.Korail;
import org.openqa.selenium.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KorailService {
    @Async
    public void booking(Korail korail) throws InterruptedException {
        // 코레일 접속
        WebDriver nowDriver = korail.setting();
        // 코레일 로그인
        nowDriver = korail.login(nowDriver);
        // 예약가능한 표 찾기
        nowDriver = korail.searching(nowDriver);
        // 예약하기
        nowDriver = korail.booking(nowDriver);
        // 결제하기
        korail.payment(nowDriver);


        // 결제완료 확인
//        nowDriver = korail.checkPayment(nowDriver);
        // WebDriver 종료
//        nowDriver.quit();
    }
}




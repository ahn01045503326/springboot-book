package com.sungsin.booking.service;

import com.sungsin.booking.dto.Korail;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class KorailService {

    public void setting(Korail korail) {
        // 크롬 드라이버 설정
        System.setProperty("webdriver.chrome.driver", "chromedriver"); // 리눅스, 맥
        // 크롬 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);

        // 코레일 접속
        driver.get("https://www.letskorail.com/ebizprd/EbizPrdTicketpr21100W_pr21110.do");

        WebDriver loginDriver = korail.login(driver);

        korail.booking(loginDriver);

        // 팝업창 모두 닫기
//        String main = driver.getWindowHandle();
//        for (String handle : driver.getWindowHandles()) {
//            System.out.println("handle : " + handle);
//            if (!handle.equals(main)) {
//                driver.switchTo().window(handle).close();
//            } else {
//
//            }
//        }


        // WebDriver 종료
//        driver.quit();
    }

    public WebDriver login(WebDriver driver, String id, String password) {
        // 로그인 페이지 접속
        WebElement loginPageElement = driver.findElement(By.cssSelector("img[alt='로그인']"));
        loginPageElement.click();

        // 로그인
        WebElement idElement = driver.findElement(By.id("txtMember"));
        idElement.sendKeys(id);
        WebElement passwordElement = driver.findElement(By.id("txtPwd"));
        passwordElement.sendKeys(password);
        WebElement checkElement = driver.findElement(By.className("btn_login"));
        checkElement.click();

        return driver;
    }

    public void booking(WebDriver driver) {
        // 출발역 설정
        WebElement startElement = driver.findElement(By.id("start"));
        String startName = "광명";
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1])", startElement, startName);

        // 도착역 설정
        WebElement getElement = driver.findElement(By.id("get"));
        String getName = "울산";
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1])", getElement, getName);

        // 출발일 설정
        // year
        WebElement s_yearElement = driver.findElement(By.id("s_year"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_yearSelect = new Select(s_yearElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        s_yearSelect.selectByVisibleText("2023");
        // month
        WebElement s_monthElement = driver.findElement(By.id("s_month"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_monthSelect = new Select(s_monthElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        s_monthSelect.selectByVisibleText("10");
        // day
        WebElement s_dayElement = driver.findElement(By.id("s_day"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_daySelect = new Select(s_dayElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        s_daySelect.selectByVisibleText("1");
        // hour
        WebElement s_hourElement = driver.findElement(By.id("s_hour"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_hourSelect = new Select(s_hourElement);
        // 옵션 선택 (값을 사용하여 선택)
        s_hourSelect.selectByValue("10");

        // 조회하기
        WebElement listElement = driver.findElement(By.cssSelector("img[alt='조회하기']"));
        listElement.click();

        // WebDriverWait 객체 생성 (최대 10초 대기)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 특정 조건을 기다림 (예: id가 "elementId"인 요소가 나타날 때까지 대기)
        // <table> 요소를 찾기 (예를 들어, id가 "tableId"인 <table> 요소를 찾음)
        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tableResult")));
        // <tbody> 요소를 태그 이름을 사용하여 찾기
        WebElement tbodyElement = tableElement.findElement(By.tagName("tbody"));
        // <tbody> 아래의 모든 <tr> 요소를 찾기
        List<WebElement> rows = tbodyElement.findElements(By.tagName("tr"));
        // 각 <tr> 요소에서 <td> 요소를 찾아 출력하기
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            System.out.println(cells.get(2).getText() + "\t");
            String s_hourCell = cells.get(2).getText().split("\n")[1].substring(0,2);
            if(s_hourCell.equals("14")) {
                System.out.println("예약!! : " + s_hourCell);
                // 좌석이 있는지 확인
                WebElement statusElement = null;
                try {
                    statusElement = cells.get(5).findElement(By.cssSelector("img[alt='좌석선택']"));
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    // 요소를 찾을 수 없는 경우
                }
                // 좌석이 있다면
                if(statusElement != null) {
                    // 예약
                    statusElement.click();
                    // 현재의 컨트롤을 알림 창으로 전환
                    Alert alert = driver.switchTo().alert();
                    // 알림 창의 텍스트를 얻어올 수도 있음
                    System.out.println("알림 메시지: " + alert.getText());
                    // 알림 창의 확인 버튼 클릭
                    alert.accept();

                    // 모달 창이 <iframe>으로 구현된 경우
                    WebElement iframeElement = driver.findElement(By.id("embeded-modal-seatmap"));
                    driver.switchTo().frame(iframeElement);

                    List<WebElement> seat1Element = null;
                    List<WebElement> seat2Element = null;
                    try {
                        // id가 "SSeat"이고 class가 "ck_seat_td1_on"인 요소 찾기
                        seat1Element = driver.findElements(By.cssSelector("#SSeat.ck_seat_td1_on"));
                        seat2Element = driver.findElements(By.cssSelector("#SSeat.ck_seat_td2_on"));
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        // 요소를 찾을 수 없는 경우
                    }

                    if(seat1Element != null) {
                        for(WebElement seatTd1Cell : seat1Element) {
                            System.out.println(seatTd1Cell);
                            seatTd1Cell.click();
                            driver.findElement(By.linkText("선택좌석예약하기")).click();
                        }
                    }
                    if(seat2Element != null) {
                        for(WebElement seatTd2Cell : seat2Element) {
                            System.out.println(seatTd2Cell);
                            seatTd2Cell.click();
                            driver.findElement(By.linkText("선택좌석예약하기")).click();
                        }
                    }

                } else {
                    // TODO 있을 때까지 반복
                }
            }
            System.out.println(); // 다음 행으로 넘어가기 위해 개행 문자 출력
        }
    }


}




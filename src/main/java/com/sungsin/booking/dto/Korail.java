package com.sungsin.booking.dto;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
public class Korail {

    // booking
    private String startName;   // 출발지역
    private String arriveName;  // 도착지역
    private String startYear;   // 년(조회)
    private String startMonth;  // 월(조회)
    private String startDay;    // 일(조회)
    private String startHour;   // 시간(조회)

    // login
    private String id;  // 코레일 id
    private String password;    // 코레일 password

    // payment
    private String firstNumber;     // 신용카드 첫번째 4자리
    private String secondNumber;    // 신용카드 두번째 4자리
    private String thirdNumber;     //신용카드 세번째 4자리
    private String fourthNumber;    // 신용카드 네번째 4자리
    private String expirationMonth; // 월 (유효기간)
    private String expirationYear;  // 년도 (유효기간)
    private String cardPassword;    // 카드 비밀번호 앞 2자리
    private String rrnNumber;       // 주민등록번호 앞 6자리


    /**
     * 코레일 접속
     *
     * @return
     */
    public WebDriver setting() {
        // 크롬 드라이버 설정
        System.setProperty("webdriver.chrome.driver", "chromedriver"); // 리눅스, 맥
        // 크롬 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);

        // 코레일 접속
        driver.get("https://www.letskorail.com/ebizprd/EbizPrdTicketpr21100W_pr21110.do");

        return driver;
    }

    /**
     * 코레일 로그인
     *
     * @param driver 코레일 접속 후 driver
     * @return 코레일 로그인된 driver
     */
    public WebDriver login(WebDriver driver) {
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

    /**
     * 예약가능한 표 찾기
     *
     * @param driver 코레일 로그인된 driver
     * @return 코레일 예약가능한 표 찾은 후 driver
     */
    public WebDriver searching(WebDriver driver) throws InterruptedException {
        // WebDriverWait 객체 생성 (최대 10초 대기)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // 특정 조건을 기다림 (예: id가 "elementId"인 요소가 나타날 때까지 대기)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn_res")));
        // 예약 페이지 접속
        driver.get("https://www.letskorail.com/ebizprd/EbizPrdTicketpr21100W_pr21110.do");

        // 출발역 설정
        WebElement startElement = driver.findElement(By.id("start"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1])", startElement, startName);
        // 도착역 설정
        WebElement getElement = driver.findElement(By.id("get"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1])", getElement, arriveName);

        // 출발일 설정
        // year
        WebElement s_yearElement = driver.findElement(By.id("s_year"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_yearSelect = new Select(s_yearElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        s_yearSelect.selectByValue(startYear);
        // month
        WebElement s_monthElement = driver.findElement(By.id("s_month"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_monthSelect = new Select(s_monthElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        s_monthSelect.selectByValue(startMonth);
        // day
        WebElement s_dayElement = driver.findElement(By.id("s_day"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_daySelect = new Select(s_dayElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        s_daySelect.selectByValue(startDay);
        // hour
        WebElement s_hourElement = driver.findElement(By.id("s_hour"));
        // Select 클래스로 <select> 요소를 감싸기
        Select s_hourSelect = new Select(s_hourElement);
        // 옵션 선택 (값을 사용하여 선택)
        s_hourSelect.selectByValue(startHour);

        // 좌석이 있을 때까지 반복
        Boolean noSeat = true;
        while (noSeat) {
            // 조회하기
            WebElement listElement = driver.findElement(By.cssSelector("img[alt='조회하기']"));
            listElement.click();

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
                if(s_hourCell.equals(startHour)) {
                    System.out.println("예약!! : " + s_hourCell);
                    // 좌석이 있는지 확인
                    WebElement statusElement = null;
                    try {
                        statusElement = cells.get(5).findElement(By.cssSelector("img[alt='좌석선택']"));
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        // 좌석이 없는 경우 (요소를 찾을 수 없는 경우)
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
                        // 제대로된 return
                        return driver;
                    } else {
                        // TODO 해당 시간에 예약 가능한 좌석이 없음
                    }
                } else {
                    // TODO 해당 시간에 운행하는 열차가 없음
                }
            }
            Thread.sleep(1000); //1초 대기
        }
        // 여기는 오지 않음..
        return driver;
    }

    /**
     * 예약하기
     *
     * @param driver 코레일 예약가능한 표 찾은 후 driver
     * @return 예약완료 후 driver
     */
    public WebDriver booking(WebDriver driver) {
        // WebDriverWait 객체 생성 (최대 10초 대기)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

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
                // 현재 컨트롤을 alert 창으로 전환
                while (true) {
                    try{
                        System.out.println("알림창 있음");
                        Alert paymentAlert = wait.until(ExpectedConditions.alertIsPresent());
                        paymentAlert.accept();
                    } catch (org.openqa.selenium.TimeoutException e) {
                        System.out.println("알림창 없음");
                        break;
                    }
                }
                driver.switchTo().defaultContent();
                driver.findElement(By.cssSelector("a[title='결제하기']")).click();
                return driver;
            }
        }

        if(seat2Element != null) {
            for(WebElement seatTd2Cell : seat2Element) {
                System.out.println(seatTd2Cell);
                seatTd2Cell.click();
                driver.findElement(By.linkText("선택좌석예약하기")).click();
                // 현재 컨트롤을 alert 창으로 전환
                while (true) {
                    try{
                        System.out.println("알림창 있음");
                        Alert paymentAlert = wait.until(ExpectedConditions.alertIsPresent());
                        paymentAlert.accept();
                    } catch (org.openqa.selenium.TimeoutException e) {
                        System.out.println("알림창 없음");
                        break;
                    }
                }
                driver.switchTo().defaultContent();
                driver.findElement(By.cssSelector("a[title='결제하기']")).click();
                return driver;
            }
        }
        // 여기는 오지 않음..
        return driver;
    }

    /**
     * 결제하기
     *
     * @param driver 예약완료 후 driver
     * @return 결제완료 후 driver
     */
    public void payment(WebDriver driver) {
        // WebDriverWait 객체 생성 (최대 5초 대기)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // 신용카드 결제 선택
        driver.findElement(By.id("tabStl1")).click();
        // 신용카드 첫째자리
        WebElement cardFirstNumber = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[title='신용카드 첫째자리']")));
        cardFirstNumber.sendKeys(firstNumber);
        // 신용카드 둘째자리
        WebElement cardSecondNumber = driver.findElement(By.cssSelector("input[title='신용카드 둘째자리']"));
        cardSecondNumber.sendKeys(secondNumber);
        // 신용카드 셋째자리
        WebElement cardThirdNumber = driver.findElement(By.cssSelector("input[title='신용카드 셋째자리']"));
        cardThirdNumber.sendKeys(thirdNumber);
        // 신용카드 넷째자리
        WebElement cardFourthNumber = driver.findElement(By.cssSelector("input[title='신용카드 넷째자리']"));
        cardFourthNumber.sendKeys(fourthNumber);

        // 유효기간 (월)
        WebElement monthElement = driver.findElement(By.id("month"));
        // Select 클래스로 <select> 요소를 감싸기
        Select monthSelect = new Select(monthElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        monthSelect.selectByValue(expirationMonth);
        // 유효기간 (년도)
        WebElement yearElement = driver.findElement(By.id("year"));
        // Select 클래스로 <select> 요소를 감싸기
        Select yearSelect = new Select(yearElement);
        // 옵션 선택 (텍스트를 사용하여 선택)
        yearSelect.selectByValue(expirationYear);

        // 신용카드 비밀번 (앞 두자리)
        WebElement passwordElement = driver.findElement(By.cssSelector("input[name='txtCCardPwd_1']"));
        passwordElement.sendKeys(cardPassword);

        // 주민번호 앞 6자리
        WebElement rrnElement = driver.findElement(By.cssSelector("input[name='txtJuminNo2_1']"));
        rrnElement.sendKeys(rrnNumber);

        // 개인정보 동의
        driver.findElement(By.id("chkAgree")).click();

        // 발권하기
        driver.findElement(By.id("fnIssuing")).click();

        // 코레일톡 발권
        // 모달 창이 <iframe>으로 구현된 경우
        WebElement iframeElement = driver.findElement(By.id("mainframeSaleInfo"));
        driver.switchTo().frame(iframeElement);
        driver.findElement(By.id("btn_next2")).click();
        Alert ticketingAlert = wait.until(ExpectedConditions.alertIsPresent());
        ticketingAlert.accept();

    }

    /**
     * 결제완료 확인하기
     *
     * @param driver 결제완료 후 driver
     * @return 결제완료 확인 페이지 driver
     */
    public WebDriver checkPayment(WebDriver driver) {
        // WebDriverWait 객체 생성 (최대 5초 대기)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // 특정 조건을 기다림 (예: id가 "elementId"인 요소가 나타날 때까지 대기)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[text()='결제가 완료되었습니다.']")));
        return driver;
    }

}

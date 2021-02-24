package ru.netology.web;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTest {

    public String getStringFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date date = cal.getTime();
        return dateFormat.format(date);
    }

    @BeforeEach
    void setUp(){
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Калуга");
        SelenideElement date = form.$$("[data-test-id=date] input").last();
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(getStringFormattedDate());
        form.$("[data-test-id=name] input").setValue("Евфимий Введенский");
        form.$("[data-test-id=phone] input").setValue("+78009379992");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").$(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
    }
}

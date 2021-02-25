package ru.netology.web;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String getStringFormattedTodayPlusDays(int days) {
        LocalDate today = LocalDate.now();
        return today.plusDays(days).format(formatter);
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        String formattedDate = getStringFormattedTodayPlusDays(5);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Калуга");
        SelenideElement date = form.$$("[data-test-id=date] input").last();
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(formattedDate);
        form.$("[data-test-id=name] input").setValue("Евфимий Введенский");
        form.$("[data-test-id=phone] input").setValue("+78009379992");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Забронировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно забронирована")).shouldBe(visible);
        $(withText(formattedDate)).shouldBe(visible);
    }

    @Test
    void shouldSubmitForm() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(7);
        String futureDay = Integer.toString(future.getDayOfMonth());

        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").sendKeys("Ка");
        $(byText("Калуга")).click();
        form.$("[data-test-id=date] button").click();

        if (today.getMonthValue() == future.getMonthValue()) {
            $(".calendar-input__calendar-wrapper")
                    .$(byText(futureDay)).click();
        }
        $$(".calendar__arrow").findBy(attribute("data-step", "1")).click();
        $(".calendar-input__calendar-wrapper")
                .$(byText(futureDay)).click();

        form.$("[data-test-id=name] input").setValue("Евфимий Введенский");
        form.$("[data-test-id=phone] input").setValue("+78009379992");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Забронировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно забронирована")).shouldBe(visible);
        $(withText(future.format(formatter))).shouldBe(visible);
    }
}

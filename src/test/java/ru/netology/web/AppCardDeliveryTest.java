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

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Калуга");
        SelenideElement date = form.$$("[data-test-id=date] input").last();
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(futureDate.format(formatter));
        form.$("[data-test-id=name] input").setValue("Евфимий Введенский");
        form.$("[data-test-id=phone] input").setValue("+78009379992");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Забронировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно забронирована")).shouldBe(visible);
        $(withText(futureDate.format(formatter))).shouldBe(visible);
    }

    @Test
    void shouldSubmitForm() {
        LocalDate defaultDate = LocalDate.now().plusDays(3);
        LocalDate futureDate = LocalDate.now().plusDays(7);
        String futureDay = Integer.toString(futureDate.getDayOfMonth());
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").sendKeys("Ка");
        $(byText("Калуга")).click();
        form.$("[data-test-id=date] button").click();
        if (defaultDate.getMonthValue() != futureDate.getMonthValue()) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $$("td.calendar__day").find(exactText(futureDay)).click();
        form.$("[data-test-id=name] input").setValue("Евфимий Введенский");
        form.$("[data-test-id=phone] input").setValue("+78009379992");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Забронировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно забронирована")).shouldBe(visible);
        $(withText(futureDate.format(formatter))).shouldBe(visible);
    }
}

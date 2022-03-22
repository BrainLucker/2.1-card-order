package ru.netology.web;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CallbackTest {
    @Test
    void shouldSubmitRequest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Ивлев Мак-сим");
        form.$("[data-test-id=phone] input").setValue("+79991234567");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=order-success]").shouldHave(text("Ваша заявка успешно принята!"));
    }
}
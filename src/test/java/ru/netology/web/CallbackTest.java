package ru.netology.web;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CallbackTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitValidForm() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Ивлев Мак-сим");
        form.$("[data-test-id=phone] input").setValue("+79991234567");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=order-success]").shouldHave(text("Ваша заявка успешно отправлена!"));
    }

    @Test
    void shouldNotSubmitEmptyForm() {
        $(".form").$(".button").click();
        $("[data-test-id=order-success]").shouldNot(exist);
    }

    @ParameterizedTest
    @CsvSource({
            "On English, Ivlev Maksim",
            "With symbol, Ивлев М@ксим",
            "Only spaces, '  '",
    })
    void shouldShowErrorIfInputtedInvalidName(String testName, String name) {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue("+79991234567");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible);
    }

    @ParameterizedTest
    @CsvSource({
            "Without plus, 79991234567",
            "With letter, +7999123456o",
            "With symbol, +7999123456.",
            "Too short, +7999123456",
            "Too long, +799912345678",
            "Only Spaces, '  '",
    })
    void shouldShowErrorIfInputtedInvalidPhone(String testName, String phone) {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Ивлев Максим");
        form.$("[data-test-id=phone] input").setValue(phone);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=phone].input_invalid .input__sub").shouldBe(visible);
    }

    @Test
    void shouldChangeCheckboxTextColorIfNotChecked() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Ивлев Максим");
        form.$("[data-test-id=phone] input").setValue("+79991234567");
        form.$(".button").click();
        form.$("[data-test-id=agreement].input_invalid .checkbox__text").shouldBe(visible);
    }
}
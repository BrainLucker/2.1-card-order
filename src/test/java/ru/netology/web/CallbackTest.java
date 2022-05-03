package ru.netology.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/* Запуск SUT:
java -jar artifacts/app-order.jar
*/

public class CallbackTest {
    private final String successOrderText = "Ваша заявка успешно отправлена!";
    private final String requiredField = "Поле обязательно для заполнения";
    private final String invalidNameError = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
    private final String invalidPhoneError = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678";
    private final SelenideElement form = $(".form");
    private final SelenideElement nameField = form.$("[data-test-id=name] input");
    private final SelenideElement phoneField = form.$("[data-test-id=phone] input");
    private final SelenideElement agreementCheckBox = form.$("[data-test-id=agreement]");
    private final SelenideElement button = form.$(".button");
    private final SelenideElement successMessage = $("[data-test-id=order-success]");

    @BeforeEach
    public void setUp() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x800";
        open("http://localhost:9999");
    }

    @Test
    public void shouldSubmitValidForm() {
        nameField.setValue("Ивлев Мак-сим");
        phoneField.setValue("+79991234567");
        agreementCheckBox.click();
        button.click();
        successMessage.shouldHave(text(successOrderText));
    }

    @Test
    public void shouldNotSubmitEmptyForm() {
        button.click();
        successMessage.shouldNot(exist);
        nameField.parent().parent().$(".input__sub").shouldBe(visible).shouldHave(text(requiredField));

    }

    @ParameterizedTest
    @CsvSource({
            "On English, Ivlev Maksim, " + invalidNameError,
            "With symbol, Ивлев М@ксим, " + invalidNameError,
            "Spaces only, '  ', " + requiredField
    })
    public void shouldShowErrorIfInputtedInvalidName(String testName, String name, String errorText) {
        nameField.setValue(name);
        phoneField.setValue("+79991234567");
        agreementCheckBox.click();
        button.click();
        nameField.parent().parent().$(".input__sub").shouldBe(visible).shouldHave(text(errorText));
    }

    @ParameterizedTest
    @CsvSource({
            "Without plus, 79991234567, " + invalidPhoneError,
            "With letter, +7999123456o, " + invalidPhoneError,
            "With symbol, +7999123456., " + invalidPhoneError,
            "Too short, +7999123456, " + invalidPhoneError,
            "Too long, +799912345678, " + invalidPhoneError,
            "Spaces only, '  ', " + requiredField
    })
    public void shouldShowErrorIfInputtedInvalidPhone(String testName, String phone, String errorText) {
        nameField.setValue("Ивлев Максим");
        phoneField.setValue(phone);
        agreementCheckBox.click();
        button.click();
        phoneField.parent().parent().$(".input__sub").shouldBe(visible).shouldHave(text(errorText));
    }

    @Test
    public void shouldChangeCheckboxTextColorIfNotChecked() {
        nameField.setValue("Ивлев Максим");
        phoneField.setValue("+79991234567");
        button.click();
        agreementCheckBox.parent().parent().$(".input__sub").shouldBe(visible);
    }
}
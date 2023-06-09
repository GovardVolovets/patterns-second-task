package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");

        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(withText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        Configuration.holdBrowserOpen = true;
        var notRegisteredUser = getUser("active");

        $("[name='login']").setValue(notRegisteredUser.getLogin());
        $("[name='password']").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").should(appear)
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        Configuration.holdBrowserOpen = true;
        var blockedUser = getRegisteredUser("blocked");

        $("[name='login']").setValue(blockedUser.getLogin());
        $("[name='password']").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").should(appear)
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        Configuration.holdBrowserOpen = true;
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").should(appear)
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").should(appear)
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}

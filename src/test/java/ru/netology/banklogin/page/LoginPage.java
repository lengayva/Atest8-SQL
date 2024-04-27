package ru.netology.banklogin.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.banklogin.data.DataHelper;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public void verifyErrorNotification(String expectedText) { //проверка сообщения об ошибке
        errorNotification.shouldHave(exactText(expectedText)).shouldHave(visible); //проверяет что у элемента есть текст и элемент виден
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) { //выполняет валидный логин, принимает данные аутентификации
        loginField.setValue(info.getLogin()); // заполнил поля
        passwordField.setValue(info.getPassword());
        loginButton.click();  //нажал
        return new VerificationPage();  //вернул новую страницу
    }
}

package ru.netology.banklogin.test;

import org.junit.jupiter.api.*;
import ru.netology.banklogin.page.LoginPage;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;
import ru.netology.banklogin.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.banklogin.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage; //объявили, как переменную экземпляра класса

    @AfterEach
    void tearDown() {  //чистит коды верификации
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() { // после всех тестов чистится база данных, после каждого теста нужно перезапускать джарник, иначе не получится валидного логина
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class); //перед каждым тестом открывается страница и ей присваивается значение loginPage
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData(); //получили валидные данные для аутентификации
        var verificationPage = loginPage.validLogin(authInfo); //на странице loginPage ввели валидные данные, нажали кнопку
        verificationPage.verifyVerificationPageVisibility(); // проверили что виден элемент поле ввода кода и считаем это основанием перехода на страницу верификации
        var verificationCode = SQLHelper.getVerificationCode();  //получили код верификации
        verificationPage.validVerify(verificationCode.getCode()); //ввел код, и верную новый экземпляр DashboardPage
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();  //вводим данные случайного пользователя
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldNotAuthWithInValidCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();  //вводим случайный код верификации
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }

/*    @Test
    @DisplayName("Should block user if he input invalid code three times")
    void shouldBlockUserIfInputInvalidCodeThreeTimes() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Система заблокирована!");
    }*/
}
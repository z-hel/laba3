package com.example.tester3.pizza;

public enum PaymentMethod {
    Cash("Наличные"),
    BankCard("Банковская карта"),
    YandexMoney("Яндекс.Деньги"),
    WebMoney("WebMoney"),
    SberbankOnline("Сбербанк Онлайн"),
    QIWI("QIWI-кошелек");

    private String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public static PaymentMethod fromString(String strValue) {

        switch (strValue) {
            case "Наличные":
                return Cash;
            case "Банковская карта":
                return BankCard;
            case "Яндекс.Деньги":
                return YandexMoney;
            case "WebMoney":
                return WebMoney;
            case "Сбербанк Онлайн":
                return SberbankOnline;
            case "QIWI-кошелек":
                return QIWI;
        }

        return null;
    }
}

package localization.message;

public class UkraineMessages extends Messages {

    protected UkraineMessages() {
        super();
    }

    @Override
    protected void init() {
        init_error = "Виникла помилка при ініціалізації. Спробуйте ще." +
                "\nЯкщо помилка не зникне перевстановіть програму або зверніться до розробника." +
                "\nvassbassapp@gmail.com";
        init_success = "Ініціалізація пройшла успішно";

        //Channel
        modifyChannel_error = "Виникла помилка при зміні інформації про канал. Будь ласка спробуйте ще раз.";
    }
}

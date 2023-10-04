package localization.message;

public class UkraineMessages extends Messages {

    protected UkraineMessages() {
        super();
    }

    @Override
    protected void init() {
        init_mainScreen = "Ініціалізація головного екрану";
        init_repository = "Ініціалізація репозіторіїв";
        exec_channelListService = "Запуск сервісу \"список вимірювальних каналів\"";
        init_measurementService = "Ініціалізація сервісу \"вимірювання\"";
        init_calibratorService = "Ініціалізація сервісу \"калібратор\"";
        init_importService = "Ініціалізація сервісу \"імпорт\"";
        init_sensorErrorService = "Ініціалізація сервісу \"похибка ПВП\"";
        init_sensorsTypesService = "Ініціалізація сервісу \"тип ПВП\"";
        init_controlPointsService = "Ініціалізація сервісу \"контрольні точки\"";
        init_personService = "Ініціалізація сервісу \"працівники\"";
        init_convertorTCService = "Ініціалізація сервісу \"перетворювач величин ТО\"";
        init_calculationMethodsService = "Ініціалізація сервісу \"метод розрахунку\"";
        init_converterService = "Ініціалізація сервісу \"перетворювач вимірювальних величин\"";
        init_archivingService = "Ініціалізація сервісу \"архівування протоколів\"";
        init_error = "Виникла помилка при ініціалізації. Спробуйте ще." +
                "\nЯкщо помилка не зникне перевстановіть програму або зверніться до розробника." +
                "\nvassbassapp@gmail.com";
    }
}

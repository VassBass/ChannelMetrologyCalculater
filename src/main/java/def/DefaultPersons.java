package def;

import constants.WorkPositions;
import model.Person;

import java.util.ArrayList;

public class DefaultPersons {
    public static ArrayList<Person> get() {
        ArrayList<Person>persons = new ArrayList<>();

        Person chekunovTM = new Person();
        chekunovTM.setId(1);
        chekunovTM.setName("Тимофій");
        chekunovTM.setSurname("Чекунов");
        chekunovTM.setPatronymic("Миколайович");
        chekunovTM.setPosition(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP);
        persons.add(chekunovTM);

        Person fesenkoEV = new Person();
        fesenkoEV.setId(2);
        fesenkoEV.setName("Євгеній");
        fesenkoEV.setSurname("Фесенко");
        fesenkoEV.setPatronymic("Вітальйович");
        fesenkoEV.setPosition(WorkPositions.HEAD_OF_AREA + " МЗтаП");
        persons.add(fesenkoEV);

        Person lenTV = new Person();
        lenTV.setId(3);
        lenTV.setName("Тетяна");
        lenTV.setSurname("Лень");
        lenTV.setPatronymic("Володимирівна");
        lenTV.setPosition(WorkPositions.ENGINEER_ASKV);
        persons.add(lenTV);

        Person pohiliiOO = new Person();
        pohiliiOO.setId(4);
        pohiliiOO.setName("Олександр");
        pohiliiOO.setSurname("Похилий");
        pohiliiOO.setPatronymic("Олександрович");
        pohiliiOO.setPosition(WorkPositions.HEAD_OF_AREA + " АСУТП");
        persons.add(pohiliiOO);

        Person sergienkoOV = new Person();
        sergienkoOV.setId(5);
        sergienkoOV.setName("Олександр");
        sergienkoOV.setSurname("Сергієнко");
        sergienkoOV.setPatronymic("Вікторович");
        sergienkoOV.setPosition(WorkPositions.HEAD_OF_AREA + " АСУТП");
        persons.add(sergienkoOV);

        Person vasilevIS = new Person();
        vasilevIS.setId(6);
        vasilevIS.setName("Ігор");
        vasilevIS.setSurname("Васильєв");
        vasilevIS.setPatronymic("Сергійович");
        vasilevIS.setPosition(WorkPositions.ELECTRONIC_ENGINEER);
        persons.add(vasilevIS);

        return persons;
    }
}

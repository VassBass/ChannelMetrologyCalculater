package def;

import model.Person;

import java.util.ArrayList;

public class DefaultPersons {
    public static ArrayList<Person> get() {
        ArrayList<Person>persons = new ArrayList<>();

        Person chekunovTM = new Person();
        chekunovTM.setName("Тимофій");
        chekunovTM.setSurname("Чекунов");
        chekunovTM.setPatronymic("Миколайович");
        chekunovTM.setPosition(Person.HEAD_OF_DEPARTMENT_ASUTP);
        persons.add(chekunovTM);

        Person fesenkoEV = new Person();
        fesenkoEV.setName("Євгеній");
        fesenkoEV.setSurname("Фесенко");
        fesenkoEV.setPatronymic("Вітальйович");
        fesenkoEV.setPosition(Person.HEAD_OF_AREA + " МЗтаП");
        persons.add(fesenkoEV);

        Person lenTV = new Person();
        lenTV.setName("Тетяна");
        lenTV.setSurname("Лень");
        lenTV.setPatronymic("Володимирівна");
        lenTV.setPosition(Person.ENGINEER_ASKV);
        persons.add(lenTV);

        Person pohiliiOO = new Person();
        pohiliiOO.setName("Олександр");
        pohiliiOO.setSurname("Похилий");
        pohiliiOO.setPatronymic("Олександрович");
        pohiliiOO.setPosition(Person.HEAD_OF_AREA + " АСУТП");
        persons.add(pohiliiOO);

        Person sergienkoOV = new Person();
        sergienkoOV.setName("Олександр");
        sergienkoOV.setSurname("Сергієнко");
        sergienkoOV.setPatronymic("Вікторович");
        sergienkoOV.setPosition(Person.HEAD_OF_AREA + " АСУТП");
        persons.add(sergienkoOV);

        Person vasilevIS = new Person();
        vasilevIS.setName("Ігор");
        vasilevIS.setSurname("Васильєв");
        vasilevIS.setPatronymic("Сергійович");
        vasilevIS.setPosition(Person.ELECTRONIC_ENGINEER);
        persons.add(vasilevIS);

        return persons;
    }
}
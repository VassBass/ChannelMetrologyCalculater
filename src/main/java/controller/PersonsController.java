package controller;

import constants.Strings;
import constants.WorkPositions;
import model.Model;
import model.Worker;
import repository.Repository;
import support.Comparator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PersonsController {
    private Window window;
    private ArrayList<Worker> persons;

    public void init(Window window){
        try {
            this.persons = new Repository<Worker>(null, Model.PERSON).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_PERSONS.getName() + "\" is empty");
            this.persons = this.resetToDefault();
        }
        this.window = window;
    }

    public ArrayList<Worker> resetToDefault() {
        if (this.persons == null){
            this.persons = new ArrayList<>();
        }else this.persons.clear();

        Worker chekunovTM = new Worker();
        chekunovTM.setName("Тимофій");
        chekunovTM.setSurname("Чекунов");
        chekunovTM.setPatronymic("Миколайович");
        chekunovTM.setPosition(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP);
        this.persons.add(chekunovTM);

        Worker fesenkoEV = new Worker();
        fesenkoEV.setName("Євген");
        fesenkoEV.setSurname("Фесенко");
        fesenkoEV.setPatronymic("Вітальйович");
        fesenkoEV.setPosition(WorkPositions.HEAD_OF_AREA + " МЗтаП");
        this.persons.add(fesenkoEV);

        Worker lenTV = new Worker();
        lenTV.setName("Тетяна");
        lenTV.setSurname("Лень");
        lenTV.setPatronymic("Володимирівна");
        lenTV.setPosition(WorkPositions.JUNIOR_ENGINEER);
        this.persons.add(lenTV);

        Worker pohiliiOO = new Worker();
        pohiliiOO.setName("Олександр");
        pohiliiOO.setSurname("Похилий");
        pohiliiOO.setPatronymic("Олександрович");
        pohiliiOO.setPosition(WorkPositions.HEAD_OF_AREA + " АСУТП");
        this.persons.add(pohiliiOO);

        Worker sergienkoOV = new Worker();
        sergienkoOV.setName("Олександр");
        sergienkoOV.setSurname("Сергієнко");
        sergienkoOV.setPatronymic("Вікторович");
        sergienkoOV.setPosition(WorkPositions.HEAD_OF_AREA + " АСУТП");
        this.persons.add(sergienkoOV);

        Worker vasilevIS = new Worker();
        vasilevIS.setName("Ігор");
        vasilevIS.setSurname("Васильєв");
        vasilevIS.setPatronymic("Сергійович");
        vasilevIS.setPosition(WorkPositions.ELECTRONIC_ENGINEER);
        this.persons.add(vasilevIS);

        this.save();
        return this.persons;
    }

    public ArrayList<Worker> getAll() {
        return this.persons;
    }

    public ArrayList<Worker> add(Worker worker) {
        this.persons.add(worker);
        this.save();
        return this.persons;
    }

    public ArrayList<Worker> remove(Worker person) {
        boolean removed = false;

        for (Worker worker : this.persons){
            if (Comparator.personsMatch(worker, person)){
                this.persons.remove(worker);
                removed = true;
                break;
            }
        }

        if (removed){
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.persons;
    }

    public ArrayList<Worker> set(Worker oldPerson, Worker newPerson) {
        if (oldPerson != null){
            if (newPerson == null){
                this.remove(oldPerson);
            }else {
                if (!Comparator.personsMatch(oldPerson, newPerson)) {
                    for (int p=0;p<this.persons.size();p++){
                        Worker person = this.persons.get(p);
                        if (Comparator.personsMatch(person, oldPerson)){
                            this.persons.set(p, newPerson);
                            break;
                        }
                    }
                }
            }
            this.save();
        }
        return this.persons;
    }

    public int getIndex(Worker person) {
        for (int index=0;index<this.persons.size();index++) {
            Worker p = this.persons.get(index);
            if (Comparator.personsMatch(p, person)) {
                return index;
            }
        }
        this.showNotFoundMessage();
        return -1;
    }

    public Worker get(int index) {
        if (index >= 0) {
            return this.persons.get(index);
        }else {
            return null;
        }
    }

    public void clear() {
        this.persons.clear();
        this.save();
    }

    private void save() {
        new Repository<Worker>(this.window, Model.PERSON).writeList(this.persons);
    }

    private void showNotFoundMessage() {
        String message = "Працівник не знайден в списку працівників.";
        JOptionPane.showMessageDialog(this.window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}

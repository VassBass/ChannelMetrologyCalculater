package def;

import java.util.ArrayList;

public class DefaultProcesses {
    public static ArrayList<String> get() {
        ArrayList<String>processes = new ArrayList<>();

        String barmak = "Бармак";
        String section = "Секція";
        String tract = "Тракт";
        String line = "Технологічна лінія";

        processes.add(barmak);
        processes.add(section);
        processes.add(tract);
        processes.add(line);

        return processes;
    }
}

package constants;

import java.io.File;

public class Files {

    public static final File MAIN_DIR = new File(Strings.MAIN_DIR_NAME);

    public static final File LIST_DIR = new File(MAIN_DIR, Strings.DIR_NAME_LISTS);
    public static final File CERTIFICATES_DIR = new File(MAIN_DIR, Strings.DIR_NAME_CERTIFICATES);
    public static final File EXPORT_DIR = new File(MAIN_DIR, Strings.DIR_NAME_EXPORT);
    public static final File FORMS_DIR = new File(MAIN_DIR, "forms");

    public static final File FILE_LIST_SENSORS = new File(LIST_DIR, Strings.FILE_NAME_SENSORS);
    public static final File FILE_LIST_CHANNELS = new File(LIST_DIR, Strings.FILE_NAME_CHANNELS);
    public static final File FILE_LIST_DEPARTMENTS = new File(LIST_DIR, Strings.FILE_NAME_DEPARTMENTS);
    public static final File FILE_LIST_AREAS = new File(LIST_DIR, Strings.FILE_NAME_AREAS);
    public static final File FILE_LIST_PROCESSES = new File(LIST_DIR, Strings.FILE_NAME_PROCESSES);
    public static final File FILE_LIST_INSTALLATIONS = new File(LIST_DIR, Strings.FILE_NAME_INSTALLATIONS);
    public static final File FILE_LIST_CALIBRATORS = new File(LIST_DIR, Strings.FILE_NAME_CALIBRATORS);
    public static final File FILE_LIST_MEASUREMENTS = new File(LIST_DIR, Strings.FILE_NAME_MEASUREMENTS);
    public static final File FILE_LIST_PERSONS = new File(LIST_DIR, Strings.FILE_NAME_PERSONS);
    
    public static final File FILE_FORM_MKMX_5300_01_18_BAD = new File(FORMS_DIR, Strings.FILE_NAME_FORM_MKMX_5300_01_18_BAD);
    public static final File FILE_FORM_MKMX_5300_02_18_BAD = new File(FORMS_DIR, Strings.FILE_NAME_FORM_MKMX_5300_02_18_BAD);
    public static final File FILE_FORM_MKMX_5300_01_18_GOOD = new File(FORMS_DIR, Strings.FILE_NAME_FORM_MKMX_5300_01_18_GOOD);
    public static final File FILE_FORM_MKMX_5300_02_18_GOOD = new File(FORMS_DIR, Strings.FILE_NAME_FORM_MKMX_5300_02_18_GOOD);
    public static final File FILE_FORM_MKMX_5300_01_18_GOOD_v3_5 = new File(FORMS_DIR, Strings.FILE_NAME_FORM_MKMX_5300_01_18_GOOD_v3_5);
    public static final File FILE_FORM_MKMX_5300_01_18_BAD_v3_5 = new File(FORMS_DIR, Strings.FILE_NAME_FORM_MKMX_5300_01_18_BAD_v3_5);

    public static File certificateFile(String fileName){
        File file = new File(CERTIFICATES_DIR, fileName);
        if (!file.exists()){
            try {
                if (!file.createNewFile()){
                    System.out.println("certificateFile was not created");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
        return file;
    }

    public static void create() {
        try {
            //Main folder
            if (!MAIN_DIR.exists() && !MAIN_DIR.mkdir()) {
                System.out.println("mainDir was not created");
            }

            //Folders
            if (!LIST_DIR.exists() && !LIST_DIR.mkdir()) {
                System.out.println("listDir was not created");
            }

            if (!CERTIFICATES_DIR.exists() && !CERTIFICATES_DIR.mkdir()){
                System.out.println("certificatesDir was not created");
            }

            if (!EXPORT_DIR.exists() && !EXPORT_DIR.mkdir()){
                System.out.println("exportDir was not created");
            }
            
            if (!FORMS_DIR.exists() && !FORMS_DIR.mkdir()){
                System.out.println("formsDir was not created");
            }

            //Files
            if (!FILE_LIST_SENSORS.exists() && !FILE_LIST_SENSORS.createNewFile()) {
                System.out.println("sensors list was not created");
            }
            if (!FILE_LIST_CHANNELS.exists() && !FILE_LIST_CHANNELS.createNewFile()) {
                System.out.println("channels list was not created");
            }
            if (!FILE_LIST_DEPARTMENTS.exists() && !FILE_LIST_DEPARTMENTS.createNewFile()) {
                System.out.println("departments list was not created");
            }
            if (!FILE_LIST_AREAS.exists() && !FILE_LIST_AREAS.createNewFile()) {
                System.out.println("areas list was not created");
            }
            if (!FILE_LIST_PROCESSES.exists() && !FILE_LIST_PROCESSES.createNewFile()) {
                System.out.println("processes list was not created");
            }
            if (!FILE_LIST_INSTALLATIONS.exists() && !FILE_LIST_INSTALLATIONS.createNewFile()) {
                System.out.println("installations list was not created");
            }
            if (!FILE_LIST_CALIBRATORS.exists() && !FILE_LIST_CALIBRATORS.createNewFile()) {
                System.out.println("calibrators list was not created");
            }
            if (!FILE_LIST_MEASUREMENTS.exists() && !FILE_LIST_MEASUREMENTS.createNewFile()) {
                System.out.println("measurements list was not created");
            }
            if (!FILE_LIST_PERSONS.exists() && !FILE_LIST_PERSONS.createNewFile()){
                System.out.println("persons list was not created");
            }
            
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getFileExtension(File file){
        if (file.exists()){
            char[]chars = file.getName().toCharArray();
            int index = -1;
            for (int x=0;x<chars.length;x++){
                if (chars[x] == '.'){
                    index = x + 1;
                }
            }
            if (index != -1) {
                return file.getName().substring(index);
            }else {
                return null;
            }
        }else {
            return null;
        }
    }
}

package support;

import constants.Files;
import constants.Strings;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class UnZipper {
    
    public void unzip(){
        try {
            String zip = "forms/".concat(Strings.FILE_NAME_FORM_MKMX_5300_01_18_BAD);
            InputStream in = UnZipper.class.getClassLoader().getResourceAsStream(zip);
            if (in == null){
                System.out.println("File no find at: " + zip);
            }else {
                Path out = Paths.get(Files.FILE_FORM_MKMX_5300_01_18_BAD.getAbsolutePath());
                java.nio.file.Files.copy(in, out, REPLACE_EXISTING);
                in.close();
            }
            
            zip = "forms/".concat(Strings.FILE_NAME_FORM_MKMX_5300_01_18_GOOD);
            in = UnZipper.class.getClassLoader().getResourceAsStream(zip);
            if (in == null){
                System.out.println("File no find at: " + zip);
            }else {
                Path out = Paths.get(Files.FILE_FORM_MKMX_5300_01_18_GOOD.getAbsolutePath());
                java.nio.file.Files.copy(in, out, REPLACE_EXISTING);
                in.close();
            }

            zip = "forms/"
                    + Strings.FILE_NAME_FORM_MKMX_5300_01_18_BAD_v3_5;
            in = UnZipper.class.getClassLoader().getResourceAsStream(zip);
            if (in == null){
                System.out.println("File no find at: " + zip);
            }else {
                Path out = Paths.get(Files.FILE_FORM_MKMX_5300_01_18_BAD_v3_5.getAbsolutePath());
                java.nio.file.Files.copy(in, out, REPLACE_EXISTING);
                in.close();
            }

            zip = "forms/"
                    + Strings.FILE_NAME_FORM_MKMX_5300_01_18_GOOD_v3_5;
            in = UnZipper.class.getClassLoader().getResourceAsStream(zip);
            if (in == null){
                System.out.println("File no find at: " + zip);
            }else {
                Path out = Paths.get(Files.FILE_FORM_MKMX_5300_01_18_GOOD_v3_5.getAbsolutePath());
                java.nio.file.Files.copy(in, out, REPLACE_EXISTING);
                in.close();
            }
            
            zip = "forms/".concat(Strings.FILE_NAME_FORM_MKMX_5300_02_18_GOOD);
            in = UnZipper.class.getClassLoader().getResourceAsStream(zip);
            if (in == null){
                System.out.println("File no find at: " + zip);
            }else {
                Path out = Paths.get(Files.FILE_FORM_MKMX_5300_02_18_GOOD.getAbsolutePath());
                java.nio.file.Files.copy(in, out, REPLACE_EXISTING);
                in.close();
            }
            
            zip = "forms/".concat(Strings.FILE_NAME_FORM_MKMX_5300_02_18_BAD);
            in = UnZipper.class.getClassLoader().getResourceAsStream(zip);
            if (in == null){
                System.out.println("File no find at: " + zip);
            }else {
                Path out = Paths.get(Files.FILE_FORM_MKMX_5300_02_18_BAD.getAbsolutePath());
                java.nio.file.Files.copy(in, out, REPLACE_EXISTING);
                in.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
}

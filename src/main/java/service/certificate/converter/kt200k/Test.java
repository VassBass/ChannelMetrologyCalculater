package service.certificate.converter.kt200k;

import service.certificate.converter.kt200k.reader.XmlCertificateReader;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File file = new File("/home/vass/IdeaProjects/KT_Certificates/20060101_21-02_TS.xml");

        XmlCertificateReader reader = new XmlCertificateReader();
        System.out.println(reader.read(file));
    }
}

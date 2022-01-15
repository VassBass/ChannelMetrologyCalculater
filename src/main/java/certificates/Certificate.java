package certificates;

import calculation.Calculation;
import model.Channel;

import java.io.File;
import java.util.HashMap;

public interface Certificate {

    void init(Calculation result, HashMap<Integer, Object> values, Channel channel);
    void formation();
    void putCertificateData();
    void putChannelData();
    void putSensorData();
    void putCalibratorData();
    void putResult();
    void putPersons();

    void save();
    void show();
    void print();
    void openInExplorer();
    File getCertificateFile();
}

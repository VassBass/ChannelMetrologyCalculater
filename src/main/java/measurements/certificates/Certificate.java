package measurements.certificates;

import measurements.calculation.Calculation;
import model.Channel;
import support.Values;

import java.io.File;

public interface Certificate {

    void init(Calculation result, Values values, Channel channel);
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

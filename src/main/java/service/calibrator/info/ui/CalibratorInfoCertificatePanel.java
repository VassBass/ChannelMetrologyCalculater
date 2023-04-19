package service.calibrator.info.ui;

import model.dto.Calibrator;

import javax.annotation.Nullable;

public interface CalibratorInfoCertificatePanel {
    @Nullable Calibrator.Certificate getCertificate();
    void setCertificateInfo(Calibrator.Certificate certificate);
}

package service.calibrator.info.ui.swing;

import model.dto.Calibrator;
import model.ui.*;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import service.calibrator.info.ui.CalibratorInfoCertificatePanel;
import util.DateHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.SwingConstants.CENTER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingCalibratorInfoCertificatePanel extends TitledPanel implements CalibratorInfoCertificatePanel {
    private static final String TITLE_TEXT = "Документ про придатність калібратора";
    private static final String NAME_LABEL_TEXT = "Назва:";
    private static final String COMPANY_LABEL_TEXT = "Видав документ:";

    private final DefaultComboBox type;
    private final DatePanel datePanel;
    private final TitledTextField name;
    private final TitledTextField company;

    public SwingCalibratorInfoCertificatePanel(@Nonnull RepositoryFactory repositoryFactory) {
        super(TITLE_TEXT, Color.BLACK);
        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);

        type = new DefaultComboBox(true);
        datePanel = new DatePanel();
        name = new TitledTextField(20, NAME_LABEL_TEXT);
        company = new TitledTextField(20, COMPANY_LABEL_TEXT);

        List<String> types = new ArrayList<>();
        types.add(EMPTY);
        types.addAll(calibratorRepository.getAll().stream()
                .map(c -> c.getCertificate().getType())
                .distinct()
                .collect(Collectors.toList()));
        type.setList(types);

        datePanel.setDate(DateHelper.dateToString(Calendar.getInstance()));

        this.add(type, new CellBuilder().x(0).y(0).width(1).build());
        this.add(new DefaultLabel(" від "), new CellBuilder().x(1).y(0).width(1).build());
        this.add(datePanel, new CellBuilder().x(2).y(0).width(1).build());
        this.add(name, new CellBuilder().x(0).y(1).width(3).build());
        this.add(company, new CellBuilder().x(0).y(2).width(3).build());
    }

    @Nullable
    @Override
    public Calibrator.Certificate getCertificate() {
        String type = this.type.getSelectedString();
        String date = datePanel.getDate();
        String name = this.name.getText();
        String company = this.company.getText();

        if (StringHelper.nonEmpty(type, date, name, company)) {
            return new Calibrator.Certificate.CertificateBuilder()
                    .setType(type)
                    .setDate(date)
                    .setName(name)
                    .setCompany(company)
                    .build();
        } else return null;
    }

    @Override
    public void setCertificateInfo(Calibrator.Certificate certificate) {
        type.setSelectedItem(certificate.getType());
        datePanel.setDate(certificate.getDate());
        name.setText(certificate.getName());
        company.setText(certificate.getCompany());
    }

    private static class DatePanel extends DefaultPanel {
        private static final String DAY_TOOLTIP_TEXT = "День";
        private static final String MONTH_TOOLTIP_TEXT = "Місяць";
        private static final String YEAR_TOOLTIP_TEXT = "Рік";

        private final IntegerTextField dayField;
        private final IntegerTextField monthField;
        private final IntegerTextField yearField;

        public DatePanel() {
            super();

            dayField = new IntegerTextField(2, DAY_TOOLTIP_TEXT, CENTER);
            monthField = new IntegerTextField(2, MONTH_TOOLTIP_TEXT, CENTER);
            yearField = new IntegerTextField(4, YEAR_TOOLTIP_TEXT, CENTER);
            DefaultLabel dot1 = new DefaultLabel(".", CENTER);
            DefaultLabel dot2 = new DefaultLabel(".", CENTER);

            this.add(dayField, new CellBuilder().x(0).build());
            this.add(dot1, new CellBuilder().x(1).build());
            this.add(monthField, new CellBuilder().x(2).build());
            this.add(dot2, new CellBuilder().x(3).build());
            this.add(yearField, new CellBuilder().x(4).build());
        }

        public void setDate(String date) {
            String[] splttedDate = DateHelper.getSplittedDate(date);
            dayField.setText(splttedDate[0]);
            monthField.setText(splttedDate[1]);
            yearField.setText(splttedDate[2]);
        }

        public String getDate() {
            String date = String.format("%s.%s.%s",
                    dayField.getText(),
                    monthField.getText(),
                    yearField.getText());
            return DateHelper.isDateValid(date) ? date : EMPTY;
        }
    }
}

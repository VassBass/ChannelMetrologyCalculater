package service.measurement.info.ui.swing;

import model.dto.Measurement;
import model.dto.MeasurementTransformFactor;
import model.ui.ButtonCell;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import service.measurement.info.ui.MeasurementInfoFactorsPanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static model.ui.builder.CellBuilder.HORIZONTAL;

public class SwingMeasurementInfoFactorsPanel extends TitledPanel implements MeasurementInfoFactorsPanel {

    private final List<FactorPanel> factorPanel;

    public SwingMeasurementInfoFactorsPanel(@Nonnull RepositoryFactory repositoryFactory, @Nullable Measurement oldMeasurement) {
        super(Objects.nonNull(oldMeasurement) ?
                String.format("1 %s =", oldMeasurement.getValue()) : "1 =",
                Color.BLACK);
        this.factorPanel = new ArrayList<>();

        if (Objects.nonNull(oldMeasurement)) {
            MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
            this.setFactorOutputList(new ArrayList<>(factorRepository.getBySource(oldMeasurement.getValue())));
        }
    }

    @Override
    public void setFactorOutputList(List<MeasurementTransformFactor> factorList) {
        factorPanel.clear();
        this.removeAll();
        for (int y = 0; y < factorList.size(); y++) {
            MeasurementTransformFactor transformFactor = factorList.get(y);
            FactorPanel panel = new FactorPanel(transformFactor.getTransformTo(), transformFactor.getTransformFactor());
            this.add(panel, new CellBuilder().fill(HORIZONTAL).y(y).build());
            factorPanel.add(panel);
        }
    }

    @Override
    @Nullable
    public Map<String, Double> getFactorList() {
        if (factorPanel.stream().noneMatch(p -> Double.isNaN(p.getFactorValue()))) {
            this.setTitleColor(Color.BLACK);
            return factorPanel.stream().collect(Collectors.toMap(FactorPanel::getMeasurementValue, FactorPanel::getFactorValue));
        } else {
            this.setTitleColor(Color.RED);
            return null;
        }
    }

    @Override
    public void setFactorInput(String input) {
        this.setTitleText(String.format("1 %s =", input));
    }

    private static class FactorPanel extends DefaultPanel {

        private final ButtonCell toMeasurementValue;
        private final DefaultTextField factorValue;

        private FactorPanel(String toMeasurementValue, double factorValue) {
            super();

            this.toMeasurementValue = new ButtonCell(ButtonCell.SIMPLE, toMeasurementValue);
            String value = StringHelper.roundingDouble(factorValue, StringHelper.FOR_LAST_ZERO);
            this.factorValue = new DefaultTextField(10, value);
            this.factorValue.setText(value);

            this.add(this.toMeasurementValue, new CellBuilder().x(0).build());
            this.add(this.factorValue, new CellBuilder().x(1).build());
        }

        private String getMeasurementValue() {
            return this.toMeasurementValue.getText();
        }

        private double getFactorValue() {
            String factor = factorValue.getText();
            if (StringHelper.isDouble(factor)) {
                return Double.parseDouble(factor);
            } else return Double.NaN;
        }
    }
}

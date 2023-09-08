package service.export_import;

import model.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class ExportDataValidator {
    private static final Logger logger = LoggerFactory.getLogger(ExportDataValidator.class);

    private static final String NOT_VALID_MESSAGE_FORMAT = "Not valid export data of %s";

    private ExportDataValidator(){}

    public static boolean isValid(Map<Entity, Collection<?>> exportData) {
        Collection<?> buffer = exportData.get(Entity.CALIBRATOR);
        boolean valid = true;

        for (Object o : buffer) {
            valid = o instanceof Calibrator;
            if (!valid) {
                logger.warn(String.format(NOT_VALID_MESSAGE_FORMAT, Calibrator.class));
            }
        }

        buffer = exportData.get(Entity.CHANNEL);
        for (Object o : buffer) {
            valid = o instanceof Channel;
            logger.warn(String.format(NOT_VALID_MESSAGE_FORMAT, Channel.class));
        }

        buffer = exportData.get(Entity.CONTROL_POINTS);
        for (Object o : buffer) {
            if (!valid) break;
            valid = o instanceof ControlPoints;
            logger.warn(String.format(NOT_VALID_MESSAGE_FORMAT, ControlPoints.class));
        }

        buffer = exportData.get(Entity.MEASUREMENT);
        for (Object o : buffer) {
            if (!valid) break;
            valid = o instanceof Calibrator;
            logger.warn(String.format(NOT_VALID_MESSAGE_FORMAT, Calibrator.class));
        }

        buffer = exportData.get(Entity.MEASUREMENT_TRANSFORM_FACTOR);
        for (Object o : buffer) {
            boolean valid = o instanceof Calibrator;
            if (!valid) return false;
        }

        buffer = exportData.get(Entity.PERSON);
        if (!buffer.isEmpty() && buffer.stream().noneMatch(e -> e instanceof Person)) {
            return false;
        }

        buffer = exportData.get(Entity.SENSOR);
        if (!buffer.isEmpty() && buffer.stream().noneMatch(e -> e instanceof Sensor)) {
            return false;
        }

        buffer = exportData.get(Entity.SENSOR_ERROR);
        if (!buffer.isEmpty() && buffer.stream().noneMatch(e -> e instanceof SensorError)) {
            return false;
        }

        return true;
    }
}

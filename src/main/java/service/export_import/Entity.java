package service.export_import;

import java.io.Serializable;

public enum Entity implements Serializable {
    CALIBRATOR,
    CHANNEL,
    CONTROL_POINTS,
    MEASUREMENT,
    MEASUREMENT_TRANSFORM_FACTOR,
    PERSON,
    SENSOR,
    SENSOR_ERROR
}

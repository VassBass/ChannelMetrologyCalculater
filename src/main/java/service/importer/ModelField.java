package service.importer;

/**
 * Contains old version db fields
 */
public enum ModelField {
    /**
     * Calibrator fields
     * @see model.dto.Calibrator
     */
    CALIBRATOR_TYPE,
    CALIBRATOR_NAME,
    CALIBRATOR_CERTIFICATE,
    CALIBRATOR_NUMBER,
    CALIBRATOR_MEASUREMENT_NAME,
    CALIBRATOR_MEASUREMENT_VALUE,
    CALIBRATOR_RANGE_MIN,
    CALIBRATOR_RANGE_MAX,
    CALIBRATOR_ERROR_FORMULA,
    /**
     * Calibrator certificate fields
     * @see model.dto.Calibrator.Certificate
     */
    CALIBRATOR_CERTIFICATE_TYPE,
    CALIBRATOR_CERTIFICATE_NAME,
    CALIBRATOR_CERTIFICATE_DATE,
    CALIBRATOR_CERTIFICATE_COMPANY,
    /**
     * Channel fields
     * @see model.dto.Channel
     */
    CHANNEL_CODE,
    CHANNEL_NAME,
    CHANNEL_MEASUREMENT_NAME,
    CHANNEL_MEASUREMENT_VALUE,
    CHANNEL_DEPARTMENT,
    CHANNEL_AREA,
    CHANNEL_PROCESS,
    CHANNEL_INSTALLATION,
    CHANNEL_DATE,
    CHANNEL_FREQUENCY,
    CHANNEL_TECHNOLOGY_NUMBER,
    CHANNEL_PROTOCOL_NUMBER,
    CHANNEL_RANGE_MIN,
    CHANNEL_RANGE_MAX,
    CHANNEL_REFERENCE,
    CHANNEL_SUITABILITY,
    CHANNEL_ALLOWABLE_ERROR_PERCENT,
    CHANNEL_ALLOWABLE_ERROR_VALUE,
    CHANNEL_SENSOR_NAME,
    CHANNEL_SENSOR_JSON,
    /**
     * Control points fields
     * @see model.dto.ControlPoints
     */
    CONTROL_POINTS_NAME,
    CONTROL_POINTS_SENSOR_TYPE,
    CONTROL_POINTS_VALUES,
    CONTROL_POINTS_RANGE_MIN,
    CONTROL_POINTS_RANGE_MAX,

    /**
     * Measurement fields
     * @see model.dto.Measurement
     */
    MEASUREMENT_NAME,
    MEASUREMENT_VALUE,
    MEASUREMENT_FACTORS,
    /**
     * Measurement transform factor fields
     * @see model.dto.MeasurementTransformFactor
     */
    MEASUREMENT_TRANSFORM_FACTOR_ID,
    MEASUREMENT_TRANSFORM_FACTOR_SOURCE,
    MEASUREMENT_TRANSFORM_FACTOR_RESULT,
    MEASUREMENT_TRANSFORM_FACTOR_FACTOR,
    MEASUREMENT_TRANSFORM_FACTOR_JSON,
    /**
     * Person fields
     * @see model.dto.Person
     */
    PERSON_ID,
    PERSON_SURNAME,
    PERSON_NAME,
    PERSON_PATRONYMIC,
    PERSON_POSITION,
    /**
     * Sensor fields
     * @see model.dto.Sensor
     */
    SENSOR_CHANNEL_CODE,
    SENSOR_TYPE,
    SENSOR_RANGE_MIN,
    SENSOR_RANGE_MAX,
    SENSOR_SERIAL_NUMBER,
    SENSOR_MEASUREMENT_NAME,
    SENSOR_MEASUREMENT_VALUE,
    SENSOR_ERROR_FORMULA,
    SENSOR_NAME,
    /**
     * Department fields
     */
    DEPARTMENT,
    /**
     * Area fields
     */
    AREA,
    /**
     * Process fields
     */
    PROCESS,
    /**
     * Installation fields
     */
    INSTALLATION
}

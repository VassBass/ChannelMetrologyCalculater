package service.converter_tc.converter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static service.converter_tc.model.Type.*;

public class Converter_GOST6651_2009Test {
    private static final Converter converter = new Converter_GOST6651_2009();

    @Test
    public void testTransformFromTemperature() {
        //Pt100
        assertEquals(92.159898432, converter.transformFromTemperature(Pt, 100, -20), 0.000_000_1);
        assertEquals(100, converter.transformFromTemperature(Pt, 100, 0), 0.000_000_1);
        assertEquals(157.325125, converter.transformFromTemperature(Pt, 100, 150), 0.000_000_1);
        //Pl100
        assertEquals(92.03822032, converter.transformFromTemperature(Pl, 100, -20), 0.000_000_1);
        assertEquals(100, converter.transformFromTemperature(Pl, 100, 0), 0.000_000_1);
        assertEquals(158.220775, converter.transformFromTemperature(Pl, 100, 150), 0.000_000_1);
        //Cu50
        assertEquals(45.711409128, converter.transformFromTemperature(Cu, 50, -20), 0.000_000_1);
        assertEquals(50, converter.transformFromTemperature(Cu, 50, 0), 0.000_000_1);
        assertEquals(82.1, converter.transformFromTemperature(Cu, 50, 150), 0.000_000_1);
    }

    @Test
    public void transformFromResistance() {
        //Pt100
        assertEquals(-20, converter.transformFromResistance(Pt, 100, 92.159898432), 0.7);
        assertEquals(0, converter.transformFromResistance(Pt, 100, 100), 0.02);
        assertEquals(150, converter.transformFromResistance(Pt, 100, 157.325125), 0.02);
        //Pl100
        assertEquals(-20, converter.transformFromResistance(Pl, 100, 92.03822032), 0.7);
        assertEquals(0, converter.transformFromResistance(Pl, 100, 100), 0.02);
        assertEquals(150, converter.transformFromResistance(Pl, 100, 158.220775), 0.02);
        //Cu50
        assertEquals(-20, converter.transformFromResistance(Cu, 50, 45.711409128), 0.7);
        assertEquals(0, converter.transformFromResistance(Cu, 50, 50), 0.02);
        assertEquals(150, converter.transformFromResistance(Cu, 50, 82.1), 0.02);
    }
}
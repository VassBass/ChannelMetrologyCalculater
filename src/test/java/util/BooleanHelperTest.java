package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanHelperTest {

    @Test
    public void isAllTrue() {
        assertTrue(BooleanHelper.isAllTrue(true, true, true));
        assertFalse(BooleanHelper.isAllTrue(true, false, true));
        assertFalse(BooleanHelper.isAllTrue(false, false, false));
    }
}
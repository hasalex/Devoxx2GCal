package org.sewatech.devoxx.gcal;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.sewatech.devoxx.model.Event;
import org.sewatech.devoxx.model.StaticUserCredentials;

/**
 * Created : 25 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class ScheduleCopierTest {
    private static final Logger logger = Logger.getLogger(ScheduleCopierTest.class);
    private ScheduleCopier scheduleCopier;


    @Before
    public void setUp() throws Exception {
        scheduleCopier = new ScheduleCopier();
        scheduleCopier.fromScheduleId = Event.DEVOXX2010_ID;
        scheduleCopier.toGCalTitle = "Test Devoxx";
        scheduleCopier.userCredentials = new StaticUserCredentials();

    }

    @Test
    public void testCopy() throws Exception {
        scheduleCopier.copy();
    }
}

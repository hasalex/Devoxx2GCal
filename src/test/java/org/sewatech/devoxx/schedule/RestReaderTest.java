package org.sewatech.devoxx.schedule;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.sewatech.devoxx.model.Event;
import org.sewatech.devoxx.model.Presentation;
import org.sewatech.devoxx.model.ScheduleItem;

import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created : 21 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class RestReaderTest {
    private static final Logger logger = Logger.getLogger(RestReaderTest.class);
    private RestReader reader;

    @Before
    public void setUp() throws Exception {
        reader = new RestReader();
    }

    @Test
    public void testGetEvents() throws Exception {
        Event[] result = reader.getEvents();
        assertNotNull(result);
    }

    @Test
    public void testGetEvent() throws Exception {
        RestReader reader = new RestReader();
        Event result = reader.getEvent(Event.DEVOXX2010_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetPresentations() throws Exception {
        Presentation[] result = reader.getPresentations(Event.DEVOXX2010_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetSchedule() throws Exception {
        ScheduleItem[] result = reader.getSchedule(Event.DEVOXX2010_ID);
        assertNotNull(result);
        print(result);
    }

    private void print(Object obj) {
        if (obj == null) {
            System.out.println(obj);
        } else if (obj.getClass().isArray()) {
            StringBuffer buffer = new StringBuffer("");
            for (Object element : (Object[]) obj) {
                buffer.append(element.toString()).append('\n');
            }
            logger.info(buffer);

        } else {
            logger.info(obj);
        }
    }

    @Test
    public void tmp() {
        String[] tzs = TimeZone.getAvailableIDs();
        for (String tz : tzs) {
            System.out.println(tz);
        }
    }

}

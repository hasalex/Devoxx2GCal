package org.sewatech.devoxx.gcal;

import com.google.gdata.data.calendar.CalendarEntry;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sewatech.devoxx.model.Presentation;
import org.sewatech.devoxx.model.ScheduleItem;
import org.sewatech.devoxx.model.Speaker;
import org.sewatech.devoxx.model.StaticUserCredentials;
import org.sewatech.devoxx.model.UserCredentials;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created : 25 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class GCalendarTest {
    private static final Logger logger = Logger.getLogger(GCalendarTest.class);
    private GCalendar gcal;
    private static final String CALENDAR_NAME = "Test Calendar";

    @Before
    public void setUp() throws Exception {
        UserCredentials userCredentials = new StaticUserCredentials();
        gcal = new GCalendar(CALENDAR_NAME, userCredentials);
    }

    @After
    public void tearDown() throws Exception {
        if (gcal != null) {
           gcal.delete();
        }
    }

    @Test
    public void testFind() throws Exception {
        CalendarEntry calEntry = GCalendar.find(CALENDAR_NAME);
        assertNotNull(calEntry);
    }

    @Test
    public void testAddEvents() throws Exception {
        ScheduleItem[] scheduleItems = new ScheduleItem[2];
        scheduleItems[0] = newItem(0);
        scheduleItems[1] = newItem(1);

        gcal.add(scheduleItems);
    }

    private ScheduleItem newItem(Integer itemNumber) {
        boolean odd = itemNumber % 2 == 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.add(Calendar.DAY_OF_MONTH, itemNumber);

        ScheduleItem item = new ScheduleItem();
        if (odd) {
            item.title = "test n°" + itemNumber;
        }
        item.fromTime = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        item.toTime = calendar.getTime();
        item.id = itemNumber;
        item.code = "CODE" + itemNumber;
        item.kind = "Kind" + itemNumber;
        item.partnerSlot = odd;
        item.room = "room n°" + itemNumber;
        item.type = "TYPE" + itemNumber;

        if (odd) {
            Speaker speaker0 = new Speaker();
            speaker0.speaker = "Sam Oine";
            Speaker speaker1 = new Speaker();
            speaker1.speaker = "Auzaire Oine";
            item.speakers = new Speaker[2];
            item.speakers[0] = speaker0;
            item.speakers[1] = speaker1;
        }

        if (odd) {
            item.presentation = new Presentation();
            item.presentation.speakers = item.speakers;
            item.presentation.experience = "Novice";
            item.presentation.summary = "Summary";
            item.presentation.title = item.title;
        }
        return item;
    }
}

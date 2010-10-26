package org.sewatech.devoxx.gcal;

import org.apache.log4j.Logger;
import org.sewatech.devoxx.model.Event;
import org.sewatech.devoxx.model.ScheduleItem;
import org.sewatech.devoxx.model.UserCredentials;
import org.sewatech.devoxx.schedule.RestReader;

/**
 * Created : 25 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class ScheduleCopier {
    private static final Logger logger = Logger.getLogger(ScheduleCopier.class);
    public UserCredentials userCredentials;
    public Integer fromScheduleId;
    public String toGCalTitle;

    public void copy() throws Exception {
        RestReader reader = new RestReader();
        Event event = reader.getEvent(fromScheduleId);
        GCalendar gcal = new GCalendar(toGCalTitle, userCredentials);

        logger.info("Starting to read the schedule");
        ScheduleItem[] items = reader.getSchedule(fromScheduleId);
        logger.info("Adding " + items.length + " schedule items to GCal");
        gcal.add(items);
        logger.info("Done");
    }
}

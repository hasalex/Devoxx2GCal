package org.sewatech.devoxx.gcal;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ILink;
import com.google.gdata.data.Link;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclNamespace;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.calendar.CalendarAclRole;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;
import org.sewatech.devoxx.model.Presentation;
import org.sewatech.devoxx.model.ScheduleItem;
import org.sewatech.devoxx.model.Speaker;
import org.sewatech.devoxx.model.UserCredentials;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.TimeZone;

/**
 * Created : 25 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class GCalendar {
    private static final String OWNCALENDARS_URL = "https://www.google.com/calendar/feeds/default/owncalendars/full";
    private static final Logger logger = Logger.getLogger(GCalendar.class);

    private static transient CalendarService calService;

    private CalendarEntry calendarEntry;
    public String title = "Devoxx Schedule";

    /**
     * With standard calendar title
     *
     * @throws Exception
     */
    public GCalendar(UserCredentials userCredentials) throws Exception {
        init(userCredentials);
    }

    /**
     * For test purpose
     *
     * @param title
     * @throws Exception
     */
    public GCalendar(String title, UserCredentials userCredentials) throws Exception {
        this.title = title;
        init(userCredentials);
    }

    private void init(UserCredentials userCredentials) throws Exception {
        this.calService = new CalendarService("Devoxx GCal");
        calService.setUserCredentials(userCredentials.userName, userCredentials.password);

        this.calendarEntry = create(title);
        setPublic();
    }

    private static CalendarEntry create(String title) throws Exception {
        CalendarEntry calendarEntry = find(title);
        if (calendarEntry == null) {
            CalendarEntry calendar = new CalendarEntry();
            calendar.setTitle(new PlainTextConstruct(title));
            calendar.setSummary(new PlainTextConstruct(title + " as GCal"));
            calendar.setHidden(HiddenProperty.FALSE);
            calendar.setColor(new ColorProperty("#1B887A"));
            calendar.addLocation(new Where("", "Antwerpe", "Antwerpe"));
            calendar.setTimeZone(new TimeZoneProperty("Europe/Brussels"));

            // Insert the calendar
            URL postUrl = new URL(OWNCALENDARS_URL);
            return calService.insert(postUrl, calendar);
        } else {
            return calendarEntry;
        }
    }

    public void delete() throws Exception {
        calendarEntry.delete();
    }

    public void add(ScheduleItem[] scheduleItems) throws Exception {
        CalendarEventFeed batchRequest = new CalendarEventFeed();
        for (ScheduleItem scheduleItem : scheduleItems) {
            if (scheduleItem.title != null) {
                CalendarEventEntry eventEntry = asCalendarEventEntry(scheduleItem);
                BatchUtils.setBatchId(eventEntry, scheduleItem.id.toString());
                BatchUtils.setBatchOperationType(eventEntry, BatchOperationType.INSERT);
                batchRequest.getEntries().add(eventEntry);
            }
        }
        logger.info("Batch request to GCal");
        CalendarEventFeed batchResponse = calService.batch(getEditURL(true), batchRequest);
    }

    private URL getEditURL(boolean batch) throws IOException, ServiceException {
        String suffix = (batch ? "/batch" : "");
        final String urlAsText = calendarEntry.getLink(ILink.Rel.ALTERNATE, "application/atom+xml").getHref() + suffix;
        logger.debug(urlAsText);
        return new URL(urlAsText);
    }

    public void add(ScheduleItem scheduleItem) throws IOException, ServiceException {
        if (scheduleItem.title != null) {
            CalendarEventEntry eventEntry = asCalendarEventEntry(scheduleItem);
            CalendarEventEntry insertedEntry = calService.insert(getEditURL(false), eventEntry);
        }
    }

    private CalendarEventEntry asCalendarEventEntry(ScheduleItem scheduleItem) {
        CalendarEventEntry eventEntry = new CalendarEventEntry();

        buildEventTitle(scheduleItem, eventEntry);
        buildEventSummary(scheduleItem, eventEntry);
        buildEventPeriod(scheduleItem, eventEntry);
        eventEntry.addLocation(new Where(null, scheduleItem.room, scheduleItem.room));
        return eventEntry;
    }

    private void buildEventTitle(ScheduleItem scheduleItem, CalendarEventEntry eventEntry) {
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append(scheduleItem.title);
        String separator = " (";
        if (scheduleItem.speakers != null) {
            for (Speaker speaker : scheduleItem.speakers) {
                titleBuilder.append(separator).append(speaker.firstName == null ? "" : speaker.firstName.charAt(0) + ". ");
                titleBuilder.append(speaker.lastName);
                separator = ", ";
            }
            titleBuilder.append(')');
        }
        eventEntry.setTitle(new PlainTextConstruct(titleBuilder.toString()));
        logger.debug(eventEntry.getTitle().getPlainText());
    }

    private void buildEventSummary(ScheduleItem scheduleItem, CalendarEventEntry eventEntry) {
        Presentation presentation = scheduleItem.presentation;
        StringBuilder summaryBuilder = new StringBuilder();
        if (presentation != null) {
            summaryBuilder.append(presentation.summary);
            summaryBuilder.append("\n\nSpeakers :\n");
            for (Speaker speaker : presentation.speakers) {
                summaryBuilder.append("- ").append(speaker.speaker).append(", ").append(speaker.company).append('\n');
            }
            summaryBuilder.append("\nTrack : ").append(presentation.track);
            summaryBuilder.append("\nType : ").append(presentation.type);
            summaryBuilder.append("\nExperience : ").append(presentation.experience);
        }
        eventEntry.setContent(new PlainTextConstruct(summaryBuilder.toString()));
    }

    private void buildEventPeriod(ScheduleItem scheduleItem, CalendarEventEntry eventEntry) {
        When eventTimes = new When();
        logger.debug("From " + scheduleItem.fromTime + " to " + scheduleItem.toTime);
        TimeZone tzCet = TimeZone.getTimeZone("Europe/Brussels");
        
        eventTimes.setStartTime(new DateTime(scheduleItem.fromTime, tzCet));
        eventTimes.setEndTime(new DateTime(scheduleItem.toTime, tzCet));
        logger.debug("From " + eventTimes.getStartTime() + " to " + eventTimes.getEndTime());
        eventEntry.addTime(eventTimes);
    }

    private static List<CalendarEntry> findAll() throws Exception {
        // Send the request and print the response
        CalendarFeed resultFeed = calService.getFeed(new URL(OWNCALENDARS_URL), CalendarFeed.class);
        return resultFeed.getEntries();
    }                   

    private void setPublic() throws IOException, ServiceException {
        Link link = calendarEntry.getLink(AclNamespace.LINK_REL_ACCESS_CONTROL_LIST,
                Link.Type.ATOM);

        URL aclUrl = new URL(link.getHref());

        AclEntry aclEntry = new AclEntry();
        aclEntry.setScope(new AclScope(AclScope.Type.DEFAULT, null));
        aclEntry.setRole(CalendarAclRole.READ);
        calService.insert(aclUrl, aclEntry);
    }

    public static CalendarEntry find(String calName) throws Exception {
        URL feedUrl = new URL(OWNCALENDARS_URL);

        List<CalendarEntry> allCalendars = findAll();
        for (CalendarEntry calendarEntry : allCalendars) {
            if (calendarEntry.getTitle().getPlainText().equals(calName)) {
                return calendarEntry;
            }
        }

        return null;
    }
}

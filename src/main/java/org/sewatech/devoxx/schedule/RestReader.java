package org.sewatech.devoxx.schedule;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.sewatech.devoxx.model.Event;
import org.sewatech.devoxx.model.Presentation;
import org.sewatech.devoxx.model.ResourceCache;
import org.sewatech.devoxx.model.ScheduleItem;
import org.sewatech.devoxx.model.Speaker;

import javax.ws.rs.core.MediaType;

/**
 * Created : 21 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class RestReader {
    private static final Logger logger = Logger.getLogger(RestReader.class);
    public static final String EVENTS = "http://cfp.devoxx.com/rest/v1/events";

    public Event[] getEvents() {
        return get(EVENTS, Event[].class);
    }

    public Event getEvent(Integer id) {
        return get(EVENTS + "/" + id, Event.class);
    }
    
    public Presentation[] getPresentations(Integer eventId) {
        Presentation[] presentations = get(EVENTS + "/" + eventId + "/presentations", Presentation[].class);
        ResourceCache.put(presentations, EVENTS + "/presentations");
        return presentations;
    }
    public Speaker[] getSpeakers(Integer eventId) {
        Speaker[] speakers = get(EVENTS + "/" + eventId + "/speakers", Speaker[].class);
        ResourceCache.put(speakers, EVENTS + "/speakers");
        return speakers;
    }
    public ScheduleItem[] getSchedule(Integer eventId) {
        logger.debug("Getting whole schedule");
        initializeCache(eventId);

        ScheduleItem[] scheduleItems = get(EVENTS + "/" + eventId + "/schedule", ScheduleItem[].class);
        for (ScheduleItem scheduleItem : scheduleItems) {
            scheduleItem.loadDetails();
        }
        return scheduleItems;
    }

    private void initializeCache(Integer eventId) {
        getSpeakers(eventId);
        getPresentations(eventId);
    }

    public <T> T get(String uri, Class<T> clazz) {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(cc);

        WebResource resource = client.resource(uri);
        resource.accept(MediaType.APPLICATION_JSON_TYPE);
        //String response = resource.get(String.class);
        return resource.get(clazz);
    }
}

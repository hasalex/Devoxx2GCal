package org.sewatech.devoxx.model;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;

/**
 * Created : 21 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class Event extends Resource<Event> {
    private static final Logger logger = Logger.getLogger(Event.class);
    public static final int DEVOXX2010_ID = 1;
      
    @JsonDeserialize(using = DateAdapter.class)
    public Date to;
    @JsonDeserialize(using = DateAdapter.class)
    public Date from;
    public String name;
    public String description;
    public String location;
    public Boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != null ? !id.equals(event.id) : event.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

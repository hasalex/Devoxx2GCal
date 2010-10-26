package org.sewatech.devoxx.model;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.net.URI;
import java.util.Date;

/**
 * Created : 22 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ScheduleItem extends Resource<ScheduleItem> {
    private static final Logger logger = Logger.getLogger(ScheduleItem.class);

    public Boolean partnerSlot;
    @JsonDeserialize(using = DateAdapter.class)
    public Date toTime;
    @JsonDeserialize(using = DateAdapter.class)
    public Date fromTime;
    public String code;
    public String type;
    public String kind;
    public String room;

    public URI presentationUri;
    public Presentation presentation;
    public Speaker[] speakers;
    public String title;

    public void loadDetails() {
        logger.debug("Loading details");
        if (presentationUri != null) {
            presentation = new Presentation();
            presentation.uri = presentationUri;
            presentation.loadDetails();
        }

        if (speakers != null) {
            for (Speaker speaker : speakers) {
                speaker.loadDetails();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleItem that = (ScheduleItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", from '" + fromTime + " to " + toTime +
                '}';
    }
}

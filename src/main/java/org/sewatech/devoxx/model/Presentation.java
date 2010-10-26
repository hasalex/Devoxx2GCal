package org.sewatech.devoxx.model;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created : 22 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Presentation extends Resource<Presentation> {
    private static final Logger logger = Logger.getLogger(Presentation.class);
    
    public String summary;
    public String title;
    public Speaker[] speakers;
    public String track;
    public String experience;
    public String type;

    public void loadDetails() {
        super.loadDetails();
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

        Presentation that = (Presentation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Presentation{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

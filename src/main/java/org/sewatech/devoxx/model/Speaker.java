package org.sewatech.devoxx.model;

import org.apache.log4j.Logger;

import java.net.URI;

/**
 * Created : 22 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class Speaker extends Resource<Speaker> {
    private static final Logger logger = Logger.getLogger(Speaker.class);

    public String speaker;
    public URI speakerUri;

    public String lastName;
    public String bio;
    public String company;
    public String firstName;
    public String imageURI;

    @Override
    public void loadDetails() {
        logger.debug("Loading details");
        this.uri = speakerUri;
        super.loadDetails();
    }
}

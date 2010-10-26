package org.sewatech.devoxx.model;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URI;

/**
 * Created : 25 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Resource<T extends Resource> {
    private static final Logger logger = Logger.getLogger(Resource.class);

    public Integer id;
    public URI uri;
    private Class<T> resourceType;

    public Resource() {
        this.resourceType = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void loadDetails() {
        T details = get();
        for (Field field : resourceType.getFields()) {
            try {
                Object newValue = field.get(details);
                if (newValue != null) {
                    field.set(this, field.get(details));
                }
            } catch (IllegalAccessException e) {
                logger.warn(e);
            }
        }
    }

    public T get() {
        logger.debug("GET " + uri);
        T result = ResourceCache.get(uri.toString(), resourceType);
        if (result == null) {
            logger.debug("Getting it without cache");
            result = getWithoutCache();
        } else {
            logger.debug("Found in cache");            
        }
        return (T) result;
    }

    private T getWithoutCache() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(clientConfig);

        WebResource resource = client.resource(uri);
        resource.accept(MediaType.APPLICATION_JSON_TYPE);
        return resource.get(resourceType);
    }
}

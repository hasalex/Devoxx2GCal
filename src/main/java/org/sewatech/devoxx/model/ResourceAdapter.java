package org.sewatech.devoxx.model;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.deser.BeanDeserializer;
import org.codehaus.jackson.map.type.TypeFactory;

import java.io.IOException;

/**
 * Created : 25 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class ResourceAdapter extends JsonDeserializer<Resource> {
    private static final Logger logger = Logger.getLogger(ResourceAdapter.class);

    private BeanDeserializer deserializer;

    public ResourceAdapter() {
        deserializer = new BeanDeserializer(TypeFactory.fromCanonical(Resource.class.getName()));
    }

    @Override
    public Resource deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        logger.info("Deserializing resource");
        Resource result = (Resource) deserializer.deserialize(jsonParser, deserializationContext);
        return result;
    }
}

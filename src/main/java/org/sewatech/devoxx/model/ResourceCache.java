package org.sewatech.devoxx.model;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created : 26 oct. 2010
 *
 * @author Alexis Hassler
 * @since 1.0
 */
public class ResourceCache {
    private static final Logger logger = Logger.getLogger(ResourceCache.class);

    private static Map<String, Object> cache = new HashMap<String, Object>();

    public static <T> T get(String url, Class<T> clazz) {
        logger.debug("Getting from cache " + url);
        return (T) cache.get(url);
    }    
    public static void put(Resource<?>[] resources, String rootUrl) {
        for (Resource<?> resource : resources) {
            cache.put(rootUrl + "/" + resource.id, resource);
        }
    }
}

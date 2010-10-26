package org.sewatech.devoxx.model;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends JsonDeserializer<Date> {

    private static final Logger logger = Logger.getLogger(DateAdapter.class);
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        logger.debug("Deserializing date");
        try {
            return df.parse(jsonParser.getText());
        } catch (ParseException e) {
            return null;
        }
    }
}
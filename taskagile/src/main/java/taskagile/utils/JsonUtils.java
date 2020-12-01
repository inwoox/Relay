package taskagile.utils;

import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

  private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

  private JsonUtils() {
  }

  public static String toJson(Object object) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert object to JSON string", e);
      return null;
    }
  }

  public static <T> T toObject(String json, Class<T> clazz) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json, clazz);
    } catch (IOException e) {
      log.error("Failed to convert string `" + json + "` class `" + clazz.getName() + "`", e);
      return null;
    }
  }

  public static void write(Writer writer, Object value) throws IOException {
    new ObjectMapper().writeValue(writer, value);
  }

}

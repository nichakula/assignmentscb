package com.assignment.api.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

  public static String toJSON(Object o, boolean encode) {
    ObjectMapper mapper = new ObjectMapper();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    mapper.setDateFormat(df);
    mapper.setSerializationInclusion(Include.NON_NULL);
    try {
      String vals = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
      StringBuilder sb = new StringBuilder(0);
      if (encode) {
        for (int i = 0; i < vals.length(); i++) {
          final char ch = vals.charAt(i);
          if (ch <= 127)
            sb.append(ch);
          else
            sb.append("\\u").append(String.format("%04x", (int) ch));
        }
      } else {
        sb.append(vals);
      }

      return sb.toString();
    } catch (Exception e) {
      return "";
    }
  }

  public static <T> T toObject(final InputStream is, final Class<T> cz) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(is, cz);
    } catch (Exception e) {
    }
    return null;
  }

  public static <T> T string2Object(String json, final Class<T> cz) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(json, cz);
    } catch (Exception e) {
    }
    return null;
  }

  public static <T> T toObjectUTF8(byte[] jsons, Class<T> cz) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(new String(jsons, "UTF-8"), cz);
    } catch (Exception e) {
    }

    return null;
  }

  public static String getJsonFromUrl(String url) {
	  String jsonText = "";
	  try {
		  InputStream is = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			jsonText = JsonUtil.readAll(rd);
	  }catch(Exception ex) {
		  
	  }
	  return jsonText;
  }
  
  public static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
  
}

package org.google.code.translate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains convenient methods for grabbing information
 * from remote translation server.
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
public class TranslateHelper {
  private static String TRANSLATE_HOST_URL = "http://translate.google.com";
  private static String RESULT_START_SEQUENCE = "<span id=result_box class=\"short_text\"><span title";
  private static String RESULT_END_SEQ = "</span></span";

  private static String LANGUAGE_FROM_START_SEQUENCE = "<select id=gt-sl name=sl tabindex=0";
  private static String LANGUAGE_TO_START_SEQUENCE = "<select id=gt-tl name=tl tabindex=0";
  private static String LANGUAGE_END_SEQUENCE = "</select>";

  private String proxyLogin;
  private String proxyPassword;

  private static List<KeyValuePair> fromLanguage;
  private static List<KeyValuePair> toLanguage;

  public void setProxyLogin(String proxyLogin) {
    this.proxyLogin = proxyLogin;
  }

  public void setProxyPassword(String proxyPassword) {
    this.proxyPassword = proxyPassword;
  }

  public List<KeyValuePair> getFromLanguage() {
    return fromLanguage;
  }

  public List<KeyValuePair> getToLanguage() {
    return toLanguage;
  }

  public void retrieveLanguagePairs() throws IOException {
    URLConnection urlConnection = prepareURLConnection(TRANSLATE_HOST_URL);

    InputStream is = urlConnection.getInputStream();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    while (true) {
      int ch = is.read();

      if (ch == -1) {
        break;
      }

      baos.write(ch);
    }

    is.close();
    baos.close();

    String content = new String(baos.toByteArray(), "UTF-8");

    fromLanguage = processLanguage(LANGUAGE_FROM_START_SEQUENCE, LANGUAGE_END_SEQUENCE, content);
    toLanguage = processLanguage(LANGUAGE_TO_START_SEQUENCE, LANGUAGE_END_SEQUENCE, content);
  }

  private List<KeyValuePair> processLanguage(String start1, String end, String s) {
    List<KeyValuePair> language = new ArrayList<KeyValuePair>();

    String result = "";

    int index = s.indexOf(start1);

    if (index != -1) {
      String s2 = s.substring(index + start1.length());

      int index2 = s2.indexOf(end);

      if (index2 != -1) {
        result = s2.substring(0, index2);
      }
    }

    if (result != null) {
      result = result.trim();
    }

    String str2 = "value=";
    String str3 = "</option>";

    boolean ok = false;

    while (!ok) {
      if (result == null || result.trim().length() == 0) {
        ok = true;
      } else {
        int index2 = result.indexOf(str2);
        int index3 = result.indexOf(str3);

        String s2 = result.substring(index2 + str2.length(), index3);

        if (!s2.startsWith("auto") && !s2.startsWith("separator")) {
          int index4 = s2.substring(1).indexOf("\"");
          int index5 = s2.indexOf(">");

          String key = s2.substring(1, index4 + 1);
          String value = s2.substring(index5 + 1);

          language.add(new KeyValuePair(key, value));
        }

        result = result.substring(index3 + str3.length());
      }
    }

    return language;
  }

  public String translate(String request, String fromLanguage, String toLanguage) throws IOException {
    request = preProcess(request);

    String url = TRANSLATE_HOST_URL + "/translate_t" + "?" + "langpair=" + fromLanguage + "|" + toLanguage +
        "&text=" + request;

    TranslateHelper translateHelper = new TranslateHelper();

    URLConnection urlConnection = translateHelper.prepareURLConnection(url);

    String result = null;

    InputStream is = urlConnection.getInputStream();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    while (true) {
      int ch = is.read();

      if (ch == -1) {
        break;
      }

      baos.write(ch);
    }

    is.close();
    baos.close();

    String s = new String(baos.toByteArray(), "UTF-8");

    int index1 = s.indexOf(RESULT_START_SEQUENCE);

    if (index1 != -1) {
      String s2 = s.substring(index1 + RESULT_START_SEQUENCE.length());

      int index2 = s2.indexOf(RESULT_END_SEQ);

      if (index2 != -1) {
        int index3 = s2.indexOf(">");
        result = s2.substring(index3 + 1, index2);
      }
    }

    if (result != null) {
      return postProcess(result);
    }

    return request;
  }

  private URLConnection prepareURLConnection(String url)
      throws IOException {
    URLConnection urlConnection = new URL(url).openConnection();

    urlConnection.setRequestProperty("Accept", "*/*");
    urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; Maxthon; .NET CLR 1.1.4322)");
    urlConnection.setRequestProperty("Pragma", "no-cache");

    if (proxyLogin != null && proxyPassword != null) {
      urlConnection.setRequestProperty(
          "Proxy-Authorization",
          "Basic " + new sun.misc.BASE64Encoder().encode((proxyLogin + ":" + proxyPassword).getBytes()
          ));
    }

    return urlConnection;
  }

  private String preProcess(String text) {
    //text = text.replaceAll("\"", " ");
    text = text.replaceAll("<", "%20");
    text = text.replaceAll(">", "%20");
    text = text.replaceAll(" ", "%20");
    text = text.replaceAll("\n", "%20");
    text = text.replaceAll("\r", "%20");

    return text;
  }

  private String postProcess(String text) {
    text = text.trim();

    text = text.replaceAll("\\Q&quot;\\E", "\"");

    return text;
  }

}

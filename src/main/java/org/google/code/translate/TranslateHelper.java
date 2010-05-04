package org.google.code.translate;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.net.HttpConfigurable;

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

  private static String LANGUAGE_FROM_START_SEQUENCE = "<select class=sllangdropdown name=sl id=\"old_sl\"";
  private static String LANGUAGE_TO_START_SEQUENCE = "<select class=tllangdropdown name=tl id=\"old_tl\"";
  private static String LANGUAGE_END_SEQUENCE = "</select>";

  private static List<KeyValuePair> fromLanguage = new ArrayList<KeyValuePair>();
  private static List<KeyValuePair> toLanguage = new ArrayList<KeyValuePair>();
  /**
   * Creates new translate helper.
   *
   * @throws Exception the exception
   */
  public TranslateHelper() throws Exception {
    HttpConfigurable httpConfigurable = (HttpConfigurable)
        ApplicationManager.getApplication().getComponent("HttpConfigurable");

    if (httpConfigurable == null) {
      httpConfigurable = HttpConfigurable.getInstance();
    }
    if (httpConfigurable != null) {
      if (httpConfigurable.USE_HTTP_PROXY) {
        System.getProperties().put("proxySet", Boolean.valueOf(httpConfigurable.USE_HTTP_PROXY).toString());
        System.getProperties().put("proxyPort", Integer.toString(httpConfigurable.PROXY_PORT));
        System.getProperties().put("proxyHost", httpConfigurable.PROXY_HOST);

        System.getProperties().put("http.proxySet", Boolean.valueOf(httpConfigurable.USE_HTTP_PROXY).toString());
        System.getProperties().put("http.proxyPort", Integer.toString(httpConfigurable.PROXY_PORT));
        System.getProperties().put("http.proxyHost", httpConfigurable.PROXY_HOST);
      }
    }

    if (fromLanguage.size() == 0 || toLanguage.size() == 0) {
      prepareLangPairs();
    }
  }

  private void prepareLangPairs() throws Exception {
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

    String s = new String(baos.toByteArray(), "UTF-8");

    processlanguage(LANGUAGE_FROM_START_SEQUENCE, LANGUAGE_END_SEQUENCE, s, fromLanguage);
    processlanguage(LANGUAGE_TO_START_SEQUENCE, LANGUAGE_END_SEQUENCE, s, toLanguage);
  }

  private void processlanguage(String start1, String end, String s, List<KeyValuePair> language) {
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

        String s2 = result.substring(index2+str2.length(), index3);

        int index4 = s2.substring(1).indexOf("\"");
        int index5 = s2.indexOf(">");

        String key = s2.substring(1, index4 + 1);
        String value = s2.substring(index5 + 1);

        language.add(new KeyValuePair(key, value));

        result = result.substring(index3 + str3.length());
      }
    }
  }

  private URLConnection prepareURLConnection(String url)
      throws IOException {
    URLConnection urlConnection = new URL(url).openConnection();

    urlConnection.setRequestProperty("Accept", "*/*");
    urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; Maxthon; .NET CLR 1.1.4322)");
    urlConnection.setRequestProperty("Pragma", "no-cache");

    HttpConfigurable httpConfigurable = (HttpConfigurable)
        ApplicationManager.getApplication().getComponent("HttpConfigurable");

    if (httpConfigurable != null) {
      if (httpConfigurable.PROXY_AUTHENTICATION) {
        // proxy user and pass
        urlConnection.setRequestProperty(
            "Proxy-Authorization",
            "Basic " + new sun.misc.BASE64Encoder().encode((httpConfigurable.PROXY_LOGIN + ":" +
                httpConfigurable.getPlainProxyPassword()).getBytes()
            ));
      }
    }

    return urlConnection;
  }

  public List<KeyValuePair> getFromLanguage() {
    return fromLanguage;
  }

  public List<KeyValuePair> getToLanguage() {
    return toLanguage;
  }

  public String translate(String request, String fromLanguage, String toLanguage) throws Exception {
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
        result = s2.substring(index3+1, index2);
      }
    }
    
    if (result != null) {
      return postProcess(result);
    }

    return request;
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

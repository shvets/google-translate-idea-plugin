import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.google.code.translate.TranslateHelper;

public class TranslateHelperTest {

  private TranslateHelper helper;

  @Before
  public void before() {
    helper = new TranslateHelper();
  }

  @After
  public void after() {
    helper = null;
  }

  @Test
  public void testFromMessagesList() throws Exception {
    helper.retrieveLanguagePairs();
    
    Assert.assertEquals(helper.getFromLanguage().size(), 58);
  }

  @Test
  public void testToMessagesList() throws Exception {
    helper.retrieveLanguagePairs();

    Assert.assertEquals(helper.getToLanguage().size(), 59);
  }

  @Test
  public void testTranslate() throws Exception {
    helper.retrieveLanguagePairs();

    System.out.println(helper.translate("hello", "en", "ru"));
//    Assert.assertEquals(helper.translate("hello", "en", "ru"), "привет");
  }

}

import java.util.Date;

import org.junit.Test;

import play.mvc.Http.Response;
import play.test.FunctionalTest;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
 
    @Test
    public void getDate() {
        Date date = new Date(Long.valueOf("1313017229").longValue() * 1000);
        
        assertTrue(true);
    }
    
    
}
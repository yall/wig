import java.util.Date;
import java.util.List;

import org.junit.Test;

import play.Logger;
import play.mvc.Http.Response;
import play.test.FunctionalTest;
import wiki.Category;

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
    
    @Test
    public void testCategorySplitting() {
    	Category cat = new Category("a/b/c/d/e");
    	List<Category> split = cat.split();
    	for (Category c : split) {
    		Logger.info(c.name + " : " + c.path);
    	}
    	assertEquals(split.size(), 5);
    	
    }
    
    
}
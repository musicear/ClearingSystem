package resoft.junit;

import java.io.IOException;

import junit.framework.TestCase;
import resoft.basLink.packer.CfxFmtFilesCache;

/**
 * function:
 * User: albert lee
 * Date: 2005-9-27
 * Time: 11:41:34
 */
public class TestCfxFmtFilesCache extends TestCase{
    private CfxFmtFilesCache cache = CfxFmtFilesCache.getInstance();
    public void testCache() throws IOException {
        String one = cache.getCfxFmtFile("conf/pack/1000.xml");
        String two = cache.getCfxFmtFile("conf/pack/1000.xml");
        assertTrue(one==two);
    }
}

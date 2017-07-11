package longshu.easycontroller.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DateUtilTest
 * @author LongShu 2017年7月11日
 */
public class DateUtilTest {

    private static ExecutorService executorService;
    private Date now = new Date();

    private Callable<String> formatTask = new Callable<String>() {
        @Override
        public String call() throws Exception {
            return DateUtil.toStr(now);
        }
    };
    private Callable<Date> parseTask = new Callable<Date>() {
        @Override
        public Date call() throws Exception {
            return DateUtil.parseDate("2017-7-11", DateUtil.dateFormat);
        }
    };

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        int nThreads = 2 * Runtime.getRuntime().availableProcessors();
        System.out.println(nThreads);
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        executorService.shutdown();
    }

    /**
     * Test method for {@link longshu.easycontroller.util.DateUtil#parseDate(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testParseDateString() throws Exception {
        List<Future<Date>> results = new ArrayList<Future<Date>>();

        for (int i = 0; i < 16; i++) {
            Future<Date> future = executorService.submit(parseTask);
            results.add(future);
        }
        for (Future<Date> future : results) {
            System.out.println(future.get());
        }
    }

    /**
     * Test method for {@link longshu.easycontroller.util.DateUtil#parseDate(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testParseDateStringString() {

    }

    /**
     * Test method for {@link longshu.easycontroller.util.DateUtil#toStr(java.util.Date)}.
     * @throws Exception 
     */
    @Test
    public void testToStrDate() throws Exception {
        List<Future<String>> results = new ArrayList<Future<String>>();

        for (int i = 0; i < 16; i++) {
            Future<String> future = executorService.submit(formatTask);
            results.add(future);
        }
        for (Future<String> future : results) {
            System.out.println(future.get());
        }
    }

    /**
     * Test method for {@link longshu.easycontroller.util.DateUtil#toStr(java.util.Date, java.lang.String)}.
     */
    @Test
    public void testToStrDateString() {

    }

}

package longshu.easycontroller.demo;

import longshu.easycontroller.core.BeanFactory;
import longshu.easycontroller.core.config.ActionConfig;
import longshu.easycontroller.core.config.ActionMapping;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.core.config.Interceptors;
import longshu.easycontroller.core.view.FreeMarkerView;
import longshu.easycontroller.core.view.ViewType;
import longshu.easycontroller.demo.controller.IndexController;
import longshu.easycontroller.demo.controller.TestController;
import longshu.easycontroller.demo.interceptor.GlobalInterceptor;
import longshu.easycontroller.demo.interceptor.TestInterceptor;
import longshu.easycontroller.demo.module.MyBatisMapperModule;
import longshu.easycontroller.demo.module.ServiceModule;
import longshu.easycontroller.json.FastJson;
import longshu.easycontroller.util.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
import java.sql.Connection;
import java.util.Locale;

/**
 * DemoConfig
 *
 * @author LongShu 2017/06/20
 */
public class DemoConfig extends ActionConfig {
    private static final long serialVersionUID = 1L;

    @Override
    public void configConstant(Constants me) {
        prop = loadPropertyFile("demo.properties");
        me.setDevMode(getPropToBoolean("devMode", false));
        me.setViewType(ViewType.JSP);
        me.setViewExtension(".html");// 模板引擎使用,可以共存

        // 测试模板
        FreeMarkerView.getConfig().setLocale(Locale.CHINA);

        // 指定json实现
        me.setDefaultJson(new FastJson().setDatePattern(DateUtil.DATE_PATTERN));

        me.setErrorView(404, "/WEB-INF/jsps/error/404.jsp");
        me.setErrorView(500, "/WEB-INF/jsps/error/500.jsp");

        // 文件上传配置
        me.addAllowUploadFileType(getProp("allowFileType"));
        // 文件下载缓存配置
        me.setFileCacheSize(getPropToInt("fileCacheSize", 80));
        me.setMaxFileCachetSize(getPropToInt("maxFileCachetSize", 2 << 10));// 2M
    }

    @Override
    public void configModule(BeanFactory me) {
        me.addModule(new MyBatisMapperModule(), new ServiceModule());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configInterceptor(Interceptors me) {
        // 全局拦截器
        me.addGlobalInterceptor(GlobalInterceptor.class);
        // 给某个控制器指定拦截器
        me.addActionInterceptor(TestController.class, TestInterceptor.class);
    }

    @Override
    public void configMapping(ActionMapping me) {
        me.setBaseViewPath("/WEB-INF/jsps/");// 视图路径

        me.addStaticMapping("/assets/");
        me.addStaticMapping("/druid/");

        me.addMapping("/", IndexController.class);
        me.addMapping("/test", TestController.class);
    }

    @Override
    public void afterStart() {
        /*
        测试的数据库(启动后创建)
         */
        Reader config = null;
        Reader sql = null;
        SqlSession sqlSession = null;
        try {
            config = Resources.getResourceAsReader("mybatis-config.xml");
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(config);
            sqlSession = sessionFactory.openSession(true);

            Connection connection = sqlSession.getConnection();
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);

            sql = Resources.getResourceAsReader("easycontroller.sql");
            runner.runScript(sql);

            runner.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(sqlSession);
            IOUtils.closeQuietly(sql);
            IOUtils.closeQuietly(config);
        }
    }
}

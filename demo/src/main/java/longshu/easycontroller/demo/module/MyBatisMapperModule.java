package longshu.easycontroller.demo.module;

import org.mybatis.guice.XMLMyBatisModule;

/**
 * MyBatisMapperModule
 *
 * @author LongShu 2017/05/20
 */
public class MyBatisMapperModule extends XMLMyBatisModule {

    public MyBatisMapperModule() {
    }

    public MyBatisMapperModule(String classPathResource) {
        setClassPathResource(classPathResource);
    }

    @Override
    protected void initialize() {

    }

}

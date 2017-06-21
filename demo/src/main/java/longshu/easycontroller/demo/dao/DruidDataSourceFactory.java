package longshu.easycontroller.demo.dao;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * DruidDataSourceFactory 自定义实现mybatis 对 druid的数据源类
 * 
 * @author LongShu 2017/06/20
 */
public class DruidDataSourceFactory extends UnpooledDataSourceFactory {

    public DruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();
    }

}

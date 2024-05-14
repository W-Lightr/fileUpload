package cn.lightr.fileupload.config;

import cn.lightr.fileupload.constant.consist.StorageMode;
import cn.lightr.fileupload.storage.LocalStorageProcessor;
import cn.lightr.fileupload.storage.MinioStorageProcessor;
import cn.lightr.fileupload.storage.StorageProcessor;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Lightr
 * @date 2024/1/10 14:44
 * @Description 描述
 */
@Configuration
@EnableAspectJAutoProxy
public class Config {

    @ConfigurationProperties("spring.datasource.druid")
    @Bean(destroyMethod = "close", initMethod = "init")
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }
    /**
     * 配置druid的监控页功能
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(statViewServlet,"/druid/*");
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        // 配置参数添加
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    /**
     * 注册代码生成器
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    @Value("${storage.mode}")
    private String storageMode;
    @Bean
    public StorageProcessor storageProcessor() {
        StorageProcessor storageProcessor = null;
        if (StorageMode.MINIO.equalsIgnoreCase(storageMode)) {
            storageProcessor = new MinioStorageProcessor();
        }
        if (StorageMode.LOCAL.equalsIgnoreCase(storageMode)) {
            storageProcessor = new LocalStorageProcessor();
        }
        // 默认使用本地存储
        if (storageProcessor == null){
            storageProcessor = new LocalStorageProcessor();
        }
        return storageProcessor;
    }
}

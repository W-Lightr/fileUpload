package cn.lightr.fileupload.config;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Lightr
 * @date 2024/1/5 17:05
 * @Description swagger
 */
@Configuration
@Data
@ConfigurationProperties("swagger")
@EnableOpenApi
public class SwaggerConfiguration {
    /*** 是否开启swagger，生产环境一般关闭，所以这里定义一个变量*/
    private Boolean enable;
    /*** 项目应用名*/
    private String applicationName;
    /*** 项目版本信息*/
    private String applicationVersion;
    /*** 项目描述信息*/
    private String applicationDescription;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30).pathMapping("/")// 定义是否开启swagger，false为关闭，可以通过变量控制，线上关闭
                .enable(enable)//配置api文档元信息
                .apiInfo(apiInfo())// 选择哪些接口作为swagger的doc发布
                .select()//apis() 控制哪些接口暴露给swagger，// RequestHandlerSelectors.any() 所有都暴露// RequestHandlerSelectors.basePackage("**.**.*")  指定包位置// withMethodAnnotation(ApiOperation.class)标记有这个注解 ApiOperation
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(applicationDescription)
                .contact(new Contact("Lightr", "https://github.com/W-Lightr", "xxx@xxxx.com"))
                .version(applicationVersion).build();
    }






}


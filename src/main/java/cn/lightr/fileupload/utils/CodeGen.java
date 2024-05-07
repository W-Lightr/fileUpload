package cn.lightr.fileupload.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * @author Lightr
 * @date 2024/1/22 10:04
 * @Description 代码生成
 */
public class CodeGen {
    private static final String AUTHOR = "Lightr";
    private static final String MODEL_NAME = ""; //父模块名称
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/fileUpload?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String PACKAGE = "cn.lightr.fileupload";
    //一般只需要改下面
    private static final String TABLE = "file_chunk"; //表名
    private static final String MODEL = ""; //模块名称 .gen
    private static final String MAPPER_MODEL = ""; //mapper模块名称  \\gen
    public static void main(String[] args) {
        String projectDirectory = Paths.get("").toAbsolutePath()+"\\src\\main";
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(projectDirectory+"\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                }))
                .packageConfig(builder -> {
                    builder.parent(PACKAGE) // 设置父包名
                            .moduleName(MODEL_NAME) // 设置父包模块名
                            .entity("model.entity"+ MODEL)
                            .service("service.intf"+ MODEL)
                            .serviceImpl("service.impl"+ MODEL)
                            .mapper("mapper"+ MODEL)
                            .xml("mapper.xml"+ MODEL)
                            .controller("controller"+ MODEL)
                            .pathInfo(Collections.singletonMap(OutputFile.xml, projectDirectory + "\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE) // 设置需要生成的表名
                            .controllerBuilder().enableRestStyle() // 开启Rest风格控制器
                            .entityBuilder().enableFileOverride() // 开启实体类覆盖已生成文件
                            .enableLombok() // 开启lombok注解
                            .enableColumnConstant(); //开启静态列名字段生成
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

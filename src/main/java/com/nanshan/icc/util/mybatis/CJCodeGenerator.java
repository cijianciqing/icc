package com.nanshan.icc.util.mybatis;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.nanshan.icc.config.mybatis.CJBaseColmns;
import com.nanshan.icc.config.mybatis.CJMapper;

import java.util.Collections;

public class CJCodeGenerator {

    // 数据库连接配置
    private static final String JDBC_URL = "jdbc:mysql://172.20.1.117:3306/icc_test?useUnicode=true&useSSL=false&characterEncoding=utf8";
    private static final String JDBC_USER_NAME = "wqn";
    private static final String JDBC_PASSOWRD = "wqn641214";

    // 包名和模块名
    private static final String PACKAGE_NAME = "com.nanshan.icc";
    private static final String MODULE_NAME = "generated";

    // 表名，多个表使用英文逗号分割
    private static final String[] TBL_NAMES = {"channel" };
//            { "cj_auth_user","cj_auth_user_and_role","cj_auth_role","cj_auth_role_and_resource","cj_auth_resource" };

    // 表名的前缀，从表生成代码时会去掉前缀
    private static final String TABLE_PREFIX = "";

    // 生成代码入口main方法
    public static void main(String[] args) {
        // 1.数据库配置
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder(JDBC_URL, JDBC_USER_NAME,
                JDBC_PASSOWRD).dbQuery(new MySqlQuery()).typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler());

        // 1.1.快速生成器
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceConfigBuilder);

        // 2.全局配置
        // 覆盖已生成文件
        // 不打开生成文件目录
        // 指定输出目录,注意使用反斜杠\
        // 设置注释的作者
        // 设置注释的日期格式
        // 使用java8新的时间类型
        fastAutoGenerator.globalConfig(globalConfigBuilder -> globalConfigBuilder.fileOverride().disableOpenDir()
                .outputDir("src\\main\\java").author("cj").commentDate("yyyy-MM-dd").dateType(DateType.TIME_PACK));

        // 3.包配置
        // 设置父包名
        // 设置父包模块名
        // 设置MVC下各个模块的包名
        // 设置XML资源文件的目录
        fastAutoGenerator.packageConfig(packageConfigBuilder -> packageConfigBuilder.parent(PACKAGE_NAME)
                .moduleName(MODULE_NAME).entity("entity").mapper("dao").service("service").serviceImpl("service.impl")
                .controller("controller").other("other")
                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "src\\main\\resources\\mapper")));

        // 4.模板配置
        // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        AbstractTemplateEngine templateEngine = new FreemarkerTemplateEngine();
        fastAutoGenerator.templateEngine(templateEngine);

        // 5.注入配置 TODO

        // 6.策略配置
        // 设置需要生成的表名
        // 设置过滤表前缀
        fastAutoGenerator.strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.enableCapitalMode()
                .enableSkipView().disableSqlFilter().addInclude(TBL_NAMES).addTablePrefix(TABLE_PREFIX));

        // 6.1.Entity策略配置
        // 生成实体时生成字段的注解，包括@TableId注解等
        // 数据库表和字段映射到实体的命名策略，为下划线转驼峰
        // 实体名称格式化为XXXEntity
        fastAutoGenerator.strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.entityBuilder()
                .idType(IdType.ASSIGN_ID)//默认实现类 {@link com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator}(雪花算法)
                .superClass(CJBaseColmns.class)//设置父类和自动注入的字段
                .addIgnoreColumns("createTime","updateTime","flag")//将父类总的字段取消
                .enableTableFieldAnnotation()
                .addTableFills(new Column("sort_no", FieldFill.INSERT))
                .naming(NamingStrategy.underline_to_camel)//驼峰命名
                .columnNaming(NamingStrategy.underline_to_camel).formatFileName("%sEntity"));

        // 6.2.Controller策略配置
        // 开启生成@RestController控制器
        fastAutoGenerator
                .strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.controllerBuilder()
                        .enableHyphenStyle()//开启驼峰转连字符
                        .enableRestStyle());//开启生成@RestController 控制器

        // 6.3.Service策略配置
        // 格式化service接口和实现类的文件名称，去掉默认的ServiceName前面的I
        fastAutoGenerator.strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.serviceBuilder()
                .formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl"));

        // 6.4.Mapper策略配置
        // 格式化 mapper文件名,格式化xml实现类文件名称
        fastAutoGenerator.strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.mapperBuilder()
                .superClass(CJMapper.class)
                .enableBaseResultMap()//启用 BaseResultMap 生成
                .formatMapperFileName("%sDao").formatXmlFileName("%sDao"));

        // 7.生成代码
        fastAutoGenerator.execute();
    }

}
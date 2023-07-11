package com.nanshan.icc.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
* 实现自动装填
* */
@Slf4j
@Component
public class CJMetaObjectHandler implements MetaObjectHandler {


    /**
     * 创建时间
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        //可以根据如下代码进行个性化配置
        //metaObject.getOriginalObject() instanceof CjMissionInfoEntity

        log.info(" -------------------- start insert fill ...  --------------------");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "flag", Boolean.class, false);

        /*
        * 以下这两个字段，可以考虑放到初始代码块中。。
        * 好用
        * */

        this.strictInsertFill(metaObject, "sortNo", Integer.class, 0);
//        this.strictInsertFill(metaObject, "parentId", String.class, "0");

//        if (metaObject.hasGetter("createTime") && metaObject.hasGetter("updateTime")) {
//            setFieldValByName("createTime", new Date(), metaObject);
//            setFieldValByName("updateTime", new Date(), metaObject);
//        }
//        if (metaObject.hasGetter("flag")) {
//            setFieldValByName("flag", false, metaObject);
//
//        }
    }

    /**
     * 最后一次更新时间
     */
    @Override
    public  void updateFill(MetaObject metaObject) {
        log.info(" -------------------- start update fill ...  --------------------");
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

//
//        if (metaObject.hasGetter("updateTime")) {
//            setFieldValByName("updateTime", new Date(), metaObject);
//        }
    }

}
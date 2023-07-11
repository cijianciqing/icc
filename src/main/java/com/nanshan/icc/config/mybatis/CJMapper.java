package com.nanshan.icc.config.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface CJMapper<T> extends BaseMapper<T> {
    int deleteByIdWithFill(T entity);
}

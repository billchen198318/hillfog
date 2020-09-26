package org.qifu.hillfog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 這是引入共用設定的 Mapper 不需要加 Method
 */
@Mapper
@Component("DB1ConfigHillfog")
public interface DB1Config {

}

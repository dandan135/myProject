package com.cssl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cssl.pojo.Items;
import com.cssl.pojo.Options;
import com.cssl.pojo.Subject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OptionsMapper extends BaseMapper<Options> {
    public List<Subject> select(@Param("title") String title);
    public Subject selectBySid(@Param("sid") int sid);
    public int isTitle(@Param("title") String title);
    public int insertSubject(Subject subject);
    public int insertOptions(Options options);
    public int selectCount(@Param("sid") int sid);
    public int selectPoll(@Param("subid") int subid);
    public int isPoll(@Param("userid") int userid,@Param("subid")int subid);
    public int insertItems(Items items);
    public int selectOptions(int opid);
    public int selectAllCount(int subid);
    public int updateSubject(Subject subject);
    public int updateOptions(Options options);
    public int deleteSubject(int sid);
    public int deleteOptions(int osid);
    public int deleteItems( @Param("subid")int subid,@Param("opid")int opid);
    public int deleteOp(int oid);
}

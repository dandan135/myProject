package com.cssl.service;

import com.cssl.dao.OptionsMapper;
import com.cssl.pojo.Items;
import com.cssl.pojo.Options;
import com.cssl.pojo.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 这是mybatis业务层
 */
@Service
public class OptionsService {

    @Resource
    private OptionsMapper optionsMapper;

    public List<Subject> selectAll(String title){
        return optionsMapper.select(title);
    }

    public Subject selectBySid(int sid){
        return optionsMapper.selectBySid(sid);
    }
    public int yzTitle(String title){return optionsMapper.isTitle(title);}

    public int insertSubject(Subject subject){return  optionsMapper.insertSubject(subject);}

    public int insertOptions(Options options){return optionsMapper.insertOptions(options);}

    public int selectCount(int sid){return optionsMapper.selectCount(sid);}

    public int selectPoll(int subid){return optionsMapper.selectPoll(subid);}

    public int isPoll(int userid,int subid){return optionsMapper.isPoll(userid,subid);}

    public int insertItems(Items items){return optionsMapper.insertItems(items);}

    public int selectOptions(int opid){return optionsMapper.selectOptions(opid);}

    public int selectAllCount(int subid){return optionsMapper.selectAllCount(subid);}

    public int updateSubject(Subject subject){return optionsMapper.updateSubject(subject);}

    public int updateOptions(Options options){return optionsMapper.updateOptions(options);}

    public int deleteSubject(int sid){return optionsMapper.deleteSubject(sid);}

    public int deleteOptions(int osid){return optionsMapper.deleteOptions(osid);}

    public int deleteItems(int subid,int opid){return optionsMapper.deleteItems(subid,opid);}

    public int deleteOp(int oid){return optionsMapper.deleteOp(oid);}
}

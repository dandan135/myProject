package com.cssl.controller;

import com.cssl.pojo.Items;
import com.cssl.pojo.Options;
import com.cssl.pojo.Subject;
import com.cssl.pojo.Users;
import com.cssl.service.OptionsService;
import com.cssl.util.Helper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VoteController {


    @Resource
    private OptionsService optionsService;

    /**
     * 返回列表的方法 （由于我修改存了标记在session并且修改和主页查询列表是一个页面）
     * 所以需要在后台移除session的标记
     * @param session
     * @return
     */
    @RequestMapping("/returnSelect")
    public String selectRe(HttpSession session){
        session.removeAttribute("state");
        return "redirect:/select";
    }

    /**
     * 维护页面的根据sid删除
     * 由于我jquery学的不怎么样 前台ajax删除移除元素就懒得搞了 直接后台删除
     * 删除成功返回维护页面
     * 由于主外键关系 所以要先删从表 在删主表 所以涉及三张表的删除
     * @param sid
     * @return
     */
    @RequestMapping("/del")
    public String Del(int sid){
        System.out.println("删除sid:"+sid);
        Subject subject=optionsService.selectBySid(sid);
        for (Options option:
             subject.getOps()) {
            int result=optionsService.deleteItems(sid,option.getOid());
            System.out.println("删除Items:"+result);
        }
        int result2=optionsService.deleteOptions(sid);
        System.out.println("删除Options:"+result2);
        int result1=optionsService.deleteSubject(sid);
        System.out.println("删除Subject:"+result1);

        return "redirect:/select";
    }

    /**
     * 由于我修改跟新增是一个页面 所以查询出回显对象以后返回add.html
     * @param sid 修改的sid
     * @param model
     * @return
     */
    @RequestMapping("/update")
    public String Update(int sid,Model model){
        Subject subject=optionsService.selectBySid(sid);
        model.addAttribute("update","修改");//标记，在add.html做判断 是修改还是新增
        model.addAttribute("subject",subject);
        return "add";
    }

    /**
     * 点击维护先判断是否是管理员
     * 不是管理员不能维护
     * @param session
     * @return
     */
    @RequestMapping("/modify")
    public String Modify(HttpSession session){
        Users users=(Users) session.getAttribute("users");
        if("y".equals(users.getIsadmin())){
            //标记是维护
            session.setAttribute("state","维护列表");
            //维护跟主页查询是一个页面 查询方法也一致
            return "redirect:/select";
        }else{
            System.out.println("不是管理员不能维护");
            return "redirect:/select";
        }

    }

    /**
     *想的比较简单 并且在实体类增加了我需要的属性 这样每个选项的占比就跟着选项走 前台显示比较容易
     * 如果不增加实体类属性 也可以把占比存进去map key为占比的选项的id value为占比
     * 前台循坏时 map.get(oid)就OK
     * @param entityId 前台查看投票的sid
     * @param model
     * @return
     */
    @RequestMapping("/view")
    public String View(int entityId,Model model){
        //根据sid查出subject对象
        Subject subject=optionsService.selectBySid(entityId);
        //查出当前sid的总票数
        int sum= optionsService.selectAllCount(entityId);
        System.out.println("view subject总票数："+sum);
        for (Options op:
             subject.getOps()) {//循坏subject的option选项。求出每一个选项的占比
            int count=optionsService.selectOptions(op.getOid());//单独选项的票数
            op.setPollCount(count);//set进实体类前台显示
            Double wid= Helper.accuracy(count,sum,2);//这是我网上找的一个方法 直接乘除结果不对
            //Helper.accuracy（count,sum,2） count为当前选项票数 sum总票数  scale为保留两位小数
            System.out.println("wid:"+wid);
            op.setWidth(wid);//占比
            System.out.println("view oid:"+op.getOid()+"count:"+count+"width:"+op.getWidth());
        }
        subject.setOptionCount(optionsService.selectCount(entityId));//当前标题下有几个选项
        subject.setPoll(optionsService.selectPoll(entityId));//当前标题有几个人投过票
        //注意有几个人投票跟总票数是不一样的
        model.addAttribute("subject",subject);
        return "voteview";
    }

    /**
     * 投票
     * @param options 投票选项的oid(有多选所以是数组)
     * @param entityId 投票标题的sid
     * @param session
     * @return
     */
    @RequestMapping("/votesubmit")
    public String voteSubmit(int [] options,int entityId,HttpSession session){
        Users user=(Users)session.getAttribute("users");//获取当前是哪个用户投票
        System.out.println("投票：");
        for (int i :
             options) {
            System.out.println("userid:"+user.getUid());
            System.out.println("sid:"+entityId);
            System.out.println("oid:"+i);
            int result=optionsService.insertItems(new Items(user.getUid(),entityId,i));//插入中间表
            System.out.println("insert Items:"+result);

        }
        return "redirect:/select";
    }

    /**
     * 投票(需要判断当前用户此标题是否已投过票 一个用户一个标题只能投一次票)
     * @param sid 投票的sid
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/vote")
    public String Vote(int sid, Model model,HttpSession session){
        System.out.println("投票id:"+sid);
        Users users=(Users)session.getAttribute("users");
        int result= optionsService.isPoll(users.getUid(),sid);
        if(result>0){//该选项该用户已经投过票
            System.out.println("该选项该用户已经投过票");
            return "redirect:/select";
        }else{
            System.out.println("该选项该用户没有投过票");
            Subject subject=optionsService.selectBySid(sid);
            subject.setPoll(optionsService.selectPoll(sid));//几个人投过票
            subject.setOptionCount(optionsService.selectCount(sid));//几个选项
            System.out.println("count:"+subject.getOptionCount()+" poll:"+subject.getPoll());
            model.addAttribute("subject",subject);
            return "vote";
        }


    }

    /**
     * 新增投票内容
     * 只有管理员才能新增 所以需要判断一下
     * @param session
     * @param model
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/add")
    public String Add(HttpSession session, Model model, HttpServletResponse response)throws IOException {
        Users users=(Users)session.getAttribute("users");
        if("y".equals(users.getIsadmin())){
            return "add";
        }
        /*PrintWriter out=response.getWriter();
        out.write("<script>");
        out.write("alert('只有管理员才能新增');");

        //out.write("location.href='http://localhost:8080/boot/select'; ");
        out.write("</script>");*/
        System.out.println("只有管理员才能新增");
        return "redirect:/select";
    }

    /**
     * 新增投票内容(由于修改新增在同一个页面 所以action也是一个地址)
     * @param subject 修改注入的对象 由于我使用的对象里面包含list集合对象（一对多）
     *                所以我修改的选项内容也在subject
     * @param options 新增的options选项内容
     * @param update 标记是修改还是新增
     * @return
     */
    @RequestMapping("/addSubject")
    public String addOption(Subject subject,@RequestParam(required = false)String [] options,@RequestParam(required = false) String update){
        if(update==null){//判断是修改还是新增
            System.out.println("新增Subject title:"+subject.getTitle());
            int result=optionsService.insertSubject(subject);
            System.out.println("新增Subject Result:"+result);
            System.out.println("新增Subject sid:"+subject.getSid());
            //List<Options> list=new ArrayList<Options>();
            if(result>0) {
                for (String str :
                        options) {
                    Options options1 = new Options();
                    options1.setContext(str);
                    options1.setOsid(subject.getSid());
                    int result1 = optionsService.insertOptions(options1);
                    System.out.println("新增Options context:" + str);
                    System.out.println("新增Options Result:" + result1);
                }
        }
        }else{
            System.out.println("修改Subject sid:"+subject.getSid()+"size："+subject.getOps().size());
            //标题不能修改 所以修改的是type(单选多选)
            int result=optionsService.updateSubject(subject);
            System.out.println("updateSubject result:"+result);
            //由于此修改须实现删除选项，增加选项，修改选项
            //所以删除选的比较笨的方法 由于点击删除以后只是在前台移除元素
            //当提交到后台时拿不到删除的id 所以这里就先查询出之前的所以选项
            //并且把前台提交过来的options 的oid单独提出来（list）
            //循坏后台数据库查询出来的options  if(!list.contains(op.getOid()))
            //就是前台的list（oid）里面没有包含后台的oid时 就说明这个oid在前台被删掉了
            //删除要先删从表 在删主表 因为中间表items引用了oid
            Subject subject1=optionsService.selectBySid(subject.getSid());
            List<Integer> list=new ArrayList<Integer>();
            for (Options op:
                 subject.getOps()) {
                list.add(op.getOid());
            }
            for (Options op:
                 subject1.getOps()) {
                if(!list.contains(op.getOid())){
                    System.out.println("删除的id:"+op.getOid());
                     int delItems=optionsService.deleteItems(subject.getSid(),op.getOid());
                    int delOptions=optionsService.deleteOp(op.getOid());
                    System.out.println("修改删除的oid："+op.getOid());
                    System.out.println("修改删除的result："+delOptions);
                }
            }
            /*
            *修改
             */
            for (Options option1:
                 subject.getOps()) {
                    if(subject1.getOps().contains(option1)){
                        System.out.println("原有选项不修改："+option1.getOid()+"\t"+option1.getContext());
                    }else{
                        int result2=optionsService.updateOptions(option1);
                        System.out.println("修改删除的result："+result2);
                    }
            }

            //新增
            if(options!=null) {
                for (String context:
                        options) {
                    System.out.println("修改 新增选项内容："+context);
                    int insertResult =optionsService.insertOptions(new Options(context,subject.getSid()));
                    System.out.println("修改 新增Result："+insertResult);
                }
            }
        }
        return "redirect:/select";
    }

    /**
     * 主页模糊查询和分页
     * @param pageIndex 页码
     * @param title 标题模糊查询
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/select")
    public String Select(@RequestParam(required = false) String pageIndex,
                         @RequestParam(required = false) String title,
                         Model model,HttpSession session){
        System.out.println("select查询");
        int pageNum=0;
        if(pageIndex==null||"0".equals(pageIndex)){
            pageNum=1;
        }else{
            pageNum=Integer.parseInt(pageIndex);
        }
        System.out.println("pageIndex:"+pageNum);
        Page<Subject> page=PageHelper.startPage(pageNum,3);
        List<Subject> list=optionsService.selectAll(title);
        for(Subject subject:list){

            System.out.println("sid:"+subject.getSid()+"count:"+subject.getOptionCount()+"poll:"+subject.getPoll());
        }
        model.addAttribute("page",page);
        model.addAttribute("list",list);
        model.addAttribute("pageIndex",String.valueOf(pageNum));

        return "votelist";
    }
}

package com.cssl.controller;

import com.cssl.pojo.Users;
import com.cssl.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import java.util.List;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;


@Controller
public class UsersController implements ServletContextAware {

    private ServletContext application;
    @Override
    public void setServletContext(ServletContext arg0) {

        this.application=arg0;
    }
    @Autowired
    private UsersService us;



    /*
    *返回注册页面
     */
    @RequestMapping("/regist")
    public String Return(){
        return "regist";
    }
    /*
     *返回登录页面
     */
    @RequestMapping("/log")
    public String Return2(){
        return "login";
    }
    /*
    *注册
     */
    @RequestMapping("/reg.action")
    public String reg(Users users,String confirmPassword){
        if(confirmPassword.equals(users.getPassword())){
            int i=us.reg(users);
            if (i>0){
                return "login";
            }
        }
        return "regist";
    }

    /**
     * 这是之前实现不能重复登录的方法
     * 当用户登录就把当前用户的islog改为y 把当前登录的时间存进去数据库
     * 下次登录在判断 如果当前登录的时间距上次登录时间超过2分钟 或者islog为n都登录成功
     * @param users
     * @param session
     * @param model
     * @return
     */
    /*@RequestMapping("/log.action")
    public String log(Users users, Model model,HttpSession session) throws ParseException {
        Users user=us.log(users);
        if(user!=null){
            Date date=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate=sdf.format(date);
            System.out.println("当前登录时间："+currentDate);
            long time=date.getTime()-sdf.parse(user.getLogdate()).getTime();
            System.out.println("time:"+time);
            long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
            System.out.println("分钟数："+minutes);
            if(minutes>2||"n".equals(user.getIslog())){
                System.out.println("登录成功：name:"+user.getUsername());
                System.out.println("登录改变状态的uid:"+user.getUid());
                int i=us.change(new Users(user.getUid(),null,null,null,currentDate,"y"));
                System.out.println("change Result:"+i);
                session.setAttribute("users",user);
                return "redirect:/select";
            }else{
                model.addAttribute("msg","该用户已经在线");
            }
        }else{
            model.addAttribute("msg","用户名或密码输入错误");
        }
        return "login";
    }*/

    /**
     *
     * 对应上面的注销方法
     * 点击注销 把当前用户的islog状态改为n
     * @return
     */
    /*@RequestMapping("/logout")
    public String logOut(HttpSession session){
        session.removeAttribute("state");
        Users users=(Users)session.getAttribute("users");
        int result=us.change(new Users(users.getUid(),null,null,null,null,"n"));
        System.out.println("注销result:"+result);
        return "login";
    }*/
    @RequestMapping("/log.action")
    public String Login(Users users, HttpSession session, Model model){
        Users user=us.log(users);
        if(user!=null){//登录成功
            session.setAttribute("users",user);
            List<String> list=(List<String>)application.getAttribute("list");
            if(!list.contains(user.getUsername())){//不包含则登陆成功
                list.add(user.getUsername());
                return "redirect:/select";
            }else{//如果list包含当前登录用户名 则重复登录
                model.addAttribute("msg","该用户已经在线");
            }
        }else{
            model.addAttribute("msg","用户名或密码输入错误");
        }
        return "login";
    }

    /**
     * 注销方法
     * 将session全部移除
     * @param session
     * @return
     */
    @RequestMapping("/logout")
    public void logOut(HttpSession session) {
        session.invalidate();
    }
}

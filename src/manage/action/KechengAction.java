package manage.action;

import java.io.*;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

import manage.dao.BanJiDao;
import manage.dao.KechengDao;
import manage.model.Kecheng;
import manage.util.Pager;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author XYLZR
 *	课程行动类
 */

public class KechengAction extends ActionSupport {

	private static final long serialVersionUID = 7963004028001698964L;

	//定义变量
	private KechengDao kechengDao;
	private BanJiDao banjiDao;

	//利用setter和getter方法实现对变量的初始化
	public BanJiDao getBanjiDao() {
		return banjiDao;
	}
	
	public void setBanjiDao(BanJiDao banjiDao) {
		this.banjiDao = banjiDao;
	}

	public KechengDao getKechengDao() {
		return kechengDao;
	}

	public void setKechengDao(KechengDao kechengDao) {
		this.kechengDao = kechengDao;
	}


	/**
	 *	设置页面数据，用于管理员
	 * @return
	 */
	public String kechenglist() {
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//定义页面显示的数据量
		int numPerPage = 20;
		//设置为第一张界面
		int pageNum = 1;
		
		//判断是否换页
		if(request.getParameter("pageNum")!=null && !request.getParameter("pageNum").equals("")){
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		//设置该页面的数据数量
		if(request.getParameter("numPerPage")!=null){
			numPerPage = Integer.parseInt(request.getParameter("numPerPage"));
		}
		
		//获取课程的数量
		int total = kechengDao.selectAllKechengCount();
		
		//将当前页面的数据存入List中
		List<Kecheng> kechengs = kechengDao.selectAllKecheng((pageNum - 1) * numPerPage, numPerPage);
		
		//将获取的数据写入进网页中
		request.setAttribute("totalCount", total);
		request.setAttribute("ps", numPerPage);
		request.setAttribute("pn", pageNum);
		request.setAttribute("kechenglist", kechengs);
		request.setAttribute("banjilist", banjiDao.getAll(""));
		return "success";
	}
	
	/**
	 * 	设置页面数据，用于老师
	 * @return
	 */
	public String kechenglist2() {
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//设置页面数据量
		int numPerPage = 20;
		//设置为第一张界面
		int pageNum = 1;
		
		//判断是否换页
		if(request.getParameter("pageNum")!=null && !request.getParameter("pageNum").equals("")){
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		//设置该页面的数据数量
		if(request.getParameter("numPerPage")!=null){
			numPerPage = Integer.parseInt(request.getParameter("numPerPage"));
		}
		
		//获取课程的数量
		int total = kechengDao.selectAllKechengCount();
		
		//获取请求会话
		HttpSession session = request.getSession();
		//获取session中键为role的值
		String role = (String)session.getAttribute("role");
		
		//判断是否为学生或者老师
		if("role4".equals(role)||role=="role4" || "role3".equals(role)){
			//获取班级号
			String banjinum=(String) session.getAttribute("banjinum"); 
			//设置该班的课程
			request.setAttribute("kechenglist", kechengDao.selectAllKechengBy((pageNum - 1) * numPerPage, 50,
					"and banjinum='"+banjinum+"'"));
		}else{
			//设置对应的信息
			request.setAttribute("kechenglist", kechengDao.selectAllKecheng((pageNum - 1) * numPerPage, numPerPage));
			request.setAttribute("totalCount", total);
			request.setAttribute("ps", numPerPage);
			request.setAttribute("pn", pageNum);
		}
		return "success";
	}
	
	/**
	 * 	增加课程信息的班级信息
	 * @return
	 */
	public String kechengadd(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//设置班级信息
		request.setAttribute("banjilist", banjiDao.getAll(""));
		return "success";
	}
	
	/**
	 * 	对增加课程信息赋值
	 * @return
	 * @throws Exception
	 */
	public String kechengadd2() throws Exception{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//新建课程类
		Kecheng kecheng = new Kecheng();
		
		//给周一至周日的课程赋值，可以为空值
		kecheng.setKechengname1(request.getParameter("kechengname1"));
		kecheng.setKechengname2(request.getParameter("kechengname2"));
		kecheng.setKechengname3(request.getParameter("kechengname3"));
		kecheng.setKechengname4(request.getParameter("kechengname4"));
		kecheng.setKechengname5(request.getParameter("kechengname5"));
		kecheng.setKechengname6(request.getParameter("kechengname6"));
		kecheng.setKechengname7(request.getParameter("kechengname7"));
		
		//设置课程位置，对应为周几的第几节课
		kecheng.setKechenglock(Integer.parseInt(request.getParameter("kechenglock")));
		//设置班级
		kecheng.setBanji(request.getParameter("banji"));
		//设置班级编号
		kecheng.setBanjinum(request.getParameter("banjinum"));
		//设置创建时间
		kecheng.setCreatetime(new Date());
		
		//调用新建课程方法
		kechengDao.insertKecheng(kecheng);
		
		//获取页面请求
		HttpServletResponse resp = ServletActionContext.getResponse();
		//设置页面输出信息
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print("{\"statusCode\":\"200\", \"message\":\"添加成功！\",\"navTabId\":\"kechengList\", \"rel\":\"kechengList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"kecheng///kechenglist.html\"}");

		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 	删除课程信息
	 * @return
	 * @throws IOException
	 */
	public String kechengdel() throws IOException{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取指定id的值
		int id = Integer.parseInt(request.getParameter("id"));
		kechengDao.delKecheng(kechengDao.selectKecheng(id));
		
		//获取页面信息，设置页面输出信息
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
	out.write(manage.util.Util.outPutMsg("200", "修改成功", "kechengList", "", false, "kecheng///kechenglist.html"));
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 	修改课程信息
	 * @return
	 */
	public String  kechengupdate(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取指定id的值
		int id = Integer.parseInt(request.getParameter("id"));
		//将返回的值设置进bean中
		request.setAttribute("bean", kechengDao.selectKecheng(id));
		//将获取的id值设置进id中
		request.setAttribute("id", id);
		return "success";
	}
	
	/**
	 * 	将修改的课程信息赋值到数据库中
	 * @return
	 * @throws Exception
	 */
	public String kechengupdate2() throws Exception{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		//设置页面输出信息
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		//获取关键字id的值
		int id = Integer.parseInt(request.getParameter("id"));
		Kecheng bean = kechengDao.selectKecheng(id);
		
		//获取原有的信息，再将修改的新值赋进去
		if(request.getParameter("kechengname1")!=null){
			bean.setKechengname1(request.getParameter("kechengname1"));
		}
		if(request.getParameter("kechengname2")!=null){
			bean.setKechengname2(request.getParameter("kechengname2"));
		}
		if(request.getParameter("kechengname3")!=null){
			bean.setKechengname3(request.getParameter("kechengname3"));
		}
		if(request.getParameter("kechengname4")!=null){
			bean.setKechengname4(request.getParameter("kechengname4"));
		}
		if(request.getParameter("kechengname5")!=null){
			bean.setKechengname5(request.getParameter("kechengname5"));
		}
		if(request.getParameter("kechengname6")!=null){
			bean.setKechengname6(request.getParameter("kechengname6"));
		}
		if(request.getParameter("kechengname7")!=null){
			bean.setKechengname7(request.getParameter("kechengname7"));
		}
		
		if(request.getParameter("kechenglock")!=null){
			bean.setKechenglock(Integer.parseInt(request.getParameter("kechenglock")));
		}
		if(request.getParameter("banji")!=null){
			bean.setBanji(request.getParameter("banji"));
		}
		if(request.getParameter("banjiid")!=null){
			bean.setBanjinum(request.getParameter("banjinum"));
		}
		
		kechengDao.updateKecheng(bean);
		out.print("{\"statusCode\":\"200\", \"message\":\"修改成功！\",\"navTabId\":\"kechengList\", \"rel\":\"kechengList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"kecheng///kechenglist.html\"}");

		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	根据班级检索课程信息
	 * @return
	 */
	public String searchkecheng(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String keyword  = request.getParameter("keyword");
		
		int currentpage = 1;
		int pagesize = 50;
		if (request.getParameter("pagenum") != null)
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		request.setAttribute("kechenglist", kechengDao.selectAllKechengBy((currentpage - 1) * pagesize, 50,
						"and banjinum like '%"+keyword+"%'"));
		request.setAttribute("searchkechenglist", kechengDao.getAll(" and kechenglock=0 "));
		request.setAttribute("banjilist", banjiDao.getAll(""));
		return "success";
		
	}
}

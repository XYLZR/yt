package manage.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import manage.dao.BanJiDao;
import manage.dao.KaoQinLogDao;
import manage.dao.SubjectDao;
import manage.dao.UserDao;
import manage.model.KaoQinLog;
import manage.model.User;
import manage.util.Util;

/**
 * 
 * @author XYLZR
 *	考勤行动类
 */

public class KaoQinLogAction  {
	
	private static final long serialVersionUID = 7963004028001698964L;

	//定义变量
	private KaoQinLogDao kaoqinlogDao;
	private BanJiDao banjiDao;
	private UserDao userDao;
	private SubjectDao subjectDao;

	//利用setter和getter方法初始化变量
	public SubjectDao getSubjectDao() {
		return subjectDao;
	}

	public void setSubjectDao(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public BanJiDao getBanjiDao() {
		return banjiDao;
	}

	public void setBanjiDao(BanJiDao banjiDao) {
		this.banjiDao = banjiDao;
	}

	public KaoQinLogDao getKaoqinlogDao() {
		return kaoqinlogDao;
	}

	public void setKaoqinlogDao(KaoQinLogDao kaoqinlogDao) {
		this.kaoqinlogDao = kaoqinlogDao;
	}


	@SuppressWarnings("unchecked")
	/**
	 * 	设置考勤页面
	 * @return
	 * @throws Exception
	 */
	public String kaoqinloglist() throws Exception {	
		HttpServletRequest request = ServletActionContext.getRequest();
		int numPerPage = 20;
		int pageNum = 1;
		
		//判断是否换页
		if(request.getParameter("pageNum")!=null && !request.getParameter("pageNum").equals("")){
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		if(request.getParameter("numPerPage")!=null){
			numPerPage = Integer.parseInt(request.getParameter("numPerPage"));
		}
		
		//获取页面会话
		HttpSession session = request.getSession();
		String role = (String)session.getAttribute("role");
		String where = "";
		
		//判断
		if(!"role0".equals(role)){
			String banjinum=(String) session.getAttribute("banjinum"); 
			where = "and banjinum='"+banjinum+"'";
		}
		if("role4".equals(role)||role=="role4"){
			String codenum=(String) session.getAttribute("codenum"); 
			where += " and codenum='"+codenum+"'";
		}
		
		//设置页面数据
		int total = kaoqinlogDao.selectAllKaoQinLogCount();
		List<KaoQinLog> kaoqinlogs = kaoqinlogDao.selectAllKaoQinLogBy(	(pageNum - 1) * numPerPage, numPerPage,where);
		request.setAttribute("totalCount", total);
		request.setAttribute("ps", numPerPage);
		request.setAttribute("pn", pageNum);
		request.setAttribute("kaoqinloglist", kaoqinlogs);
		return "success";
	}
	
	
	/**
	 * 	新增考勤
	 * @return
	 */
	public String kaoqinlogadd(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String role = (String)session.getAttribute("role");
		String where = "";
		if(!"role0".equals(role)){
			String banjinum=(String) session.getAttribute("banjinum"); 
			where = "and banjinum='"+banjinum+"'";
		}
		request.setAttribute("banjilist", banjiDao.getAll(where));
		request.setAttribute("subjectlist", subjectDao.getAll(""));
		return "success";
	}
	
	
	/**
	 * 	对增加的考勤信息赋值
	 * @return
	 * @throws Exception
	 */
	public String kaoqinlogadd2() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
		
		KaoQinLog kaoqinlog = new KaoQinLog();
		
		kaoqinlog.setBanjinum(request.getParameter("banjinum"));
		kaoqinlog.setCodenum(request.getParameter("codenum"));
		kaoqinlog.setIskuangke(Integer.parseInt(request.getParameter("iskuangke")));
		kaoqinlog.setKechengname(request.getParameter("kechengname"));
		kaoqinlog.setTeacher(request.getParameter("teacher"));
		kaoqinlog.setKuangkenum(Integer.parseInt(request.getParameter("kuangkenum")));
		kaoqinlog.setRiqi(request.getParameter("riqi"));
		
		kaoqinlog.setCreatetime(new Date());
		
		//调用新增考勤函数
		kaoqinlogDao.insertKaoQinLog(kaoqinlog);
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print("{\"statusCode\":\"200\", \"message\":\"添加成功！\",\"navTabId\":\"kaoqinlogList\", \"rel\":\"kaoqinlogList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"kaoqinlog/kaoqinloglist.html\"}");
		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	修改考勤数据
	 * @return
	 */
	public String  kaoqinlogupdate(){
		HttpServletRequest request = ServletActionContext.getRequest();
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("bean", kaoqinlogDao.selectKaoQinLog(id));
		request.setAttribute("id", id);
		request.setAttribute("banjilist", banjiDao.getAll(""));
		request.setAttribute("subjectlist", subjectDao.getAll(""));
		return "success";
	}
	
	
	/**
	 * 	修改考勤数据，并存储数据
	 * @return
	 * @throws IOException
	 */
	public String  kaoqinlogupdate2() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		int id = Integer.parseInt(request.getParameter("id"));
		KaoQinLog bean = kaoqinlogDao.selectKaoQinLog(id);
		
		
		if(request.getParameter("banjinum")!=null){
			bean.setBanjinum(request.getParameter("banjinum"));
		}
		if(request.getParameter("codenum")!=null){
			bean.setCodenum(request.getParameter("codenum"));
		}
		if(request.getParameter("iskuangke")!=null){
			bean.setIskuangke(Integer.parseInt(request.getParameter("iskuangke")));
		}
		if(request.getParameter("kechengname")!=null){
			bean.setKechengname(request.getParameter("kechengname"));
		}
		if(request.getParameter("teacher")!=null){
			bean.setTeacher(request.getParameter("teacher"));
		}
		if(request.getParameter("kuangkenum")!=null){
			bean.setKuangkenum(Integer.parseInt(request.getParameter("kuangkenum")));
		}
		if(request.getParameter("riqi")!=null){
			bean.setRiqi(request.getParameter("riqi"));
		}
		
		kaoqinlogDao.updateKaoQinLog(bean);
		out.print("{\"statusCode\":\"200\", \"message\":\"修改成功！\",\"navTabId\":\"kaoqinlogList\", \"rel\":\"kaoqinlogList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"kaoqinlog///kaoqinloglist.html\"}");

		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	删除考勤数据
	 * @return
	 * @throws IOException
	 */
	public String kaoqinlogdel() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		int id = Integer.parseInt(request.getParameter("id"));
		kaoqinlogDao.delKaoQinLog(kaoqinlogDao.selectKaoQinLog(id));
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.write(manage.util.Util.outPutMsg("200", "修改成功", "kaoqinlogList", "", false, "kaoqinlog///kaoqinloglist.html"));
		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	检索考勤数据，用于老师权限
	 * @return
	 */
	public String searchkaoqin(){
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer sb = new StringBuffer();
		sb.append(" ");
		
		//检索课程名
		String kechengname="";
		if(!"".equals(request.getParameter("kechengname"))&&request.getParameter("kechengname")!=null){
			kechengname = request.getParameter("kechengname");
			sb.append("and kechengname = '"+kechengname+"'");
		}
		
		//检索学生
		String codenum="";
		if(!"".equals(request.getParameter("codenum"))&&request.getParameter("codenum")!=null){
			codenum = request.getParameter("codenum");
			sb.append("and codenum = '"+codenum+"'");
		}
		
		//检索班级
		String banjinum="";
		if(!"".equals(request.getParameter("banjinum"))&&request.getParameter("banjinum")!=null){
			banjinum = request.getParameter("banjinum");
			sb.append("and banjinum = '"+banjinum+"'");
		}
		
		//检索老师
		String teacher="";
		if(!"".equals(request.getParameter("teacher"))&&request.getParameter("teacher")!=null){
			teacher = request.getParameter("teacher");
			 sb.append("and teacher = '"+teacher+"'");
		}
		
		//检索日期
		String riqi="";
		if(!"".equals(request.getParameter("riqi"))&&request.getParameter("riqi")!=null){
			riqi = request.getParameter("riqi");
			 sb.append("and riqi = '"+riqi+"'");
		}
		
		String where = sb.toString();
		
		//设置检索结果
		int currentpage = 1;
		int pagesize = 50;
		if (request.getParameter("pagenum") != null)
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		request.setAttribute("kaoqinloglist", kaoqinlogDao.selectAllKaoQinLogBy((currentpage - 1) * pagesize, 50,where));
		return "success";
	}
	
	
	
	@SuppressWarnings("unchecked")
	/**
	 * 	检索考勤，用于学生权限
	 * @return
	 * @throws Exception
	 */
	public String kaoqinloglist2() throws Exception {	
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String role = (String)session.getAttribute("role");
		StringBuffer sb = new StringBuffer();
		sb.append("and role=4 ");
		
		//检索老师
		String teacher="";
		if("role2".equals(role)||role=="role2"){
			teacher=(String) session.getAttribute("username"); 
			request.setAttribute("teacher", teacher);
		}else{
			if(!"".equals(request.getParameter("teacher"))&&request.getParameter("teacher")!=null){
				teacher = request.getParameter("teacher");
				request.setAttribute("teacher", teacher);
			}
		}
		
		//检索课程名
		String kechengname="";
		if(!"".equals(request.getParameter("kechengname"))&&request.getParameter("kechengname")!=null){
			kechengname = request.getParameter("kechengname");
			request.setAttribute("kechengname", kechengname);
		}
		
		//检索班级
		String banjinum="";
		if(!"".equals(request.getParameter("banjinum"))&&request.getParameter("banjinum")!=null){
			banjinum = request.getParameter("banjinum");
			request.setAttribute("banjinum", banjinum);
			sb.append("and banjinum = '"+banjinum+"'");
		}
		
		//检索日期
		String riqi="";
		if(!"".equals(request.getParameter("riqi"))&&request.getParameter("riqi")!=null){
			riqi = request.getParameter("riqi");
			request.setAttribute("riqi", riqi);
		}
		
		String where = sb.toString();
		
		//设置检索结果
		int currentpage = 1;
		int pagesize = 50;
		if (request.getParameter("pagenum") != null)
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		List<User> users = userDao.selectAllUserBy((currentpage - 1) * pagesize, pagesize,where);
		request.setAttribute("kaoqinloglist", users);
		request.setAttribute("banjilist", banjiDao.getAll(""));
		request.setAttribute("subjectlist", subjectDao.getAll(""));
		return "success";
	}
	
	
	
}
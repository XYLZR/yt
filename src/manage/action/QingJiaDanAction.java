package manage.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import manage.dao.BanJiDao;
import manage.dao.QingJiaDanDao;
import manage.model.QingJiaDan;
import manage.util.Util;

/**
 * 
 * @author XYLZR
 *	请假单行动类
 *	实现了对请假的一系列操作
 */

public class QingJiaDanAction  {
	
	private static final long serialVersionUID = 7963004028001698964L;

	private QingJiaDanDao qingjiadanDao;//设置和数据库操作的请假单对象
	private BanJiDao banjiDao;//创建和数据库操作的班级对象

	public BanJiDao getBanjiDao() {
		return banjiDao;
	}

	public void setBanjiDao(BanJiDao banjiDao) {
		this.banjiDao = banjiDao;
	}

	public QingJiaDanDao getQingjiadanDao() {
		return qingjiadanDao;
	}

	public void setQingjiadanDao(QingJiaDanDao qingjiadanDao) {
		this.qingjiadanDao = qingjiadanDao;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 	将请假消息显示在页面中
	 * @return
	 * @throws Exception
	 */
	public String qingjiadanlist() throws Exception {	
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//设置页面显示数据的条数
		int numPerPage = 20;
		//设置页面初始值
		int pageNum = 1;
		
		//判断是否换页
		if(request.getParameter("pageNum")!=null && !request.getParameter("pageNum").equals("")){
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		//设置该页面的数据数量
		if(request.getParameter("numPerPage")!=null){
			numPerPage = Integer.parseInt(request.getParameter("numPerPage"));
		}
		
		//获取请假条的数量
		int total = qingjiadanDao.selectAllQingJiaDanCount();
		
		//取出请求中的会话
		HttpSession session = request.getSession();
		//获取session中键为role的值
		String role = (String)session.getAttribute("role");
		
		//判断是否为学生请假
		if("role4".equals(role)||role=="role4"){
			//获取学号
			String codenum=(String) session.getAttribute("codenum"); 
			//设置该学生的请假信息
			request.setAttribute("qingjiadanlist", qingjiadanDao.selectAllQingJiaDanBy((pageNum - 1) * numPerPage, 50,
					"and codenum='"+codenum+"'"));			
		}else{
			//定义请假单List
			List<QingJiaDan> qingjiadans = null;
			//判断是否为管理员
			if(!"role0".equals(role)){
				//获取班级号
				String banjinum=(String) session.getAttribute("banjinum"); 
				//设置班级号为查询条件
				String where = "and banjinum='"+banjinum+"'";
				//将该班级的所有请假信息统计再List中
				qingjiadans = qingjiadanDao.selectAllQingJiaDanBy((pageNum - 1) * numPerPage, numPerPage,where);				
			}else{
				qingjiadans = qingjiadanDao.selectAllQingJiaDan((pageNum - 1) * numPerPage, numPerPage);
			}
			//设置请假信息
			request.setAttribute("qingjiadanlist", qingjiadans);			
		}
		//设置相应的值
		request.setAttribute("totalCount", total);
		request.setAttribute("ps", numPerPage);
		request.setAttribute("pn", pageNum);
				
		return "success";
	}	
	
	/**	
	 * 	设置新增请假条的班级信息
	 * @return
	 */
	public String qingjiadanadd(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//取出请求中的会话
		HttpSession session = request.getSession();
		//取出会话中的键为role的值
		String role = (String)session.getAttribute("role");
		String where = "";
		if(!"role0".equals(role)){
			//获取班级号
			String banjinum=(String) session.getAttribute("banjinum"); 
			where = "and banjinum='"+banjinum+"'";
		}
		//设置获取的where的所有班级信息
		request.setAttribute("banjilist", banjiDao.getAll(where));
		return "success";
	}
	
	/**
	 * 	给新增请假条赋值
	 * @return
	 * @throws Exception
	 */
	public String qingjiadanadd2() throws Exception{
		//获取页面请求
        HttpServletRequest request = ServletActionContext.getRequest();
		//创建请假单对象
		QingJiaDan qingjiadan = new QingJiaDan();
		
		//在请假单对象中设置从页面获取的值
		qingjiadan.setCodenum(request.getParameter("codenum"));
		qingjiadan.setQjtime1(request.getParameter("qjtime1"));
		qingjiadan.setQjtime2(request.getParameter("qjtime2"));
		qingjiadan.setShenhe("审核中");
		qingjiadan.setQjreasons(request.getParameter("qjreasons"));
		qingjiadan.setUsername(request.getParameter("username"));
		qingjiadan.setBanjinum(request.getParameter("banjinum"));
		qingjiadan.setCreatetime(new Date());
		
		//调用新增请假单方法
		qingjiadanDao.insertQingJiaDan(qingjiadan);
		//获取页面请求
		HttpServletResponse resp = ServletActionContext.getResponse();
		//设置页面输出信息
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print("{\"statusCode\":\"200\", \"message\":\"添加成功！\",\"navTabId\":\"qingjiadanList\", \"rel\":\"qingjiadanList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"qingjiadan/qingjiadanlist.html\"}");
		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	修改学生请假单信息
	 * @return
	 */
	public String  qingjiadanupdate(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取指定id的值
		int id = Integer.parseInt(request.getParameter("id"));
		//将返回的值设置进bean中
		request.setAttribute("bean", qingjiadanDao.selectQingJiaDan(id));
		//将获取的id值设置进id中
		request.setAttribute("id", id);
		//给班级页面设置获取的值
		request.setAttribute("banjilist", banjiDao.getAll(""));
		return "success";
	}
	
	
	/**
	 * 	将学生请假单获取的值赋值到数据库
	 * @return
	 * @throws IOException
	 */
	public String  qingjiadanupdate2() throws IOException{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		//设置页面输出信息
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		//获取关键字id的值
		int id = Integer.parseInt(request.getParameter("id"));
		QingJiaDan bean = qingjiadanDao.selectQingJiaDan(id);
		
		//获取原有的信息，再将修改的新值赋进去
		if(request.getParameter("username")!=null){
			bean.setUsername(request.getParameter("username"));
		}
		if(request.getParameter("banjinum")!=null){
			bean.setBanjinum(request.getParameter("banjinum"));
		}
		if(request.getParameter("codenum")!=null){
			bean.setCodenum(request.getParameter("codenum"));
		}
		if(request.getParameter("qjtime1")!=null){
			bean.setQjtime1(request.getParameter("qjtime1"));
			
		}
		if(request.getParameter("qjtime2")!=null){
			bean.setQjtime2(request.getParameter("qjtime2"));
		
		}
		if(request.getParameter("shenhe")!=null){
			bean.setShenhe(request.getParameter("shenhe"));
		}
		if(request.getParameter("shenhecontent")!=null){
			bean.setShenhecontent(request.getParameter("shenhecontent"));
		}
		
		qingjiadanDao.updateQingJiaDan(bean);
		out.print("{\"statusCode\":\"200\", \"message\":\"修改成功！\",\"navTabId\":\"qingjiadanList\", \"rel\":\"qingjiadanList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"qingjiadan///qingjiadanlist.html\"}");

		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	删除选中的请假条信息
	 * @return
	 * @throws IOException
	 */
	public String qingjiadandel() throws IOException{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取关键字id的值
		int id = Integer.parseInt(request.getParameter("id"));
		qingjiadanDao.delQingJiaDan(qingjiadanDao.selectQingJiaDan(id));
		
		//获取页面信息，设置页面输出信息
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.write(manage.util.Util.outPutMsg("200", "删除成功", "qingjiadanList", "", false, "qingjiadan///qingjiadanlist.html"));
		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 审核请假条
	 * @return
	 */
	public String  qingjiadanupdate_sh(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取并设置id的值
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("bean", qingjiadanDao.selectQingJiaDan(id));
		request.setAttribute("id", id);
		return "success";
	}
	
	
	/**
	 * 审核请假条
	 * @return
	 * @throws IOException
	 * @throws Throwable
	 */
	public String  qingjiadanupdate2_sh() throws IOException, Throwable{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		//设置页面输出信息
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		//获取关键字id的值
		int id = Integer.parseInt(request.getParameter("id"));
		QingJiaDan bean = qingjiadanDao.selectQingJiaDan(id);
		
		//获取原有的信息，再给出审批信息
		if(request.getParameter("username")!=null){
			bean.setUsername(request.getParameter("username"));
		}
		if(request.getParameter("banjinum")!=null){
			bean.setBanjinum(request.getParameter("banjinum"));
		}
		if(request.getParameter("codenum")!=null){
			bean.setCodenum(request.getParameter("codenum"));
		}
		if(request.getParameter("qjtime1")!=null){
			bean.setQjtime1(request.getParameter("qjtime1"));
			
		}
		if(request.getParameter("qjtime2")!=null){
			bean.setQjtime2(request.getParameter("qjtime2"));
		
		}
		if(request.getParameter("shenhe")!=null){
			bean.setShenhe(request.getParameter("shenhe"));
		}
		if(request.getParameter("shenhecontent")!=null){
			bean.setShenhecontent(request.getParameter("shenhecontent"));
		}
		
		//获取请假开始时间和结束时间
		String aa=request.getParameter("qjtime1");
		String bb=request.getParameter("qjtime2");
		
		//判断两个时间的值不为空
		if(aa!=null&&bb!=null){
			//获取会话信息
			HttpSession session = request.getSession();
			//从会话中获取键为role的值
			String role = (String)session.getAttribute("role");
			
			//对已使用当前日期和时间进行初始化
			Calendar nowDate = Calendar.getInstance();      
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			//按照日期格式返回一个日期对象
			Date date1 = sdf.parse(aa);
			Date date2 = sdf.parse(bb);
			//算出请假开始时间和结束时间的差值
			long daynum=(date2.getTime()-date1.getTime())/(24*60*60*1000); 
			
			qingjiadanDao.updateQingJiaDan(bean);
			out.print("{\"statusCode\":\"200\", \"message\":\"审核成功！\",\"navTabId\":\"qingjiadanList\", \"rel\":\"qingjiadanList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"qingjiadan///qingjiadanlist.html\"}");
			out.flush();
			out.close();
		}else{
		out.print("{\"statusCode\":\"200\", \"message\":\"审核失败！\",\"navTabId\":\"qingjiadanList\", \"rel\":\"qingjiadanList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"qingjiadan///qingjiadanlist.html\"}");
		out.flush();
		out.close();
	}
		
		return null;
	}
	
}

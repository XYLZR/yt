package manage.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import manage.dao.BanJiDao;
import manage.dao.KechengDao;
import manage.dao.SubjectDao;
import manage.model.BanJi;
import manage.model.Kecheng;
import manage.model.Subject;
import manage.model.User;
import manage.util.Util;


/**
 * 	
 * @author XYLZR
 *	课程管理行动类
 */

public class SubjectAction  {
	
	private static final long serialVersionUID = 7963004028001698964L;
	//定义专业课程接口
	private SubjectDao subjectDao;
	
	//利用setter和getter方法实现对变量的初始化
	public SubjectDao getSubjectDao() {
		return subjectDao;
	}

	public void setSubjectDao(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}


	@SuppressWarnings("unchecked")
	/**
	 * 	设置页面数据
	 * @return
	 * @throws Exception
	 */
	public String subjectlist() throws Exception {	
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
		int total = subjectDao.selectAllSubjectCount();

		//将当前页面的数据存入List中
		List<Subject> subjects = subjectDao.selectAllSubject((pageNum - 1) * numPerPage, numPerPage);

		//将获取的数据写入进网页中
		request.setAttribute("totalCount", total);
		request.setAttribute("ps", numPerPage);
		request.setAttribute("pn", pageNum);
		request.setAttribute("subjectlist", subjects);
		return "success";
	}
	
	
	/**
	 * 	增加专项课程信息
	 * @return
	 */
	public String subjectadd(){
		HttpServletRequest request = ServletActionContext.getRequest();
		return "success";
	}
	
	
	/**
	 * 	对增加的专项课程信息赋值
	 * @return
	 * @throws Exception
	 */
	public String subjectadd2() throws Exception{
		//获取页面请求
        HttpServletRequest request = ServletActionContext.getRequest();
		
    	//新建专项课程类
		Subject subject = new Subject();
		//设置专项课程名
		subject.setSubjectname(request.getParameter("subjectname"));
		//设置创建时间
		subject.setCreatetime(new Date());
		
		//调用新建专项课程方法
		subjectDao.insertSubject(subject);
		
		//获取页面请求
		HttpServletResponse resp = ServletActionContext.getResponse();
		//设置页面输出信息
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print("{\"statusCode\":\"200\", \"message\":\"添加成功！\",\"navTabId\":\"subjectList\", \"rel\":\"subjectList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"subject/subjectlist.html\"}");
		out.flush();
		out.close();
	
		return null;
	}
	
	
	/**
	 * 	修改专项课程信息
	 * @return
	 */
	public String  subjectupdate(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取指定id的值
		int id = Integer.parseInt(request.getParameter("id"));
		//将返回的值设置进bean中
		request.setAttribute("bean", subjectDao.selectSubject(id));
		//将获取的id值设置进id中
		request.setAttribute("id", id);
		return "success";
	}
	
	
	/**
	 * 	将专项课程获取的值赋值到数据库
	 * @return
	 * @throws IOException
	 */
	public String  subjectupdate2() throws IOException{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		//设置页面输出信息
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		//获取关键字id的值
		int id = Integer.parseInt(request.getParameter("id"));
		Subject bean = subjectDao.selectSubject(id);
		
		//获取原有的信息，再将修改的新值赋进去
		if(request.getParameter("subjectname")!=null){
			bean.setSubjectname(request.getParameter("subjectname"));
		}
		
		subjectDao.updateSubject(bean);
		out.print("{\"statusCode\":\"200\", \"message\":\"修改成功！\",\"navTabId\":\"subjectList\", \"rel\":\"subjectList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"subject///subjectlist.html\"}");

		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	删除专项课程信息
	 * @return
	 * @throws IOException
	 */
	public String subjectdel() throws IOException{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取关键字id的值
		int id = Integer.parseInt(request.getParameter("id"));
		subjectDao.delSubject(subjectDao.selectSubject(id));
		
		//获取页面信息，设置页面输出信息
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.write(manage.util.Util.outPutMsg("200", "修改成功", "subjectList", "", false, "subject///subjectlist.html"));
		out.flush();
		out.close();
		return null;
	}
}
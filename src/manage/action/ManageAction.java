package manage.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import manage.dao.BanJiDao;
import manage.dao.KaoQinLogDao;
import manage.dao.UserDao;
import manage.model.BanJi;
import manage.model.KaoQinLog;
import manage.model.User;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;


/**
 * 
 * @author XYLZR
 *	管理数据行动类
 *	主要对用户、考勤、班级进行管理
 */

public class ManageAction extends ActionSupport {

	private static final long serialVersionUID = 7963004028001698964L;
	
	//定义变量
	private UserDao userDao;
	private BanJiDao banjiDao;
	private KaoQinLogDao kaoqinlogDao;

	//利用setter和getter方法实现对变量的初始化
	public KaoQinLogDao getKaoqinlogDao() {
		return kaoqinlogDao;
	}

	public void setKaoqinlogDao(KaoQinLogDao kaoqinlogDao) {
		this.kaoqinlogDao = kaoqinlogDao;
	}

	public BanJiDao getBanjiDao() {
		return banjiDao;
	}

	public void setBanjiDao(BanJiDao banjiDao) {
		this.banjiDao = banjiDao;
	}
	
	//取小树点后2位
	static  double  convert(double  value){  
        long  lg  =  Math.round(value*100); //四舍五入  
        double  d=  lg/100.0;
        return  d;  
    }  
	
	//定义页面数据
	private int numPerPage = 20;
	private int pageNum = 1;
	
	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}	

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * 	用户登录，获取登录用户的信息
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取输入的用户名和登录密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//将获取的数据设置进新建对象中
		User user = userDao.selectUserbByusernameByPassword(username, password);
		
		//判断对象是否为空
		if (user!=null) {
			//将此对象的考勤记录调出
			List<KaoQinLog> kaoqinlogs = kaoqinlogDao.selectAllKaoQinLogBy(0, 100,
					" and codenum='"+user.getCodenum()+"'");
			String kaoqinlogtishi="";
			//判断旷课次数是否超过三次
			if(kaoqinlogs.size()>=3){
				kaoqinlogtishi="旷课次数已经超过3次!!!";
			}else{
				kaoqinlogtishi="";
			}
			
			//获取对话并设置该用户的信息
			HttpSession session = request.getSession();
			session.setAttribute("username", user.getUsername());
			session.setAttribute("codenum", user.getCodenum());
			session.setAttribute("banjinum", user.getBanjinum());
			session.setAttribute("role", "role"+user.getRole());
			session.setAttribute("loginuser", user);
			session.setAttribute("kaoqinlogtishi", kaoqinlogtishi);
			return "success";
		} else {
			//登录失败
			request.setAttribute("errorMessage", "请重新输入帐号密码！！");
			return "fail";
		}
	}

	
	/**
	 * 	退出登录
	 * @return
	 */
	public String loginout() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		session.removeAttribute("role");
		return "success";
	}
	

	/**
	 * 	用户管理页面
	 * @return
	 */
	public String userlist() {
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//定义页面显示的数据量
		int numPerPage = 15;
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
		int total = userDao.selectAllUserCount();
		//将当前页面的数据存入List中
		List<User> users = userDao.selectAllUser((pageNum - 1) * numPerPage, numPerPage);
		//将获取的数据写入进网页中
		request.setAttribute("totalCount", total);
		request.setAttribute("ps", numPerPage);
		request.setAttribute("pn", pageNum);
		request.setAttribute("userlist", users);

		//设置用户信息
		request.setAttribute("searchuserlist", userDao.getAll(" "));
		return "success";
	}
	
	
	/**
	 * 	新增用户
	 * @return
	 */
	public String useradd(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//设置班级信息
		request.setAttribute("banjilist", banjiDao.getAll(""));
		return "success";
	}
	
	
	/**
	 * 	将用户数据赋值至数据库
	 * @return
	 * @throws Exception
	 */
	public String useradd2() throws Exception{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//将页面的数据获取到变量中
		User user = userDao.selectUserByusername(request.getParameter("username"));
		
		//判断是否为空
		if(user!=null){
			//获取页面请求，输出验证信息
			HttpServletResponse resp = ServletActionContext.getResponse();
			resp.setCharacterEncoding("utf-8");
			PrintWriter out = resp.getWriter();
			out.print("{\"statusCode\":\"200\", \"message\":\"该用户名已经存在，添加失败！\",\"navTabId\":\"userList\", \"rel\":\"userList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"userlist.html\"}");
			out.flush();
			out.close();
			return null;
		}
		
		//设置新建用户信息
		user = new User();
		String username = request.getParameter("username");		
		user.setUsername(username);
		user.setPassword(request.getParameter("password"));		
		user.setAddress(request.getParameter("address"));
		user.setBanjinum(request.getParameter("banjinum"));
		user.setCodenum(request.getParameter("username"));
		user.setPhone(request.getParameter("phone"));
		user.setRoom(request.getParameter("room"));
		//设置用户状态
		user.setUserlock(Integer.parseInt(request.getParameter("userlock")));
		//设置新建用户的类型
		user.setRole(Integer.parseInt(request.getParameter("role")));
		
		user.setCreatetime(new Date());
		
		User u = userDao.selectUserbByusernameByPassword(user.getUsername(), user.getPassword());
		
		//判断用户是否存在
		if(u==null){
			userDao.insertUser(user);
			}else{
			request.setAttribute("info", "新增失败，用户帐号已存在，请检查输入");
		}
		//获取页面请求
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.print("{\"statusCode\":\"200\", \"message\":\"添加成功！\",\"navTabId\":\"userList\", \"rel\":\"userList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"userlist.html\"}");
		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	删除用户
	 * @return
	 * @throws IOException
	 */
	public String userdel() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		int id = Integer.parseInt(request.getParameter("id"));
		userDao.delUser(userDao.selectUser(id));
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.write(manage.util.Util.outPutMsg("200", "删除成功", "userList", "", false, "userlist.html"));
		out.flush();
		out.close();
		return null;
	}

	
	/**
	 * 	修改用户数据
	 * @return
	 */
	public String  userupdate(){
		HttpServletRequest request = ServletActionContext.getRequest();
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("bean", userDao.selectUser(id));
		request.setAttribute("id", id);
		request.setAttribute("banjilist", banjiDao.getAll(""));
		return "success";
	}
	
	
	/**
	 * 	将修改用户的值赋值至数据库
	 * @return
	 * @throws Exception
	 */
	public String userupdate2() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		int id = Integer.parseInt(request.getParameter("id"));
		User bean = userDao.selectUser(id);
		
		//修改每一项信息
		if(request.getParameter("username")!=null){
			bean.setUsername(request.getParameter("username"));
		}
		if(request.getParameter("password")!=null){
			bean.setPassword(request.getParameter("password"));
		}
		if(request.getParameter("address")!=null){
			bean.setAddress(request.getParameter("address"));
		}
		if(bean.getRole()==2){
			if(request.getParameterValues("banjinum")!=null){
				String[] banjinums=request.getParameterValues("banjinum");
				String banjinum="";
				for(String str : banjinums){
					banjinum=banjinum+str;
					bean.setBanjinum(banjinum);
				}
			}			
		}else{
			if(request.getParameter("banjinum")!=null){
				bean.setBanjinum(request.getParameter("banjinum"));
			}
		}
		if(request.getParameter("phone")!=null){
			bean.setPhone(request.getParameter("phone"));
		}
		if(request.getParameter("room")!=null){
			bean.setRoom(request.getParameter("room"));
		}
		if(request.getParameter("userlock")!=null){
			bean.setUserlock(Integer.parseInt(request.getParameter("userlock")));
		}
		if(request.getParameter("role")!=null){
			bean.setRole(Integer.parseInt(request.getParameter("role")));
		}
		userDao.updateUser(bean);
		out.print("{\"statusCode\":\"200\", \"message\":\"修改成功！\",\"navTabId\":\"userList\", \"rel\":\"userList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"userlist.html\"}");

		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	检索用户
	 * @return
	 */
	public String searchuser(){
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取检索关键字
		String keyword  = request.getParameter("keyword");
		
		int currentpage = 1;
		int pagesize = 50;
		if (request.getParameter("pagenum") != null)
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		
		request.setAttribute("userlist", userDao.selectAllUserByusername((currentpage - 1) * pagesize, 50,keyword));
		request.setAttribute("searchuserlist", userDao.getAll(" and userlock=0 "));
		return "success";
		
	}
	
	/**
	 * 	修改登录密码
	 * @return
	 */
	public String  passwordupdateok(){
		HttpServletRequest request = ServletActionContext.getRequest();
		return "success";
	}
	
	
	/**
	 * 	将修改的密码赋值至数据库
	 * @return
	 * @throws IOException
	 */
	public String  passwordupdate() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		//获取登录用户名
		String username = (String)session.getAttribute("codenum");

		String password1 = request.getParameter("oldpwd");
		String password2 = request.getParameter("newspwd");
		User u = userDao.selectUserbByusernameByPassword(username, password1);
		if(u!=null){
			u.setPassword(password2);
			userDao.updateUser(u);
			request.setAttribute("info", "密码修改成功");
		}else{
			request.setAttribute("info", "修改密码失败，请先确认原密码是否正确");
		}
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		if(u!=null){
			out.print("{\"statusCode\":\"200\", \"message\":\"密码修改成功！\",\"navTabId\":\"userList\", \"rel\":\"userList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"userlist.html\"}");
		}else{
			out.print("{\"statusCode\":\"200\", \"message\":\"修改密码失败，请先确认原密码是否正确！\",\"navTabId\":\"userList\", \"rel\":\"userList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"userlist.html\"}");
		}
		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 	查看用户信息
	 * @return
	 */
	public String  usershow(){
		HttpServletRequest request = ServletActionContext.getRequest();
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("bean", userDao.selectUser(id));
		request.setAttribute("id", id);
		request.setAttribute("banjilist", banjiDao.getAll(""));
		return "success";
	}
}

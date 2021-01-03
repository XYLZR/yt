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
import manage.model.BanJi;
import manage.model.Kecheng;
import manage.model.User;
import manage.util.Util;

/**
 * 
 * @author XYLZR
 * 	班级行动类
 *
 */

public class BanJiAction  {
	
	private static final long serialVersionUID = 7963004028001698964L;

	private BanJiDao banjiDao;//定义班级接口

	
	public BanJiDao getBanjiDao() {
		return banjiDao;
	}

	public void setBanjiDao(BanJiDao banjiDao) {
		this.banjiDao = banjiDao;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 	设置页面数据
	 * @return
	 * @throws Exception
	 */
	public String banjilist() throws Exception {	
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//定义页面可以放多少数据
		int numPerPage = 10;
		//定义页面初始值
		int pageNum = 1;
		
		//判断是否换页
		if(request.getParameter("pageNum")!=null && !request.getParameter("pageNum").equals("")){
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		//设置该页面的数据数量
		if(request.getParameter("numPerPage")!=null){
			numPerPage = Integer.parseInt(request.getParameter("numPerPage"));
		}
		
		//统计所有数据数量
		int total = banjiDao.selectAllBanJiCount();
		
		//将当前页面的数据存入List中
		List<BanJi> banjis = banjiDao.selectAllBanJi((pageNum - 1) * numPerPage, numPerPage);
		
		//将获取的数据写入进网页中
		request.setAttribute("totalCount", total);
		request.setAttribute("ps", numPerPage);
		request.setAttribute("pn", pageNum);
		request.setAttribute("banjilist", banjis);
		return "success";
	}
	
	/**
	 * 新建班级
	 * @return	成功
	 */
	public String banjiadd(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		return "success";
	}
	
	/**
	 * 	新建班级
	 * @return
	 * @throws Exception
	 */
	public String banjiadd2() throws Exception{
		//获取页面请求
        HttpServletRequest request = ServletActionContext.getRequest();
		//初始化班级
		BanJi banji = new BanJi();
		
		//在页面中新建班级
		banji.setBanjiname(request.getParameter("banjiname"));
		banji.setBanjinum(request.getParameter("banjinum"));
		banji.setYuanxi(request.getParameter("yuanxi"));
		banji.setZhuanye(request.getParameter("zhuanye"));
		banji.setCreatetime(new Date());
		
		//获取查询的数据；获取班级编号信息
		List<BanJi> num = banjiDao.selectAllBanJiBy(0, 10, "and banjinum='"+request.getParameter("banjinum")+"'");
		//获取查询的数据；获取班级名信息
		List<BanJi> info = banjiDao.selectAllBanJiBy(0, 10, "and banjiname='"+request.getParameter("banjiname")+"'");
		
		//判断班级编号数据是否存在
		if(num.size()==0&&info.size()==0){
			banjiDao.insertBanJi(banji);
			
			//获取页面请求
			HttpServletResponse resp = ServletActionContext.getResponse();
			
			//输出添加成功提示信息
			resp.setCharacterEncoding("utf-8");
			PrintWriter out = resp.getWriter();
			out.print("{\"statusCode\":\"200\", \"message\":\"添加成功！\",\"navTabId\":\"banjiList\", \"rel\":\"banjiList\", "
					+ "\"callbackType\":\"closeCurrent\",\"forwardUrl\":\"banji/banjilist.html\"}");
			out.flush();
			out.close();
		}else{
			//新建的班级编号已存在
			request.setAttribute("info", "新增班级失败，班级编号或班级名已存在，请检查输入");
			
			//获取页面请求
			HttpServletResponse resp = ServletActionContext.getResponse();
			
			//输出添加失败提示信息
			resp.setCharacterEncoding("utf-8");
			PrintWriter out = resp.getWriter();
			out.print("{\"statusCode\":\"200\", \"message\":\"添加失败，班级编号或班级名已存在！\",\"navTabId\":\"banjiList\", "
					+ "\"rel\":\"banjiList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"banji/banjilist.html\"}");
			out.flush();
			out.close();
		}
		return null;
	}
	
	/**
	 * 修改班级
	 * @return	成功
	 */
	public String  banjiupdate(){
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取id
		int id = Integer.parseInt(request.getParameter("id"));
		//用于分辨数据存储点
		request.setAttribute("bean", banjiDao.selectBanJi(id));
		//设置班级页面专属id
		request.setAttribute("id", id);
		return "success";
	}
	
	/**
	 * 	将获取的值赋值到数据库
	 * @return
	 * @throws IOException
	 */
	public String  banjiupdate2() throws IOException{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		
		//定义信息，方便输出信息至页面
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		//获取选中对象
		int id = Integer.parseInt(request.getParameter("id"));
		BanJi bean = banjiDao.selectBanJi(id);
		
		//获取原有的信息，再修改新值进去
		if(request.getParameter("banjiname")!=null){
			bean.setBanjiname(request.getParameter("banjiname"));
		}
		if(request.getParameter("banjinum")!=null){
			bean.setBanjinum(request.getParameter("banjinum"));
		}
		if(request.getParameter("yuanxi")!=null){
			bean.setYuanxi(request.getParameter("yuanxi"));
		}
		if(request.getParameter("zhuanye")!=null){
			bean.setZhuanye(request.getParameter("zhuanye"));
		}
		
		banjiDao.updateBanJi(bean);
		out.print("{\"statusCode\":\"200\", \"message\":\"修改成功！\",\"navTabId\":\"banjiList\", \"rel\":\"banjiList\", \"callbackType\":\"closeCurrent\",\"forwardUrl\":\"banji///banjilist.html\"}");

		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 删除选中的班级信息
	 * @return
	 * @throws IOException
	 */
	public String banjidel() throws IOException{
		//获取页面请求
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取选中对象
		int id = Integer.parseInt(request.getParameter("id"));
		//调用删除函数
		banjiDao.delBanJi(banjiDao.selectBanJi(id));
		//获取页面请求
		HttpServletResponse resp = ServletActionContext.getResponse();
		//定义信息，方便输出信息至页面
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.write(manage.util.Util.outPutMsg("200", "修改成功", "banjiList", "", false, "banji///banjilist.html"));
		out.flush();
		out.close();
		return null;
	}
	
	
}

package manage.dao;

import java.util.List;

import manage.model.BanJi;

/**
 * 
 * @author XYLZR
 *	班级接口
 */

public interface BanJiDao  {
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<BanJi> getAll(String where);
	
	/**删除选定班级信息*/
	public void delBanJi(BanJi banji);
	
	/**新建班级信息*/
	public void insertBanJi(BanJi banji);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<BanJi> selectAllBanJi(final int start, final int limit);
	
	/**统计所有班级信息*/
	public int selectAllBanJiCount();
	
	/**修改选定的班级信息*/
	public void updateBanJi(BanJi banji);
	
	/**返回指定id的班级信息，用于处理修改班级和删除班级*/
	public BanJi selectBanJi(int id);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<BanJi> selectAllBanJiBy(final int start,final int limit,final String keyword);
}

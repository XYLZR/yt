package manage.dao;

import java.util.List;

import manage.model.Kecheng;

/**
 * 
 * @author XYLZR
 *	课程接口
 */

public interface KechengDao  {
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<Kecheng> getAll(String where);
	
	/**删除选中的课程实例*/
	public void delKecheng(Kecheng kecheng);
	
	/**保存新的课程实例*/
	public void insertKecheng(Kecheng kecheng);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<Kecheng> selectAllKecheng(final int start, final int limit);
	
	/**统计所有课程信息*/
	public int selectAllKechengCount();
	
	/**修改选中的课程实例*/
	public void updateKecheng(Kecheng kecheng);
	
	/**返回课程指定id的数据*/
	public Kecheng selectKecheng(int id);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<Kecheng> selectAllKechengBy(final int start,final int limit,final String keyword);
	
}

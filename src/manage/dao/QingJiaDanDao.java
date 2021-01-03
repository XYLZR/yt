package manage.dao;

import java.util.List;

import manage.model.QingJiaDan;

/**
 * 
 * @author XYLZR
 * 请假单接口
 *
 */

public interface QingJiaDanDao  {
	/**获取数据库中的所有信息*/
	public List<QingJiaDan> getAll(String where);
	
	/**删除数据库中的指定对象*/
	public void delQingJiaDan(QingJiaDan qingjiadan);
	
	/**在数据库中插入新数据*/
	public void insertQingJiaDan(QingJiaDan qingjiadan);
	
	/**及时更新所有请假数据*/
	public List<QingJiaDan> selectAllQingJiaDan(final int start, final int limit);
	
	/**查询所有学生的请假信息的条数*/
	public int selectAllQingJiaDanCount();
	
	/**修改数据库中的数据*/
	public void updateQingJiaDan(QingJiaDan qingjiadan);
	
	/**返回请假单指定id的数据*/
	public QingJiaDan selectQingJiaDan(int id);
	
	/**当前页面数据的数量*/
	public List<QingJiaDan> selectAllQingJiaDanBy(final int start,final int limit,final String keyword);
	
}

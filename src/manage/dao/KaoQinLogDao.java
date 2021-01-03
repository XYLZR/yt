package manage.dao;

import java.util.List;

import manage.model.KaoQinLog;


/**
 * 
 * @author XYLZR
 *	考勤接口
 */

public interface KaoQinLogDao  {
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<KaoQinLog> getAll(String where);

	/**删除选中的考勤实例*/
	public void delKaoQinLog(KaoQinLog kaoqinlog);

	/**保存新的考勤实例*/
	public void insertKaoQinLog(KaoQinLog kaoqinlog);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<KaoQinLog> selectAllKaoQinLog(final int start, final int limit);

	/**统计所有考勤信息*/
	public int selectAllKaoQinLogCount();

	/**修改选中的考勤实例*/
	public void updateKaoQinLog(KaoQinLog kaoqinlog);

	/**返回考勤指定id的数据*/
	public KaoQinLog selectKaoQinLog(int id);

	/**将查询到的数据统计到List中，并返回*/
	public List<KaoQinLog> selectAllKaoQinLogBy(final int start,final int limit,final String keyword);
	
}

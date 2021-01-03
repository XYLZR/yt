package manage.dao;

import java.util.List;

import manage.model.Subject;

/**
 * 	
 * @author XYLZR
 *	专项课程管理接口
 */

public interface SubjectDao  {
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<Subject> getAll(String where);

	/**删除选中的专项课程实例*/
	public void delSubject(Subject banji);

	/**保存新的专项课程实例*/
	public void insertSubject(Subject banji);

	/**将查询到的数据统计到List中，并返回*/
	public List<Subject> selectAllSubject(final int start, final int limit);

	/**统计所有专项课程信息*/
	public int selectAllSubjectCount();

	/**修改选中的专项课程实例*/
	public void updateSubject(Subject banji);

	/**返回专项课程指定id的数据*/
	public Subject selectSubject(int id);

	/**将查询到的数据统计到List中，并返回*/
	public List<Subject> selectAllSubjectBy(final int start,final int limit,final String keyword);
	
}

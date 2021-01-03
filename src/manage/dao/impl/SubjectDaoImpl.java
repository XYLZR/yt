package manage.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import manage.dao.SubjectDao;
import manage.model.Subject;

/**
 * 
 * @author XYLZR
 *	专项课程接口实现类
 */

public class SubjectDaoImpl extends HibernateDaoSupport implements  SubjectDao{
	
	@SuppressWarnings("unchecked")
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<Subject> getAll(String where) {
		return this.getHibernateTemplate().find("from Subject where 1=1 "+where+" order by id");
	}
	
	/**保存新的专项课程实例*/
	public void insertSubject(Subject banji){
		this.getHibernateTemplate().save(banji);
	}
	
	/**删除选中的专项课程实例*/
	public void delSubject(Subject banji) {
		this.getHibernateTemplate().delete(banji);
	}
	
	/**修改选中的专项课程实例*/
	public void updateSubject(Subject banji) {
		this.getHibernateTemplate().update(banji);
		
	}

	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置*/
	public List<Subject> selectAllSubject(final int start,final int limit) {
		return (List<Subject>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {				
				List<Subject> list = session.createQuery("from Subject  order by id desc")
				.setFirstResult(start)
				.setMaxResults(limit)
				.list();
				return list;
			}
		});
	}

	/**统计所有专项课程信息
	 * return：返回统计的数据*/
	public int selectAllSubjectCount() {
		long count = (Long)this.getHibernateTemplate().find("select count(*) from Subject").get(0);
		return (int)count;
	}

	/**返回专项课程指定id的数据*/
	public Subject selectSubject(int id) {
		return this.getHibernateTemplate().get(Subject.class, id);
	}
	
	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置
	 *keyword：查询关键字*/
	public List<Subject> selectAllSubjectBy(final int start,final int limit,final String keyword) {
		return (List<Subject>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {				
				List<Subject> list = session.createQuery("from Subject where 1=1 "+keyword+" order by id desc")
				.setFirstResult(start)
				.setMaxResults(limit)
				.list();
				return list;
			}
		});
	}
	
}

package manage.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import manage.dao.KaoQinLogDao;
import manage.model.KaoQinLog;


/**
 * 
 * @author XYLZR
 *	考勤接口实现类
 */

public class KaoQinLogDaoImpl extends HibernateDaoSupport implements  KaoQinLogDao{
	
	@SuppressWarnings("unchecked")
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<KaoQinLog> getAll(String where) {
		return this.getHibernateTemplate().find("from KaoQinLog where 1=1 "+where+" order by id");
	}
	
	/**保存新的考勤实例*/
	public void insertKaoQinLog(KaoQinLog kaoqinlog){
		this.getHibernateTemplate().save(kaoqinlog);
	}
	
	/**删除选中的考勤实例*/
	public void delKaoQinLog(KaoQinLog kaoqinlog) {
		this.getHibernateTemplate().delete(kaoqinlog);
	}
	
	/**修改选中的考勤实例*/
	public void updateKaoQinLog(KaoQinLog kaoqinlog) {
		this.getHibernateTemplate().update(kaoqinlog);
		
	}

	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置*/
	public List<KaoQinLog> selectAllKaoQinLog(final int start,final int limit) {
		return (List<KaoQinLog>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {				
				List<KaoQinLog> list = session.createQuery("from KaoQinLog  order by id desc")
				.setFirstResult(start)
				.setMaxResults(limit)
				.list();
				return list;
			}
		});
	}

	/**统计所有考勤信息
	 * return：返回统计的数据*/
	public int selectAllKaoQinLogCount() {
		long count = (Long)this.getHibernateTemplate().find("select count(*) from KaoQinLog").get(0);
		return (int)count;
	}

	/**返回考勤指定id的数据*/
	public KaoQinLog selectKaoQinLog(int id) {
		return this.getHibernateTemplate().get(KaoQinLog.class, id);
	}
	
	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置
	 *keyword：查询关键字*/
	public List<KaoQinLog> selectAllKaoQinLogBy(final int start,final int limit,final String keyword) {
		return (List<KaoQinLog>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {				
				List<KaoQinLog> list = session.createQuery("from KaoQinLog where 1=1 "+keyword+" order by id desc")
				.setFirstResult(start)
				.setMaxResults(limit)
				.list();
				return list;
			}
		});
	}
}
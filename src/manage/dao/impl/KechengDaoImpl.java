package manage.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import manage.dao.KechengDao;
import manage.model.Kecheng;


/**
 * 
 * @author XYLZR
 *	课程接口实现类
 */

public class KechengDaoImpl extends HibernateDaoSupport implements  KechengDao{
	
	@SuppressWarnings("unchecked")
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<Kecheng> getAll(String where) {
		return this.getHibernateTemplate().find("from Kecheng where 1=1 "+where+" order by id");
	}
	
	/**保存新的课程实例*/
	public void insertKecheng(Kecheng kecheng){
		this.getHibernateTemplate().save(kecheng);
	}
	
	/**删除选中的课程实例*/
	public void delKecheng(Kecheng kecheng) {
		this.getHibernateTemplate().delete(kecheng);
	}
	
	/**修改选中的课程实例*/
	public void updateKecheng(Kecheng kecheng) {
		this.getHibernateTemplate().update(kecheng);
		
	}

	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置*/
	public List<Kecheng> selectAllKecheng(final int start,final int limit) {
		return (List<Kecheng>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {				
				List<Kecheng> list = session.createQuery("from Kecheng  order by id desc")
				.setFirstResult(start)
				.setMaxResults(limit)
				.list();
				return list;
			}
		});
	}

	/**统计所有课程信息
	 * return：返回统计的数据*/
	public int selectAllKechengCount() {
		long count = (Long)this.getHibernateTemplate().find("select count(*) from Kecheng").get(0);
		return (int)count;
	}

	/**返回课程指定id的数据*/
	public Kecheng selectKecheng(int id) {
		return this.getHibernateTemplate().get(Kecheng.class, id);
	}
	
	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置
	 *keyword：查询关键字*/
	public List<Kecheng> selectAllKechengBy(final int start,final int limit,final String keyword) {
		return (List<Kecheng>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {				
				List<Kecheng> list = session.createQuery("from Kecheng where 1=1 "+keyword+" order by kechenglock")
				.setFirstResult(start)
				.setMaxResults(limit)
				.list();
				return list;
			}
		});
	}
	
}

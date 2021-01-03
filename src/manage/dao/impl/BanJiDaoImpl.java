package manage.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import manage.dao.BanJiDao;
import manage.model.BanJi;

public class BanJiDaoImpl extends HibernateDaoSupport implements  BanJiDao{
	
	@SuppressWarnings("unchecked")
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<BanJi> getAll(String where) {
		return this.getHibernateTemplate().find("from BanJi where 1=1 "+where+" order by id");
	}
	
	/**新建班级信息*/
	public void insertBanJi(BanJi banji){
		this.getHibernateTemplate().save(banji);
	}
	
	/**删除选定班级信息*/
	public void delBanJi(BanJi banji) {
		this.getHibernateTemplate().delete(banji);
	}
	
	/**修改选定的班级信息*/
	public void updateBanJi(BanJi banji) {
		this.getHibernateTemplate().update(banji);
		}

	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置*/
	public List<BanJi> selectAllBanJi(final int start,final int limit) {
		/**返回一个idList给调用者，通过new一个HibernateCallback实例在有效的Hibernate数据访问中使用*/
		return (List<BanJi>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			/**该方法是Spring执行的持久化操作，在请假单内按id属性条件查询*/
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {		
				
				List<BanJi> list = session.createQuery("from BanJi  order by id desc")
						.setFirstResult(start).setMaxResults(limit).list();
				return list;//将固定的start和limit条件查询出来的结果存储进list并返回
			}
		});
	}

	/**统计所有班级信息*/
	public int selectAllBanJiCount() {
		long count = (Long)this.getHibernateTemplate().find("select count(*) from BanJi").get(0);
		return (int)count;
	}

	/**返回指定id的班级信息，用于处理修改班级和删除班级
	 * return：返回统计的数据*/
	public BanJi selectBanJi(int id) {
		return this.getHibernateTemplate().get(BanJi.class, id);
	}
	
	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置
	 *keyword：查询关键字*/
	public List<BanJi> selectAllBanJiBy(final int start,final int limit,final String keyword) {
		/**返回一个idList给调用者，通过new一个HibernateCallback实例在有效的Hibernate数据访问中使用*/
		return (List<BanJi>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			/**该方法是Spring执行的持久化操作，在请假单内按关键词keyword条件查询*/
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {				
				
				List<BanJi> list = session.createQuery("from BanJi where 1=1 "+keyword+" order by id desc")
				.setFirstResult(start).setMaxResults(limit).list();
				return list;//将固定的start和limit按照keyword条件查询出来的结果存储进list并返回
			}
		});
	}
}
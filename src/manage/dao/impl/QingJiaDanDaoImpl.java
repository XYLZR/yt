package manage.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import manage.dao.QingJiaDanDao;
import manage.model.QingJiaDan;
/**
 * 
 * @author XYLZR
 *请假单接口实现类
 *此类通过Hibernate对数据库进行操作，主要是增删查改！
 */
public class QingJiaDanDaoImpl extends HibernateDaoSupport implements  QingJiaDanDao{
	
	@SuppressWarnings("unchecked")
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<QingJiaDan> getAll(String where) {
		return this.getHibernateTemplate().find("from QingJiaDan where 1=1 "+where+" order by id");
	}
	
	/**保存新的请假单实例*/
	public void insertQingJiaDan(QingJiaDan qingjiadan){
		this.getHibernateTemplate().save(qingjiadan);
	}
	
	
	/**删除选中的请假单实例*/
	public void delQingJiaDan(QingJiaDan qingjiadan) {
		this.getHibernateTemplate().delete(qingjiadan);
	}
	
	/**修改选中的请假单实例*/
	public void updateQingJiaDan(QingJiaDan qingjiadan) {
		this.getHibernateTemplate().update(qingjiadan);
	}

	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置*/
	public List<QingJiaDan> selectAllQingJiaDan(final int start,final int limit) {
		/**返回一个idList给调用者，通过new一个HibernateCallback实例在有效的Hibernate数据访问中使用*/
		return (List<QingJiaDan>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			/**该方法是Spring执行的持久化操作，在请假单内按id属性条件查询*/
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {		
				
				List<QingJiaDan> list = session.createQuery("from QingJiaDan  order by id desc")
						.setFirstResult(start).setMaxResults(limit).list();
				return list;//将固定的start和limit条件查询出来的结果存储进list并返回
				
			}
		});
	}

	/**统计所有学生的请假信息，包括未批准和已批准的
	 * return：返回统计的数据*/
	public int selectAllQingJiaDanCount() {
		long count = (Long)this.getHibernateTemplate().find("select count(*) from QingJiaDan").get(0);
		return (int)count;
	}

	/**返回请假单指定id的数据*/
	public QingJiaDan selectQingJiaDan(int id) {
		return this.getHibernateTemplate().get(QingJiaDan.class, id);
	}
	
	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置
	 *keyword：查询关键字*/
	public List<QingJiaDan> selectAllQingJiaDanBy(final int start,final int limit,final String keyword) {
		/**返回一个idList给调用者，通过new一个HibernateCallback实例在有效的Hibernate数据访问中使用*/
		return (List<QingJiaDan>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			/**该方法是Spring执行的持久化操作，在请假单内按关键词keyword条件查询*/
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {
				
				List<QingJiaDan> list = session.createQuery("from QingJiaDan where 1=1 "+keyword+" order by id desc")
						.setFirstResult(start).setMaxResults(limit).list();
				return list;//将固定的start和limit按照keyword条件查询出来的结果存储进list并返回
			}
		});
	}
	
}

package manage.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import manage.dao.UserDao;
import manage.model.User;

/**
 * 
 * @author XYLZR
 *	用户接口实现类
 */


public class UserDaoImpl extends HibernateDaoSupport implements  UserDao{
	
	@SuppressWarnings("unchecked")
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<User> getAll(String where) {
		return this.getHibernateTemplate().find("from User where 1=1 "+where+" order by id");
	}
	
	/**保存新的用户实例*/
	public void insertUser(User user){
		this.getHibernateTemplate().save(user);
	}
	
	/**删除选中的用户实例*/
	public void delUser(User user) {
		this.getHibernateTemplate().delete(user);
	}
	
	@SuppressWarnings("unchecked")
	/**根据用户名查询
	 * 返回list存储对象*/
	public User selectUserByusername(String username) {
		List<User> list = this.getHibernateTemplate().find("from User where username=? and userlock=0",username);
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	/**根据用户名和登陆密码查询
	 * 返回list存储对象*/
	public User selectUserbByusernameByPassword(String username, String password) {
		List<User> list = this.getHibernateTemplate().find("from User where codenum=? and password = ? and userlock=0",username,password);
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	}
	
	/**修改用户实例*/
	public void updateUser(User user) {
		this.getHibernateTemplate().update(user);
		
	}

	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置*/
	public List<User> selectAllUser(final int start,final int limit) {
		/**返回一个idList给调用者，通过new一个HibernateCallback实例在有效的Hibernate数据访问中使用*/
		return (List<User>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			/**该方法是Spring执行的持久化操作，在用户内按id属性条件查询*/
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {		
				
				List<User> list = session.createQuery("from User order by id desc").setFirstResult(start).setMaxResults(limit).list();
				return list;//将固定的start和limit条件查询出来的结果存储进list并返回
			}
		});
	}

	/**统计所有用户信息
	 * return：返回统计的数据*/
	public int selectAllUserCount() {
		long count = (Long)this.getHibernateTemplate().find("select count(*) from User").get(0);
		return (int)count;
	}

	/**返回用户内指定id的数据*/
	public User selectUser(int id) {
		return this.getHibernateTemplate().get(User.class, id);
	}
	
	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置
	 *keyword：查询关键字*/
	public List<User> selectAllUserByusername(final int start,final int limit,final String keyword) {
		/**返回一个idList给调用者，通过new一个HibernateCallback实例在有效的Hibernate数据访问中使用*/
		return (List<User>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			/**该方法是Spring执行的持久化操作，在用户内按关键词keyword条件查询*/
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {	
				
				List<User> list = session.createQuery("from User where  username like ? order by id desc")
						.setParameter(0, "%"+keyword+"%").setFirstResult(start).setMaxResults(limit).list();
				return list;//将固定的start和limit按照keyword条件查询出来的结果存储进list并返回
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	/**将查询到的数据统计到List中，并返回
	 *start：数据开始位置
	 *limit：数据结束位置
	 *keyword：查询关键字*/
	public List<User> selectAllUserBy(final int start,final int limit,final String keyword) {
		/**返回一个idList给调用者，通过new一个HibernateCallback实例在有效的Hibernate数据访问中使用*/
		return (List<User>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			/**该方法是Spring执行的持久化操作，在用户内按关键词keyword条件查询*/
			public Object doInHibernate(final Session session)throws HibernateException, SQLException {		
				
				List<User> list = session.createQuery("from User where  1=1 "+keyword+" order by id desc")
						.setFirstResult(start).setMaxResults(limit).list();
				return list;//将固定的start和limit按照keyword条件查询出来的结果存储进list并返回
			}
		});
	}
	
}

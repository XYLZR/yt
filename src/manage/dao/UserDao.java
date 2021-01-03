package manage.dao;

import java.util.List;

import manage.model.User;

/**
 * 
 * @author XYLZR
 *	用户接口
 */

public interface UserDao  {
	/**返回从数据库查询id的结果，存储至List集合中*/
	public List<User> getAll(String where);
	
	/**删除选中的用户实例*/
	public void delUser(User user);

	/**保存新的用户实例*/
	public void insertUser(User user);
	
	/**根据用户名查询*/
	public User selectUserByusername(String username);
	
	/**根据用户名和登陆密码查询*/
	public User selectUserbByusernameByPassword(String username,String password);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<User> selectAllUser(final int start, final int limit);
	
	/**统计所有用户信息*/
	public int selectAllUserCount();
	
	/**修改用户实例*/
	public void updateUser(User user);
	
	/**返回用户内指定id的数据*/
	public User selectUser(int id);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<User> selectAllUserByusername(final int start,final int limit,final String keyword);
	
	/**将查询到的数据统计到List中，并返回*/
	public List<User> selectAllUserBy(final int start,final int limit,final String keyword);
	
}

package ute.hibook.dao;

import java.util.List;

import ute.hibook.entity.User;

public interface UserDao {

	public void addUser(User user);
	public void updateUser(User user);
	public void deleteUser(int idUser);
	public User getUserById(int idUser);
	public List<User> getAllUser();
	public User getUserByEmail(String email);
	public boolean updateRoleUser(int idUser, int idRole);
	
}

package ute.hibook.dao.imp;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ute.hibook.dao.UserDao;
import ute.hibook.entity.User;

@Repository
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
@Transactional
public class UserDaoImpl implements UserDao{

	@Autowired
	SessionFactory sessionFactory;
	
	public void addUser(User user) {
		sessionFactory.getCurrentSession().save(user);
	}

	public void updateUser(User user) {
		sessionFactory.getCurrentSession().update(user);
	}

	public void deleteUser(int idUser) {
		sessionFactory.getCurrentSession().delete(getUserById(idUser));
	}

	public User getUserById(int idUser) {
		return sessionFactory.getCurrentSession().get(User.class, idUser);
	}

	public List<User> getAllUser() {
		return sessionFactory.getCurrentSession().createQuery("from user").getResultList();
	}

	public User getUserByEmail(String email) {
		Session session=sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("FROM user WHERE email = :email");
			query.setParameter("email", email);
			User user = (User) query.getSingleResult();
			return user;
		} catch(Exception se) {
			return null;
		}
	}

	public boolean updateRoleUser(int idUser, int idRole) {
		Session session=sessionFactory.getCurrentSession();
		Query us= session.createQuery("update user set idRole= :idRole where idUser = :idUser");
		us.setParameter("idRole", idRole);
		us.setParameter("idUser", idUser);
		try{
			us.executeUpdate();
		}catch (Exception e) {
			return false;
		}
		return true;
	}
}

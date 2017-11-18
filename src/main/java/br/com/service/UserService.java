package br.com.service;

import br.com.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}

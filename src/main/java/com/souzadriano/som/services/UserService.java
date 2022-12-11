package com.souzadriano.som.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.User;
import com.souzadriano.som.jpaentities.UserEntity;
import com.souzadriano.som.mappers.UserMapper;
import com.souzadriano.som.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	private UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public List<User> findAll() {
		return userMapper.toObject(userRepository.findByDisabled(Boolean.FALSE,
				Sort.by("name").ascending().and(Sort.by("email").ascending())));
	}

	public User create(User user) {
		UserEntity entity = userMapper.toEntity(user);
		entity.setUserId(null);
		return userMapper.toObject(userRepository.save(entity));
	}

	public User findOne(Long userId) {
		return userRepository.findOneByUserIdAndDisabled(userId, Boolean.FALSE).map(entity -> userMapper.toObject(entity))
				.orElseThrow(IllegalArgumentException::new);
	}

	public User update(Long userId, User user) {
		userRepository.findOneByUserIdAndDisabled(userId, Boolean.FALSE).map(entity -> userMapper.toObject(entity))
				.orElseThrow(IllegalArgumentException::new);
		UserEntity entity = userMapper.toEntity(user);
		return userMapper.toObject(userRepository.save(entity));
	}

	public void delete(Long userId) {
		UserEntity userEntity = userRepository.findOneByUserIdAndDisabled(userId, Boolean.FALSE)
				.orElseThrow(IllegalArgumentException::new);
		userEntity.setDisabled(Boolean.TRUE);
		userRepository.save(userEntity);
	}

}

package com.souzadriano.som.mappers;

import org.springframework.stereotype.Component;

import com.souzadriano.som.entities.User;
import com.souzadriano.som.jpaentities.UserEntity;

@Component
public class UserMapper extends Mapper<User, UserEntity> {

	@Override
	public User toObject(UserEntity entity) {
		return new User(entity.getUserId(), entity.getName(), entity.getEmail());
	}

	@Override
	public UserEntity toEntity(User object) {
		return new UserEntity(object.getUserId(), object.getName(), object.getEmail(), Boolean.FALSE);
	}

}

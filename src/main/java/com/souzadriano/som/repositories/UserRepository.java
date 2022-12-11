package com.souzadriano.som.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.souzadriano.som.jpaentities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	public List<UserEntity> findByDisabled(Boolean disabled, Sort sort);
	
	public Optional<UserEntity> findOneByUserIdAndDisabled(Long userId, Boolean disabled);
}

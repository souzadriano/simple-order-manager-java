package com.souzadriano.som.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.souzadriano.som.jpaentities.EmailLogEntity;

public interface EmailLogRepository extends JpaRepository<EmailLogEntity, Long> {

}

package org.japa.repository;

import org.japa.entity.AdGlobalConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdGlobalConfigRepository extends JpaRepository<AdGlobalConfigEntity, String> {
}

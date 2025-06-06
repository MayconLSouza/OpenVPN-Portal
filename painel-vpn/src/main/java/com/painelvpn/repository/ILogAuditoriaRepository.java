package com.painelvpn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.painelvpn.model.LogAuditoria;

@Repository
public interface ILogAuditoriaRepository extends JpaRepository<LogAuditoria, String> {
} 
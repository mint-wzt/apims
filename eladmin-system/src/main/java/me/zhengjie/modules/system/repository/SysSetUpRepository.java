package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.SysSetUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysSetUpRepository extends JpaRepository<SysSetUp, Long>, JpaSpecificationExecutor<SysSetUp> {
}

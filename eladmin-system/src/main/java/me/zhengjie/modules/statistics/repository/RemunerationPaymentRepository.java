package me.zhengjie.modules.statistics.repository;

import me.zhengjie.modules.statistics.domain.RemunerationPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RemunerationPaymentRepository extends JpaRepository<RemunerationPayment, Long>, JpaSpecificationExecutor<RemunerationPayment> {
}

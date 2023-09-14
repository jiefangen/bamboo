package org.panda.service.payment.repository;

import org.panda.service.payment.model.entity.PayOrder;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

/**
 * @author bamboo-code-generator
 */
public interface PayOrderRepo extends JpaRepositoryImplementation<PayOrder, Long> {
}

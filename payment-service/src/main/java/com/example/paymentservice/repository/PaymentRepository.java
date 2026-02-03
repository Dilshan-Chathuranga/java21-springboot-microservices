package com.example.paymentservice.repository;

import com.example.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: PaymentRepository class
 */

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
}

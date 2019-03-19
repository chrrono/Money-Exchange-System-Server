package com.money.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.exchange.entity.StaffMember;

public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {

}

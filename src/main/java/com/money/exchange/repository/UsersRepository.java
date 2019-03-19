package com.money.exchange.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.money.exchange.entity.Users;
import com.money.exchange.entity.WorkPlace;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	
	@Query("select u from Users u where u.username = :username")
	Optional<Users> findByUsername(@Param("username") String username);
	
	@Query("select w from Users u inner join u.employee as s inner join s.workPlace as w where u.username = :username")
	WorkPlace findWorkPlaceInfoAboutUser(@Param("username") String username);
	
	@Query("select u from Users u inner join u.employee as s inner join s.workPlace as w where w.name = :workPlaceName")
	List<Users> findUserNameByWorkPlace(@Param("workPlaceName") String name);
}

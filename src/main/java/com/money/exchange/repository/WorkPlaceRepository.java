package com.money.exchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.money.exchange.entity.WorkPlace;

public interface WorkPlaceRepository extends JpaRepository<WorkPlace, Long> {

	@Query("select w from WorkPlace w where w.name = :name")
	WorkPlace findWorkPlaceByName(@Param("name") String name);
	
	List<WorkPlace> findByRole(String Role);
	
	@Query("select w from WorkPlace w where w.role = 'Kantor'")
	List<WorkPlace> findWorkPlaceListByMoneyExchageOfficeRole();
	
	@Query("select w.name from WorkPlace w where w.role = 'Kantor'")
	List<String> findNamesByMoneyExchageOfficeRole();
}

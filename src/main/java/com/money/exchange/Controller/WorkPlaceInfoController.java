package com.money.exchange.Controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.money.exchange.entity.Currency;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.repository.WorkPlaceRepository;
import com.money.exchange.service.UserService;
import com.money.exchange.service.WorkPlaceService;

@RestController
@RequestMapping("/WorkPlace/info")
public class WorkPlaceInfoController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WorkPlaceService workPlaceService;
	
	@RequestMapping(method = RequestMethod.POST)
	WorkPlace getWorkPlaceByUsername(@RequestParam("user") String user) {
		return userService.findWorkPlaceInfoAboutUser(user);
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET)
	@ResponseBody
	List<WorkPlace> getListOfWorkPlace() {
		return workPlaceService.getListOfWorkPlace();
		 
	}

	
}

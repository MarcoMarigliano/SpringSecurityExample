package it.softwareinside.testSecurity.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import it.softwareinside.testSecurity.model.Role;
import it.softwareinside.testSecurity.model.User;
import it.softwareinside.testSecurity.repository.RoleRepository;
import it.softwareinside.testSecurity.repository.UserRepository;

@Controller
public class MainController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView model= new ModelAndView();
		model.setViewName("index");
		return model;
	}
	
	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView model= new ModelAndView();
		model.setViewName("login");
		return model;
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		LocalDate today
		= LocalDate.now();
		user.setRegisterDate(today);
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<>(Arrays.asList(userRole)));
		userRepository.save(user);
		return "register_success";
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userRepository.findAll();
		model.addAttribute("listUsers", listUsers);
		return "user";
	}

	@GetMapping("/admin")
	public String admin(Model model) {	     
		return "admin/admin";
	}

	@GetMapping("list/of/users")
	public ModelAndView getAllUser() {
		ModelAndView model= new ModelAndView();
		model.setViewName("list_users");
		return model;
	}
}

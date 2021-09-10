package it.softwareinside.testSecurity.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@Autowired(required=true)
    private JavaMailSender emailSender;

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
	
	/**
	 * method for send an email
	 * @param user
	 * @return
	 */
	public void sendMessage(User user) {			
		SimpleMailMessage message = new SimpleMailMessage(); 
		
		message.setFrom("noreply@baeldung.com");
		message.setTo(user.getEmail()); 
		message.setSubject("attivation code");
		message.setText("<h1>ciao</h1>");
		emailSender.send(message);
	}
	/**
	 * 
	 */
	 public void sendMail(User user) throws MessagingException {
	        javax.mail.internet.MimeMessage mimeMessage = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
	        helper.setSubject("Welcome " + user.getEmail());
	        String html = "<!doctype html>\n" +
	                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
	                "      xmlns:th=\"http://www.thymeleaf.org\">\n" +
	                "<head>\n" +
	                "    <meta charset=\"UTF-8\">\n" +
	                "    <meta name=\"viewport\"\n" +
	                "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
	                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
	                "    <title>Email</title>\n" +
	                "</head>\n" +
	                "<body>\n" +
	                "<div>Welcome <b>" + user.getEmail() + "</b></div>\n" +
	                "\n" +
	                "<a href="+"\""+"http://127.0.0.1:8080/attivazione/"+user.getAttivate_code()+"\""+">link </a>"+
	                "</body>\n" +
	                "</html>\n";
	        helper.setText(html, true);
	        helper.setTo(user.getEmail());
	        emailSender.send(mimeMessage);
	    }
	 
	 @GetMapping("/attivazione/{code}")
	 public String attivazione(@PathVariable()String code) {
		 
		 for(int i=0;i<userRepository.findAll().size();i++) {
			if(userRepository.findAll().get(i).getAttivate_code().equals(code)) {
				User newUser = userRepository.findAll().get(i);
				newUser.setEnabled(true);
				userRepository.save(newUser);
				return "goodRegistration";
			}
		 }
		 return "badRegistration"; 
	 }
	
	
	/**
	 * method that register a person and send an email
	 * @param user
	 * @return
	 * @throws MessagingException 
	 */
	@PostMapping("/process_register")
	public String processRegister(User user) throws MessagingException {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		LocalDate today
		= LocalDate.now();
		user.setRegisterDate(today);
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<>(Arrays.asList(userRole)));
		
		sendMail(user);	
		
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

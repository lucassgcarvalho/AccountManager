package br.com.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.model.User;
import br.com.security.SpringSessionBackedSessionRegistry;
import br.com.service.SecurityService;
import br.com.service.UserService;
import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private SecurityService securityService;

	private Authentication auth;
	
	@Autowired
	private FindByIndexNameSessionRepository<? extends ExpiringSession> sessions;
	
	@Autowired
	private SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry;
	
	@RequestMapping(value={"/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	
	@RequestMapping(value={"/expireSession"}, method = RequestMethod.GET)
	public ModelAndView expireSession(HttpSession session){
		ModelAndView login = login();
		User user = (User)session.getAttribute("user");
		List<SessionInformation> allSessions = springSessionBackedSessionRegistry.getAllSessions( user.getEmail() , true);
		login.getModel().put("sessions", allSessions);
		login.getModel().put("currSessionId", session.getId());
		SessionInformation sessionInformation = springSessionBackedSessionRegistry.getSessionInformation(session.getId());
		System.out.println(session.getId());
		sessionInformation.isExpired();
		return login();
	}
	
	
	   
	
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
			
		}
		securityService.autologin(user.getEmail(), user.getPassword());
		return modelAndView;
	}
	
	@RequestMapping(value={"/admin/home"}, method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		auth = SecurityContextHolder.getContext().getAuthentication();
		auth.isAuthenticated();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}
	
	
	@RequestMapping(value={"/"}, method = RequestMethod.GET)
	public ModelAndView listSessions(HttpSession session, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/index");
		auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if(user==null) {
			user = new User();
			user.setName("Teste");
			user.setEmail("Teste@email.com");
		}
        Collection<? extends ExpiringSession> userSessions = sessions.findByIndexNameAndIndexValue(PRINCIPAL_NAME_INDEX_NAME, user.getEmail()).values();
        model.addAttribute("sessions", userSessions);
        model.addAttribute("currSessionId", session.getId());
        return modelAndView;
    }
	
	
}

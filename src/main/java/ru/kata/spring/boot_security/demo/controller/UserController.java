package ru.kata.spring.boot_security.demo.controller;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/add")
	public String showAddForm(ModelMap modelMap) {
		modelMap.addAttribute("user", new User());
		return "add";
	}

	@PostMapping("/add")
	public String add(
//			Spring валидирует объект User
			@Valid User user,
//			Хранит результаты валидации
			BindingResult result,
			RedirectAttributes redirectAttributes,
			@RequestParam("name") String name,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email
	) {
//		Если есть ошибки - вернуться на форму
		if (result.hasErrors()) {
			return "add";
		}

		userService.add(new User(name, phone, email));
		redirectAttributes.addFlashAttribute("messageSuccess", String.format("Пользователь %s (телефон: %s, email: %s) успешно добавлен", name, phone, email));
		return "redirect:/";
	}

	@GetMapping("/update")
	public String showUpdateForm(
			ModelMap modelMap,
			@RequestParam("id") long id
	) {
		User user = userService.findById(id);
		modelMap.addAttribute("user", user);
		return "update";
	}

	@PostMapping("/update")
	public String update(
			RedirectAttributes redirectAttributes,
			@RequestParam("id") long id,
			@RequestParam("name") String name,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email
	) {
		userService.update(new User(id, name, phone, email));
		redirectAttributes.addFlashAttribute("messageSuccess", "Данные пользователя успешно сохранены");
		redirectAttributes.addAttribute("id", id);
		return "redirect:/update";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") long id) {
		userService.delete(id);
		return "redirect:/";
	}

	@GetMapping("/")
	public String getAll(ModelMap modelMap) {
		modelMap.addAttribute("users", userService.findAll());
		return "index";
	}
}
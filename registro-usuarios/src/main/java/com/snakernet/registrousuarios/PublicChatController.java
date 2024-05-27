package com.snakernet.registrousuarios;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicChatController {
	
	@GetMapping("/")
	public String showChatPage() {
		return "publicappchat";
	}
}

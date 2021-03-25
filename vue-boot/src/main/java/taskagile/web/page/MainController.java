package taskagile.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping(value={"/","/login","/register"})
	public String index() {
		return "index";
	}

	@GetMapping(value="tindex")
	public String thymeleafindex(){ return "thymeleafindex"; }
}

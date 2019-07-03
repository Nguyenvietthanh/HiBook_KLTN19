package ute.hibook.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ute.hibook.dto.CartDTO;
import ute.hibook.dto.ConvertPromotionDTO;
import ute.hibook.dto.PaymentDTO;
import ute.hibook.dto.PromotionDTO;
import ute.hibook.dto.RoleDTO;
import ute.hibook.dto.SupplierDTO;
import ute.hibook.dto.TransportDTO;
import ute.hibook.dto.TypebookDTO;
import ute.hibook.dto.UserDTO;
import ute.hibook.service.impl.CartServiceImpl;
import ute.hibook.service.impl.PaymentServiceImpl;
import ute.hibook.service.impl.PromotionServiceImpl;
import ute.hibook.service.impl.SupplierServiceImpl;
import ute.hibook.service.impl.TransportServiceImpl;
import ute.hibook.service.impl.TypebookServiceImpl;
import ute.hibook.service.impl.UserServiceImpl;

/* 
 * Display page .jsp when use redirect to difference page 
 * */
@Controller
public class PageController {
	@Autowired
	PromotionServiceImpl promotionServiceImpl;

	@Autowired
	UserServiceImpl userSer;
	@Autowired
	CartServiceImpl cartSer;
	@Autowired
	TransportServiceImpl tranSer;
	@Autowired
	PaymentServiceImpl paySer;
	@Autowired
	TypebookServiceImpl typeSer;
	@Autowired
	SupplierServiceImpl supplierSer;

	/* ====PAGE BOOK===== */
	@GetMapping({ "/", "/login" })
	public String homePage(ModelMap model) {	
		List<PromotionDTO> lPromotionDTOs=promotionServiceImpl.getAllPromotiondate();
		model.addAttribute("listPromotion", lPromotionDTOs);
		return "home";
	}

	@GetMapping({ "/book" })
	public String book() {
		return "listbook";
	}

	@GetMapping({ "/cart" })
	public String cartPage(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(auth.getPrincipal() instanceof User) { 
    		User us=(User)(auth.getPrincipal()); 
			 //get id from email 
			 UserDTO user = userSer.getUserByEmail(us.getUsername());
			 
			//get list cart of user current
	        List<CartDTO> carts=cartSer.getAllCartOfUser(user.getIdUser());
			 
	        model.addAttribute("listcart", carts);
			model.addAttribute("userinfo", user);
			return "cart";
    	}
    	model.addAttribute("erro", "Vui lòng đăng nhập!!!");
		return "/";
	}

	@GetMapping({ "/search" })
	public String searchPage() {
		return "listbook";
	}

	@GetMapping({ "/detail-book/{idBook}" })
	public String detailBook(@PathVariable int idBook, Model model) {
		System.out.println(idBook);
		model.addAttribute("idBook", idBook);
		return "detailbook";
	}


	@PostMapping(value="/detail-bill")
    public String getbill(@RequestParam(name="tenKhachHang") String tenKhachHang ,
    		@RequestParam(name="soDT") String soDT, @RequestParam(name="noigiaohang") int noigiaohang,  @RequestParam(name="diaChiGiao") String diaChiGiao,
    		@RequestParam(name="vanchuyen") int vanchuyen,ModelMap model)  {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(auth.getPrincipal() instanceof User) { 
    		User us=(User)(auth.getPrincipal()); 
			 //get id from email 
			 UserDTO user = userSer.getUserByEmail(us.getUsername());
			 
			//get list cart of user current
	        List<CartDTO> carts=cartSer.getAllCartOfUser(user.getIdUser());
	        Date date = new Date();
	        TransportDTO tran = tranSer.getTransportById(vanchuyen);
	        PaymentDTO pay = paySer.getPaymentById(noigiaohang);
			 
			model.addAttribute("listcart", carts); 
			model.addAttribute("idUser", user.getIdUser());
        	model.addAttribute("namereviece", tenKhachHang);
        	model.addAttribute("SDT", soDT);
        	model.addAttribute("diachi", diaChiGiao);
        	model.addAttribute("date", date);
        	model.addAttribute("vanchuyen", tran);
        	model.addAttribute("thanhtoan", pay);
			return "detailbill";
    	}
		return "/";
	}

	@PostMapping(value = "/register")
	public String registrations(@RequestParam(name = "uname") String uname, @RequestParam(name = "psw") String psw,
			@RequestParam(name = "rpsw") String rpsw, @RequestParam(name = "email") String email, ModelMap model) {

		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setIdRole(1);
		UserDTO userDTObyEmail = userSer.getUserByEmail(email);

		if (userDTObyEmail == null) {
			UserDTO userDTO = new UserDTO();
			userDTO.setEmail(email);
			String hash = BCrypt.hashpw(psw, BCrypt.gensalt(12));
			userDTO.setPassword(hash);
			userDTO.setRole(roleDTO);
			userDTO.setNameUser(uname);

			userSer.addUser(userDTO);
			model.addAttribute("erro", "Đăng ký thành công");
			return "redirect:/";
		} else {
			model.addAttribute("erro", "Email đã tồn tại!!!");
		}
		return "redirect:/register";
	}

	@GetMapping("/loginsuccess")
	public String loginsuccess() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = false, isUser = false;
		if (auth.getPrincipal() instanceof User) {
			User us = (User) (auth.getPrincipal());
			isAdmin = us.getAuthorities().stream().anyMatch(new Predicate<GrantedAuthority>() {

				public boolean test(GrantedAuthority role) {
					return role.getAuthority().equalsIgnoreCase("ROLE_ADMIN");
				}
			});
			isUser = us.getAuthorities().stream().anyMatch(new Predicate<GrantedAuthority>() {

				public boolean test(GrantedAuthority role) {
					return role.getAuthority().equalsIgnoreCase("ROLE_USER");
				}
			});
		} else {
			isAdmin = (auth.getPrincipal()).equals("ROLE_ADMIN");
			isUser = (auth.getPrincipal()).equals("ROLE_USER");
		}

		/* With each role will redirect to page */
		if (isAdmin) {
			return "redirect:/dashboard";
		}
		if (isUser) {

			return "redirect:/";

		}
		return "redirect:/";

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

	@GetMapping(value = "/403")
	public String accessDenied(Model model, Principal principal) {
		return "page403";
	}

	/* ====PAGE USER===== */
	@GetMapping({ "/user-info" })
	public String userInfo(ModelMap model) {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 if(auth.getPrincipal() instanceof User) { 
			 User us=(User)(auth.getPrincipal()); 
			 //get id from email 
			 UserDTO user = userSer.getUserByEmail(us.getUsername());
			 
			 model.addAttribute("info", user);
			 return "user/info";
		}
		return "/";
	}

	@GetMapping({ "/user-bill" })
	public String userBill(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 if(auth.getPrincipal() instanceof User) { 
			 User us=(User)(auth.getPrincipal()); 
			 //get id from email 
			 UserDTO user = userSer.getUserByEmail(us.getUsername()); 
			 
			 model.addAttribute("info", user);
			 return "user/historybill";
		}
		return "/";
	}

	@GetMapping({ "/user-update" })
	public String updateUserPage(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 if(auth.getPrincipal() instanceof User) { 
			 User us=(User)(auth.getPrincipal()); 
			 //get id from email 
			 UserDTO user = userSer.getUserByEmail(us.getUsername()); 

			 model.addAttribute("info", user);
			 return "user/updateinfo";
		}
		return "/";
	}

	/* ====PAGE ADMIN===== */

	@GetMapping({ "/add-book","/add-book/{idBook}"})
	public String addBookAdmin(ModelMap model) {
		List<TypebookDTO> type = typeSer.getAllTypebook();
		List<SupplierDTO> supplier = supplierSer.getAllSupplier();
		model.addAttribute("type", type);
		model.addAttribute("supplier", supplier);
		return "admin/addbook";
	}

	@GetMapping({ "/add-user","/add-user/{idUser}" })
	public String addUserAdmin(ModelMap model) {
		return "admin/adduser";
	}

	@GetMapping({ "/manage-author" })
	public String authorManageAdmin() {
		return "admin/author";
	}

	@GetMapping({ "/manage-bill" })
	public String billManageAdmin() {
		return "admin/bill";
	}

	@GetMapping({ "/manage-book" })
	public String manageBookAmin() {
		return "admin/book";
	}

	@GetMapping({ "/dashboard" })
	public String dashboardManageAdmin() {
		return "admin/dashboard";
	}

	@GetMapping({ "/manage-detail-bill/{idBill}" })
	public String detailBillManageAdmin() {
		return "admin/detailbill";
	}

	@GetMapping({ "/manage-orderstatus" })
	public String orderstatusManageAdmin() {
		return "admin/orderstatus";
	}

	@GetMapping({ "/manage-payment" })
	public String paymentManageAdmin() {
		return "admin/payment";
	}

	@GetMapping({ "/manage-supplier" })
	public String supplierManageAdmin() {
		return "admin/supplier";
	}

	@GetMapping({ "/manage-transport" })
	public String transportManageAdmin() {
		return "admin/transport";
	}

	@GetMapping({ "/manage-typebook" })
	public String typebookManageAdmin() {
		return "admin/typebook";
	}

	@GetMapping({ "/manage-user" })
	public String userManageAdmin() {
		return "admin/user";
	}

	@GetMapping({ "/manage-userreview" })
	public String userreviewManageAdmin() {
		return "admin/userreview";
	}
	
	@GetMapping({ "/promotion" })
	public String promotion() {
		
		
		return "admin/promotion";
	}
	@GetMapping(value = "/promotions/{idPromotion}")
	public String getPromotionbyID(@PathVariable int idPromotion,ModelMap model) {
		ConvertPromotionDTO promotionDTOs = promotionServiceImpl.getPromotionById(idPromotion);
		System.out.println(promotionDTOs.getContentPromotion());
		model.addAttribute("PromotionbyId", promotionDTOs);
		return "admin/addPromotion";
	}
	
	

}

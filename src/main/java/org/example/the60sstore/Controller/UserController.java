package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class UserController {

    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(CustomerService customerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerService = customerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/user-information")
    public String userInformation(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByUsername(userDetails.getUsername());
        model.addAttribute("user", customer);

        return "user-information";
    }

    @GetMapping("/user-information-edit")
    public String userInformationEdit() {
        return "user-information-edit";
    }

    @PostMapping("/update-user-information")
    public String processSignupForm(@RequestParam String firstName, @RequestParam String lastName,
                                    @RequestParam String birthDate,
                                    @RequestParam String email, @RequestParam String address,
                                    @RequestParam String password, Model model) {

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            model.addAttribute("error", "This email address is already in use.");
            return "user-information-edit";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        customer = customerService.getCustomerByUsername(userDetails.getUsername());
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setDateOfBirth(LocalDate.parse(birthDate).atStartOfDay());
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setPassword(hashedPassword);
        customerService.save(customer);

        return "redirect:/home?update=success";
    }
}

package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

/* UserController resolves features of account having role user. */
@Controller
public class UserController {

    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /* To execute features, it needs to create services below. */
    public UserController(CustomerService customerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerService = customerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /* toUserInformation sets url for html user-information.
    * Username is got from logging account saved at SecurityContextHolder.
    * By username, customerService can get all data of customer.
    * Add these to model and show at html. */
    @GetMapping("/user-information")
    public String toUserInformation(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null;

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            customer = customerService.getCustomerByEmail(attributes.get("email").toString());
        }
        if (authentication.getPrincipal() instanceof Customer) {
            customer = (Customer) authentication.getPrincipal();
        }

        model.addAttribute("user", customer);

        return "user-information";
    }

    /* toUserInformationEdit method sets url for user-information-edit.html. */
    @GetMapping("/user-information-edit")
    public String toUserInformationEdit() {
        return "user-information-edit";
    }

    /* processSignupForm method gets param from /user-information-edit and update data.
    * After that, redirect to home and show notification. */
    @PostMapping("/update-user-information")
    public String processSignupForm(@RequestParam String firstName, @RequestParam String lastName,
                                    @RequestParam String birthDate, @RequestParam String address,
                                    @RequestParam String password, @RequestParam String phoneNumber,
                                    Model model) {

        Customer customer = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            customer = customerService.getCustomerByEmail(attributes.get("email").toString());
        }
        if (authentication.getPrincipal() instanceof Customer) {
            customer = (Customer) authentication.getPrincipal();
        }

        customer = customerService.getCustomerByUsername(customer.getUsername());
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setDateOfBirth(LocalDate.parse(birthDate).atStartOfDay());
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        customer.setPassword(hashedPassword);
        customerService.save(customer);

        return "redirect:/home?update=success";
    }
}

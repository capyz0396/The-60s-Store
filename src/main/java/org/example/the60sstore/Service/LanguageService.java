package org.example.the60sstore.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/* LanguageService reduces time for adding attribute to model in Controller. */
@Service
public class LanguageService {

    private final LocaleResolver localeResolver;

    /* To get current language of web application, the service needs to create object of LocaleResolver. */
    public LanguageService(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    /* addLanguagle get language from request and add it to model. */
    public void addLanguagle(HttpServletRequest request, Model model) {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        model.addAttribute("lang", language);
    }
}

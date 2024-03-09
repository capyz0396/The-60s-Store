package org.example.the60sstore.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Service
public class LanguageService {

    private final LocaleResolver localeResolver;

    public LanguageService(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    public void addLanguagle(HttpServletRequest request, Model model) {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        model.addAttribute("lang", language);
    }
}

//package com.makarova.controllers;
//
//import com.makarova.dto.GoogleUserInfo;
//import com.makarova.entity.User;
//import com.makarova.service.OAuthService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/oauth")
//@RequiredArgsConstructor
//@Slf4j
//public class OAuthController {
//
//    private final OAuthService oAuthService;
//
//    @GetMapping("/google")
//    public String googleAuth() {
//        String authUrl = oAuthService.getGoogleAuthUrl();
//        log.info("Redirecting to Google auth URL: {}", authUrl);
//        return "redirect:" + authUrl;
//    }
//
//    @GetMapping("/google/callback")
//    public String googleCallback(
//            @RequestParam(required = false) String code,
//            @RequestParam(required = false) String error,
//            HttpSession session,
//            RedirectAttributes redirectAttributes) {
//
//        if (error != null) {
//            redirectAttributes.addFlashAttribute("error", "Ошибка авторизации: " + error);
//            return "redirect:/auth/login";
//        }
//
//        if (code == null) {
//            redirectAttributes.addFlashAttribute("error", "Код авторизации не получен");
//            return "redirect:/auth/login";
//        }
//
//        try {
//            var tokenResponse = oAuthService.getGoogleToken(code);
//            GoogleUserInfo userInfo = oAuthService.getGoogleUserInfo(tokenResponse.getAccess_token());
//            User user = oAuthService.getOrCreateParticipant(userInfo);
//            // Можно сохранить id пользователя в сессию, если нужно
//            session.setAttribute("userId", user.getId());
//            session.setAttribute("userRole", "user");
//            session.setAttribute("user", user.getUsername());
//            return "redirect:/main";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Ошибка при входе через Google: " + e.getMessage());
//            return "redirect:/auth/login";
//        }
//    }
//
//    // Пример страницы, где можно получить Principal после OAuth2
//    @GetMapping("/me")
//    public String me(Principal principal) {
//        // principal.getName() — email или id пользователя, в зависимости от провайдера
//        // Можно получить дополнительные атрибуты через каст к OAuth2AuthenticationToken, если нужно
//        return "me"; // имя thymeleaf-шаблона
//    }
//}

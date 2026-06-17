package com.crypto.portfolio.tracker.crypto_portfolio_tracker.controller;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.Holding;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/holding")
public class HoldingController {

    @Autowired
    private HoldingService holdingService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fetch/{userId}")
    public String fetchHoldings(@PathVariable Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
            holdingService.fetchAndSaveHolding(user);
            return "Success";
        } catch (Exception e) {
            return "Failed" + e.getMessage();
        }
    }

    @PostMapping("/manual")
    public String addOrEditHolding(@RequestBody Holding request) {
        try {
            holdingService.addOrEditHolding(request);
            return "Success";
        } catch (Exception e) {
            return "Failed" + e.getMessage();
        }
    }
}
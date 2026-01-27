package com.example.backend.service.bonus;

import com.example.backend.model.bonus.Bonus;
import com.example.backend.repository.bonus.BonusRepository;
import com.example.backend.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BonusService {
    @Autowired
    private BonusRepository bonusRepository;

    private static final int POINTS_PER_PLN = 10;

    public void addPoints(User user, Double amount) {
        if (user == null || amount == null || amount < 0) {
            return;
        }

        Bonus bonus = bonusRepository.findByUser(user)
                .orElseGet(() -> {
                    Bonus newBonus = new Bonus();
                    newBonus.setUser(user);
                    newBonus.setPoints(0);
                    return newBonus;
                });

        int pointsToAdd = (int) (amount * POINTS_PER_PLN);
        bonus.setPoints(bonus.getPoints() + pointsToAdd);

        bonusRepository.save(bonus);
    }

    public Integer getPointsBalance(User user) {
        if (user == null) {
            return 0;
        }

        Optional<Bonus> bonus = bonusRepository.findByUser(user);
        return bonus.map(Bonus::getPoints).orElse(0);
    }
}

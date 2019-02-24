package ru.guybydefault.restnetwork.planning;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import ru.guybydefault.restnetwork.entity.CookPreferences;
import ru.guybydefault.restnetwork.entity.Shift;

import java.time.temporal.ChronoUnit;

public class CookRosteringEasyScoreCalculator implements EasyScoreCalculator<CookRosteringSolution> {
    @Override
    public Score calculateScore(CookRosteringSolution solution) {
        int hardScore = 0;
        int softScore = 0;
        for (int i = 0; i < solution.getShiftList().size(); i++) {
            Shift shift = solution.getShiftList().get(i);
            if (shift.getCook() == null) {
                hardScore--;
            } else {
                CookPreferences cookPreferences = shift.getCook().getCookPreferences();
                if (!cookPreferences.isAvailableLateShifts() && shift.isLate() || !cookPreferences.isAvailableEarlyShifts() && shift.isEarly()) {
                    hardScore--;
                }
                if (!shift.getCook().hasCertification(shift.getCuisine())) {
                    hardScore--;
                }
                for (int k = i + 1; k < solution.getShiftList().size(); k++) {
                    Shift anotherShift = solution.getShiftList().get(k);
                    if (shift.getCook().equals(anotherShift.getCook())) {
                        if (ChronoUnit.DAYS.between(shift.getStartDateTime(), anotherShift.getEndDateTime()) == 0) {
                            hardScore--;
                        }
                    }
                }
            }


        }
        return HardSoftScore.of(hardScore, softScore);
    }
}

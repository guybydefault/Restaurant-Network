package ru.guybydefault.restnetwork.response;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

public class RosterBuildResultMessage extends RestControllerResponseMessage {
    public RosterBuildResultMessage(int statusCode, String message, HardSoftScore score) {
        super(statusCode, message);
    }
    // todo add violations
}

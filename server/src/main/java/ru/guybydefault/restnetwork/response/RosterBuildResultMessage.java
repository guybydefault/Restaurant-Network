package ru.guybydefault.restnetwork.response;

import ru.guybydefault.restnetwork.planning.HardSoftScore;

public class RosterBuildResultMessage extends RestControllerResponseMessage {
    public RosterBuildResultMessage(int statusCode, String message, HardSoftScore score) {
        super(statusCode, message);
    }
    // todo add violations
}

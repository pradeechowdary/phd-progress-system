package com.phdprogress.phd_progress.entity;

import java.util.EnumSet;
import java.util.Map;

public enum SubmissionStatus {
    DRAFT,
    SUBMITTED,
    ADVISOR_APPROVED,
    ADVISOR_REJECTED,
    DIRECTOR_APPROVED,
    DIRECTOR_REJECTED,
    COMPLETED;

    private static final Map<SubmissionStatus, EnumSet<SubmissionStatus>> VALID_TRANSITIONS = Map.of(
            DRAFT, EnumSet.of(SUBMITTED),
            SUBMITTED, EnumSet.of(ADVISOR_APPROVED, ADVISOR_REJECTED),
            ADVISOR_APPROVED, EnumSet.of(DIRECTOR_APPROVED, DIRECTOR_REJECTED),
            DIRECTOR_APPROVED, EnumSet.of(COMPLETED)
    );

    public boolean canTransitionTo(SubmissionStatus targetStatus) {
        return VALID_TRANSITIONS.getOrDefault(this, EnumSet.noneOf(SubmissionStatus.class)).contains(targetStatus);
    }
}

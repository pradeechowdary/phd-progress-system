package com.phdprogress.phd_progress.service;

import com.phdprogress.phd_progress.entity.ProgressSubmission;
import com.phdprogress.phd_progress.entity.User;

public interface EmailService {

    void notifyAdvisorSubmissionSubmitted(ProgressSubmission submission, User actor, User advisor);

    void notifyDirectorAdvisorApproved(ProgressSubmission submission, User actor, User director);

    void notifyStudentAdvisorRejected(ProgressSubmission submission, User actor, User student);

    void notifyStudentDirectorApproved(ProgressSubmission submission, User actor, User student);

    void notifyStudentDirectorRejected(ProgressSubmission submission, User actor, User student);
}

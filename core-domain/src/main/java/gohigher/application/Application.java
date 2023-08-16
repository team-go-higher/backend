package gohigher.application;

import gohigher.recruitment.Recruitment;

public record Application(
        Recruitment recruitment,
        int currentProcess
) {
}

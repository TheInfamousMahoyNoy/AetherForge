package com.example.aetherforged.action;

import java.util.List;

public record ActionProgram(String id, String name, String trigger, int cooldownTicks, List<ActionStep> actions) {
    public record ActionStep(String type, String particle, Double amount, Integer count) {}
}

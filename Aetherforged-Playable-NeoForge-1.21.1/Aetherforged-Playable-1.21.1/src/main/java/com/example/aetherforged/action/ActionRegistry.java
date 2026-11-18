package com.example.aetherforged.action;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.neoforged.fml.loading.FMLPaths;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public final class ActionRegistry {
    private static final Gson GSON = new Gson();
    private static final Set<String> TYPES = Set.of("builtin:bonus_damage", "builtin:ignite",
        "builtin:heal_wielder", "builtin:knockback", "builtin:lightning", "builtin:particle_burst");
    private static final Map<String, ActionProgram> PROGRAMS = new LinkedHashMap<>();
    private static final Path DIR = FMLPaths.CONFIGDIR.get().resolve("aetherforged").resolve("actions");
    private ActionRegistry() {}

    public static synchronized void reload() throws IOException {
        Files.createDirectories(DIR);
        createExamples();
        Map<String, ActionProgram> found = new LinkedHashMap<>();
        try (Stream<Path> paths = Files.list(DIR)) {
            for (Path path : paths.filter(p -> p.toString().endsWith(".json")).toList()) {
                try {
                    ActionProgram p = GSON.fromJson(Files.readString(path, StandardCharsets.UTF_8), ActionProgram.class);
                    validate(p);
                    if (found.putIfAbsent(p.id(), p) != null) throw new IllegalArgumentException("Duplicate ID " + p.id());
                } catch (JsonParseException | IllegalArgumentException ex) {
                    System.err.println("[Aetherforged] Ignoring " + path.getFileName() + ": " + ex.getMessage());
                }
            }
        }
        PROGRAMS.clear();
        PROGRAMS.putAll(found);
        System.out.println("[Aetherforged] Loaded " + PROGRAMS.size() + " action programs.");
    }

    private static void validate(ActionProgram p) {
        if (p == null || p.id() == null || !p.id().matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid namespaced ID");
        if (p.name() == null || p.name().isBlank()) throw new IllegalArgumentException("Missing name");
        if (!"on_hit".equals(p.trigger())) throw new IllegalArgumentException("Only on_hit trigger is currently supported");
        if (p.cooldownTicks() < 0 || p.cooldownTicks() > 12000) throw new IllegalArgumentException("Invalid cooldown");
        if (p.actions() == null || p.actions().isEmpty()) throw new IllegalArgumentException("No action steps");
        for (ActionProgram.ActionStep step : p.actions()) {
            if (step == null || !TYPES.contains(step.type())) throw new IllegalArgumentException("Unsupported action type");
            if (step.amount() != null && (!Double.isFinite(step.amount()) || Math.abs(step.amount()) > 1000))
                throw new IllegalArgumentException("Invalid action amount");
            if (step.count() != null && (step.count() < 0 || step.count() > 256))
                throw new IllegalArgumentException("Invalid particle count");
        }
    }

    private static void createExamples() throws IOException {
        writeIfMissing("void_echo.json", """
            {
              "id": "aetherforged:void_echo",
              "name": "Void Echo",
              "trigger": "on_hit",
              "cooldownTicks": 24,
              "actions": [
                { "type": "builtin:bonus_damage", "amount": 3.0 },
                { "type": "builtin:particle_burst", "particle": "minecraft:portal", "count": 28 },
                { "type": "builtin:knockback", "amount": 0.7 }
              ]
            }
            """);
        writeIfMissing("soul_drain.json", """
            {
              "id": "aetherforged:soul_drain",
              "name": "Soul Drain",
              "trigger": "on_hit",
              "cooldownTicks": 50,
              "actions": [
                { "type": "builtin:heal_wielder", "amount": 1.5 },
                { "type": "builtin:particle_burst", "particle": "minecraft:soul", "count": 18 }
              ]
            }
            """);
        writeIfMissing("ember_brand.json", """
            {
              "id": "aetherforged:ember_brand",
              "name": "Ember Brand",
              "trigger": "on_hit",
              "cooldownTicks": 40,
              "actions": [
                { "type": "builtin:ignite", "amount": 4.0 },
                { "type": "builtin:particle_burst", "particle": "minecraft:flame", "count": 20 }
              ]
            }
            """);
    }

    private static void writeIfMissing(String name, String value) throws IOException {
        Path path = DIR.resolve(name);
        if (Files.notExists(path)) Files.writeString(path, value, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
    }

    public static synchronized List<ActionProgram> all() { return List.copyOf(PROGRAMS.values()); }
    public static synchronized Optional<ActionProgram> get(String id) { return Optional.ofNullable(PROGRAMS.get(id)); }
}

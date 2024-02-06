package org.example.backender101homebanking.utils;

import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserIdValidator {
    public static void validateUserIds(List<Integer> userIds, List<User> users) {
        Set<Integer> validUserIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        List<Integer> invalidUserIds = userIds.stream()
                .filter(id -> !validUserIds.contains(id))
                .toList();

        if (!invalidUserIds.isEmpty()) {
            throw new ResourceNotFoundException("Gli utenti con id=" + invalidUserIds + " non sono presenti");
        }
    }
}

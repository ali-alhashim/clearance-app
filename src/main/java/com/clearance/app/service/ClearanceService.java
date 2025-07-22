package com.clearance.app.service;

import com.clearance.app.model.Clearance;
import com.clearance.app.repository.ClearanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClearanceService {
    @Autowired
    private ClearanceRepository clearanceRepository;

    public String generateNextClearanceCode() {
        String today = LocalDate.now().toString(); // e.g., 2025-07-22

        // Find all clearance codes starting with today
        List<Clearance> todayClearances = clearanceRepository.findByCodeStartingWith(today);

        int nextIndex = 1;

        if (!todayClearances.isEmpty()) {
            // Extract numbers and get max
            nextIndex = todayClearances.stream()
                    .map(c -> c.getCode().split("/"))
                    .filter(parts -> parts.length == 2)
                    .mapToInt(parts -> {
                        try {
                            return Integer.parseInt(parts[1]);
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .max()
                    .orElse(0) + 1;
        }

        return today + "/" + nextIndex;
    }
}

package com.example.ExpenseTracker;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByDateGreaterThanEqual(Date date);
    List<Expense> findByCategory(String category);
    List<Expense> findByUser(User user);

    List<Expense> findByUserAndDateGreaterThan(User user, Date dateIsGreaterThan);

    List<Expense> findByUserAndCategory(User user, String category);
}

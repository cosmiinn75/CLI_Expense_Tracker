package com.example.ExpenseTracker;


import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jdk.jfr.Category;

import java.util.Date;

@Entity
@Table(name = "expense")

public class Expense {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String description;
        private Integer amount;
        private Date date;
        private String category;


        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        public Expense() {}
        public Expense(String description, Integer amount , Date date , String category) {
            this.description = description;
            this.amount = amount;
            this.category= category;
            this.date = date;
        }
    public Expense(String description, Integer amount , Date date , String category , User user) {
        this.description = description;
        this.amount = amount;
        this.category= category;
        this.date = date;
        this.user = user;
    }

        public String getDescription(){
            return description;
        }
        public Integer getAmount(){
            return amount;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        @Override
        public String toString(){
            return "ID: " + id.toString() + " - " + date.toString() + " - " + category + " - " + description + " - " + amount.toString();

        }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

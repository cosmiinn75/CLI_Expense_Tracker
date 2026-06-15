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

        public Expense() {}
        public Expense(String description, Integer amount , Date date , String category) {
            this.description = description;
            this.amount = amount;
            this.category= category;
            this.date = date;
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
}

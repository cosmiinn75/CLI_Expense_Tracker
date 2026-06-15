package com.example.ExpenseTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@SpringBootApplication
public class ExpenseTrackerApplication implements CommandLineRunner {

	@Autowired
	ExpenseRepository expenseRepository;

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		boolean running = true;
		Scanner scanner = new Scanner(System.in);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		while (running) {
			System.out.println("Welcome to the expense tracker!");
			System.out.println("Choose your action:");
			System.out.println("1) Add expense");
			System.out.println("2) Update expense");
			System.out.println("3) Delete expense");
			System.out.println("4) View all expenses");
			System.out.println("5) View summary of all expenses");
			System.out.println("6) View summary of all expenses by date");
			System.out.println("7) View summary of all expenses by category");
			System.out.println("8) Exit program");
			System.out.print("Please enter your command: ");

			int command;
			try {
				command = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
				continue;
			}

			if (command == 1) {
				try {
					System.out.print("Please enter the date (yyyy-MM-dd): ");
					Date date = dateFormat.parse(scanner.nextLine());

					System.out.print("Please enter the category: ");
					String category = scanner.nextLine();

					System.out.print("Please enter the description: ");
					String description = scanner.nextLine();

					System.out.print("Please enter the amount: ");
					int amount = Integer.parseInt(scanner.nextLine());
					while (amount < 0) {
						System.out.print("Please enter a valid amount (>= 0): ");
						amount = Integer.parseInt(scanner.nextLine());
					}

					Expense newExpense = new Expense(description, amount, date, category);
					expenseRepository.save(newExpense);
					System.out.println("Expense successfully saved!");
				} catch (Exception e) {
					System.out.println("Error creating expense. Make sure the date format is yyyy-MM-dd and amount is a number.");
				}

				System.out.println("Press Enter to continue...");
				scanner.nextLine();

			} else if (command == 2) {
				System.out.print("Choose the ID of the expense you want to update: ");
				long id = Long.parseLong(scanner.nextLine());
				if (!expenseRepository.existsById(id)) {
					System.out.println("The expense does not exist.");
				} else {
					Expense wantedExpense = expenseRepository.findById(id).get();
					System.out.print("Enter the amount you want to change it to: ");
					int amount = Integer.parseInt(scanner.nextLine());
					wantedExpense.setAmount(amount);
					expenseRepository.save(wantedExpense);
					System.out.println("Task updated successfully!");
				}
				System.out.println("Press Enter to continue...");
				scanner.nextLine();

			} else if (command == 3) {
				System.out.print("Choose the ID of the expense you want to delete: ");
				long id = Long.parseLong(scanner.nextLine());
				if (!expenseRepository.existsById(id)) {
					System.out.println("The expense does not exist.");
				} else {
					expenseRepository.deleteById(id);
					System.out.println("Expense deleted successfully.");
				}
				System.out.println("Press Enter to continue...");
				scanner.nextLine();

			} else if (command == 4) {
				System.out.println("All Expenses");
				for (Expense expense : expenseRepository.findAll()) {
					System.out.println(expense.toString());
				}
				System.out.println("Press Enter to continue...");
				scanner.nextLine();

			} else if (command == 5) {
				int totalAmount = 0;
				for (Expense expense : expenseRepository.findAll()) {
					totalAmount += expense.getAmount();
				}
				System.out.println("Total amount: " + totalAmount);
				System.out.println("Press Enter to continue...");
				scanner.nextLine();

			} else if (command == 6) {
				try {
					System.out.print("Enter the date (yyyy-MM-dd) after which you want to see the summary: ");
					Date date = dateFormat.parse(scanner.nextLine());
					int totalAmount = 0;


					for (Expense expense : expenseRepository.findByDateGreaterThanEqual(date)) {
						totalAmount += expense.getAmount();
					}
					System.out.println("Total amount after " + dateFormat.format(date) + ": " + totalAmount);
				} catch (Exception e) {
					System.out.println("Invalid date format.");
				}
				System.out.println("Press Enter to continue...");
				scanner.nextLine();

			} else if (command == 7) {
				System.out.print("Please enter the category: ");
				String category = scanner.nextLine();
				int totalAmount = 0;


				for (Expense expense : expenseRepository.findByCategory(category)) {
					totalAmount += expense.getAmount();
				}
				System.out.println("Total amount for category '" + category + "': " + totalAmount);
				System.out.println("Press Enter to continue...");
				scanner.nextLine();

			} else if (command == 8) {
				running = false;
			} else {
				System.out.println("Enter a valid command.");
			}
		}
		System.out.println("Exited successfully.");
	}
}
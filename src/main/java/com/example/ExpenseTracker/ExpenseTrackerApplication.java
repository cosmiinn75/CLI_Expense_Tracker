	package com.example.ExpenseTracker;

	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.CommandLineRunner;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;

	import java.io.IOException;
	import java.text.SimpleDateFormat;
	import java.util.Date;
    import java.util.Optional;
    import java.util.Scanner;

	@SpringBootApplication
	public class ExpenseTrackerApplication implements CommandLineRunner {

		private static final Logger log = LoggerFactory.getLogger(ExpenseTrackerApplication.class);
		@Autowired
		ExpenseRepository expenseRepository;
		@Autowired
		UserRepository userRepository;
		public static void main(String[] args) {
			SpringApplication.run(ExpenseTrackerApplication.class, args);


		}

		@Override
		public void run(String... args) {
			Scanner scanner = new Scanner(System.in);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			User currentUser = null;
			while (true) {

				if(currentUser == null) {
				currentUser = HandleLoginSignup(scanner,userRepository);
				if(currentUser == null) {
					break;
				}
				} else {
					Boolean shouldExitProgram = HandleExpenses(scanner,userRepository,expenseRepository, dateFormat , currentUser);
					if(shouldExitProgram) {
						break;
					}
					currentUser = null;
				}

			}
			System.out.println("Exited successfully.");
		}

		private  static Boolean HandleExpenses(Scanner scanner, UserRepository userRepository, ExpenseRepository expenseRepository, SimpleDateFormat dateFormat, User currentUser) {
			while (true) {
				System.out.println("Welcome to the expense tracker " + currentUser.getUsername() + "!");
				System.out.println("Choose your action:");
				System.out.println("1) Add expense");
				System.out.println("2) Update expense");
				System.out.println("3) Delete expense");
				System.out.println("4) View all expenses");
				System.out.println("5) View summary of all expenses");
				System.out.println("6) View summary of all expenses by date");
				System.out.println("7) View summary of all expenses by category");
				System.out.println("8)Log out");
				System.out.println("9) Exit program");
				System.out.print("Please enter your command: ");

				int command = 0;
				try {
					command = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a number.");

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

						Expense newExpense = new Expense(description, amount, date, category, currentUser);
						expenseRepository.save(newExpense);
						System.out.println("Expense successfully saved!");
					} catch (Exception e) {
						System.out.println("Error creating expense. Make sure the date format is yyyy-MM-dd and amount is a number.");
					}

					System.out.println("Press Enter to continue...");
					scanner.nextLine();

				} else if (command == 2) {
				try {
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
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. ID and amount must be numbers.");
				}
				System.out.println("Press Enter to continue...");
				scanner.nextLine();
			} else if (command == 3) {
					try {
						System.out.print("Choose the ID of the expense you want to delete: ");
						long id = Long.parseLong(scanner.nextLine());
						if (!expenseRepository.existsById(id)) {
							System.out.println("The expense does not exist.");
						} else {
							expenseRepository.deleteById(id);
							System.out.println("Expense deleted successfully.");
						}
					}
					catch (NumberFormatException e) {
						System.out.println("Invalid input. ID and amount must be numbers.");
					}
					System.out.println("Press Enter to continue...");
					scanner.nextLine();

				} else if (command == 4) {
					System.out.println("All Expenses");
					for (Expense expense : expenseRepository.findByUser(currentUser)) {
						System.out.println(expense.toString());
					}
					System.out.println("Press Enter to continue...");
					scanner.nextLine();

				} else if (command == 5) {
					int totalAmount = 0;
					for (Expense expense : expenseRepository.findByUser(currentUser)) {
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


						for (Expense expense : expenseRepository.findByUserAndDateGreaterThan(currentUser, date)) {
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


					for (Expense expense : expenseRepository.findByUserAndCategory(currentUser, category)) {
						totalAmount += expense.getAmount();
					}
					System.out.println("Total amount for category '" + category + "': " + totalAmount);
					System.out.println("Press Enter to continue...");
					scanner.nextLine();

				} else if (command == 8) {

					System.out.println("Logging out...");
					System.out.println("Press Enter to continue...");
					scanner.nextLine();
					return false;
				} else if (command == 9) {
					return true;
				} else {
					System.out.println("Enter a valid command.");
				}

			}
		}

		private static User HandleLoginSignup(Scanner scanner , UserRepository userRepository) {
			while(true) {
				System.out.println("Welcome!");
				System.out.println("Choose your action:");
				System.out.println("1)Log in");
				System.out.println("2)Sign up");
				System.out.println("3)Exit program");
				Integer command;
				try {
					command = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Invalid command.Please enter a number");
					System.out.println("Press enter to continue...");
					scanner.nextLine();
					continue;
				}

				if (command == 1) {
					System.out.println("Enter your username or email:");
					String input = scanner.nextLine();
					Optional<User> userOpt = userRepository.findUserByUsername(input);
					if (!userOpt.isPresent()) {
						userOpt = userRepository.findUsersByEmail(input);
					}
					if (!userOpt.isPresent()) {
						System.out.println("Username or email does not exist");
						System.out.println("Press Enter to continue...");
						scanner.nextLine();
						continue;
					}
					User user = userOpt.get();

					System.out.println("Please enter your password:");
					String password = scanner.nextLine();

					if (user.getPassword().equals(password)) {
						System.out.println("Welcome " + user.getUsername());
						return user;
					} else {
						System.out.println("Incorrect password! Please try again.");
					}

					System.out.println("Press Enter to continue...");
					scanner.nextLine();

				} else if (command == 2) {
					System.out.println("Please enter your username: ");
					String username = scanner.nextLine();

					if (userRepository.findUserByUsername(username).isPresent()) {
						System.out.println("This username already exists.Please try again");
						continue;
					}

					System.out.println("Please enter your email: ");
					String email = scanner.nextLine();
					if (!email.contains("@")) {
						System.out.println("Please enter a valid email");
						continue;
					}
					System.out.println("Please enter your password");
					String password = scanner.nextLine();
					User user = new User(username, email, password);
					userRepository.save(user);
					System.out.println("Signing in...");
					return user;
				} else if (command == 3) {
					return null;
				} else {
					System.out.println("Please enter a valid command");
					System.out.println("Press enter to continue...");
					scanner.nextLine();
				}
			}

		}

	}
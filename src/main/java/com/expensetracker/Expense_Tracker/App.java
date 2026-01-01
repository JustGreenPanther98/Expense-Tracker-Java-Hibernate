package com.expensetracker.Expense_Tracker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import org.hibernate.SessionFactory;

import entities.Category;
import entities.Expense;
import entities.User;
import exceptions.ServiceException;
import service.CategoryService;
import service.ExpenseService;
import service.UserService;
import sessionFactory.HibernateSessionFactory;

public class App {

	private static final Scanner input = new Scanner(System.in);

	public static void main(String[] args) {

		SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
		UserService userService = new UserService(sessionFactory);
		CategoryService categoryService = new CategoryService(sessionFactory);
		ExpenseService expenseService = new ExpenseService(sessionFactory);

		try {
			String choice = "yes";

			System.out.println("-------------- WELCOME TO EXPENSE TRACKER CLI APP --------------");

			while (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("y")) {

				int authChoice = readInt("\n1) Login\n2) Register\n3) Recover Account\n4) Exit\nChoice : ");

				if (authChoice == 1) {
					handleLogin(userService, categoryService, expenseService);
				}

				else if (authChoice == 2) {
					handleRegister(userService);
				}

				else if (authChoice == 3) {
					handleRecover(userService);
				}

				else {
					break;
				}

				System.out.print("\nContinue? (yes/no): ");
				choice = input.nextLine();
			}

		} finally {
			input.close();
			sessionFactory.close();
		}
	}

	// ================= AUTH =================

	private static void handleLogin(UserService userService, CategoryService categoryService,
			ExpenseService expenseService) {

		try {
			System.out.print("Username : ");
			String username = input.nextLine();

			System.out.print("Password : ");
			String password = input.nextLine();

			User loggedIn = userService.login(username, password);

			if (loggedIn == null) {
				System.out.println("Invalid credentials.");
				return;
			}

			System.out.println("Welcome " + loggedIn.getUser_Name());

			int task = 0;
			while (task != 4) {

				task = readInt("\n1) Category\n2) Expense\n3) User Info\n4) Back\nChoice : ");

				if (task == 1) {
					handleCategoryMenu(categoryService, loggedIn);
				} else if (task == 2) {
					handleExpenseMenu(categoryService, expenseService, loggedIn);
				} else if (task == 3) {

					int userChoice = 0;
					while (userChoice != 4) {

						userChoice = readInt("\n1) Account Information\n" + "2) Change Password\n"
								+ "3) Delete / Deactivate Account\n" + "4) Back\nChoice : ");

						switch (userChoice) {

						case 1:
							System.out.println(loggedIn);
							break;

						case 2:
							try {
								System.out.print("Old password : ");
								String oldPass = input.nextLine();
								System.out.print("New password : ");
								String newPass = input.nextLine();

								if (oldPass.equals(newPass)) {
									System.out.println("New password cannot be same as old");
									break;
								}

								boolean changed = userService.changePassword(loggedIn.getUser_Name(), oldPass, newPass);

								System.out.println(changed ? "Password updated" : "Password change failed");

								if (changed) {
									loggedIn.setPassword(newPass);
								}

							} catch (ServiceException e) {
								System.out.println(e.getMessage());
							}
							break;

						case 3:
							try {
								System.out
										.println("\nWARNING: This will deactivate or permanently delete your account.");
								System.out.print("Type YES to continue: ");
								String confirm = input.nextLine();

								if (!confirm.equalsIgnoreCase("YES")) {
									System.out.println("Cancelled");
									break;
								}

								System.out.print("Enter D for deactivate OR P for permanent delete: ");
								String mode = input.nextLine();

								if (mode.equalsIgnoreCase("D")) {

									boolean deleted = userService.deleteAccount(loggedIn.getUser_Id(),
											loggedIn.getUser_Name(), loggedIn.getPassword());

									if (deleted) {
										System.out.println("Account deactivated successfully");
										return; // exits handleLogin → logout
									} else {
										System.out.println("Deactivation failed");
									}

								} else if (mode.equalsIgnoreCase("P")) {

									boolean deleted = userService.deleteAccountPermanently(loggedIn);

									if (deleted) {
										System.out.println("Account permanently deleted");
										return; // exits handleLogin → logout
									} else {
										System.out.println("Permanent deletion failed");
									}

								} else {
									System.out.println("Invalid option");
								}

							} catch (ServiceException e) {
								System.out.println(e.getMessage());
							}
							break;
						}
					}
				}

			}

		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void handleRegister(UserService userService) {
		try {
			User u = new User();

			System.out.print("Username : ");
			u.setUser_Name(input.nextLine());

			if (u.getUser_Name().contains(" ")) {
				System.out.println("Spaces not allowed");
				return;
			}

			if (userService.isUserNameExist(u.getUser_Name())) {
				System.out.println("Username already exists");
				return;
			}

			System.out.print("Password : ");
			u.setPassword(input.nextLine());
			u.setAge(readInt("Age : "));
			u.setStatus("active");

			System.out.println(userService.createAccount(u) != null ? "Account created" : "Failed");

		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void handleRecover(UserService userService) {
		try {
			User u = new User();
			u.setUser_Id(readInt("User id : "));
			System.out.print("Username : ");
			u.setUser_Name(input.nextLine());
			System.out.print("Password : ");
			u.setPassword(input.nextLine());

			System.out.println(userService.recoverAccount(u) ? "Recovered" : "Recovery failed");

		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	// ================= CATEGORY =================

	private static void handleCategoryMenu(CategoryService categoryService, User user) throws ServiceException {

		int cat = 0;
		while (cat != 6) {

			cat = readInt("\n1) Show general categories\n" + "2) Show user categories\n" + "3) Get category by id\n"
					+ "4) Create category\n" + "5) Delete category\n" + "6) Back\nChoice : ");

			switch (cat) {

			case 1:
				printCategories(categoryService.getGeneralCategories());
				break;

			case 2:
				printCategories(categoryService.getAllCategories(user));
				break;

			case 3:
				int id = readInt("Category id : ");
				Category c = categoryService.getCategoryById(id, user);
				System.out.println(c != null ? c : "Category not found");
				break;

			case 4:
				System.out.print("Category name : ");
				String name = input.nextLine().trim();
				if (name.isEmpty()) {
					System.out.println("Invalid name");
					break;
				}
				Category nc = new Category();
				nc.setCategory_Name(name);
				nc.setUser(user);
				System.out.println(categoryService.createCategory(nc) != null ? "Category created" : "Failed");
				break;

			case 5:
				int did = readInt("Category id to delete : ");
				System.out.println(categoryService.deleteCategory(did, user) ? "Deleted" : "Delete failed");
				break;
			}
		}
	}

	// ================= EXPENSE =================

	private static void handleExpenseMenu(CategoryService cs, ExpenseService es, User user) throws ServiceException {

		int exp = 0;
		while (exp != 11) {

			exp = readInt("\n1) Show all expenses\n" + "2) Get expense by id\n" + "3) Monthly expenses\n"
					+ "4) Add expense\n" + "5) Add multiple expenses\n" + "6) Update expense\n"
					+ "7) Show deleted expenses\n" + "8) Delete expense\n" + "9) Restore expense\n"
					+ "10) Delete permanently\n" + "11) Back\nChoice : ");

			switch (exp) {

			case 1:
				printExpenses(es.getAllExpenses(user));
				break;

			case 2:
				int eid = readInt("Expense id : ");
				Expense e = es.getExpense(eid, user);
				System.out.println(e != null ? e : "Not found");
				break;

			case 3:
				int y = readInt("Year : ");
				int m = readInt("Month : ");
				printExpenses(es.getListForMonth(user, y, m));
				break;

			case 4:
				addSingleExpense(cs, es, user);
				break;

			case 5:
				addMultipleExpenses(cs, es, user);
				break;

			case 6:
				int uid = readInt("Expense id : ");
				int cid = readInt("New category id : ");
				double amt = readDouble("New amount : ");

				Expense ue = new Expense();
				Category uc = new Category();
				uc.setCategory_id(cid);
				ue.setCategory(uc);
				ue.setAmount(amt);

				System.out.println(es.updateExpense(uid, ue, user) ? "Updated" : "Failed");
				break;

			case 7:
				printExpenses(es.getdeletedExpenses(user));
				break;

			case 8:
				int del = readInt("Expense id : ");
				System.out.println(es.deleteExpense(del, user) ? "Deleted" : "Failed");
				break;

			case 9:
				int rid = readInt("Expense id : ");
				System.out.println(es.restoreDeletedExpense(rid, user) != null ? "Restored" : "Failed");
				break;

			case 10:
				int pid = readInt("Expense id : ");
				System.out.println(es.deleteExpensePermatently(pid, user) ? "Deleted permanently" : "Failed");
				break;
			}
		}
	}

	// ================= HELPERS =================

	private static int readInt(String msg) {
		while (true) {
			try {
				System.out.print(msg);
				return Integer.parseInt(input.nextLine());
			} catch (Exception e) {
				System.out.println("Enter a valid number.");
			}
		}
	}

	private static double readDouble(String msg) {
		while (true) {
			try {
				System.out.print(msg);
				return Double.parseDouble(input.nextLine());
			} catch (Exception e) {
				System.out.println("Enter a valid amount.");
			}
		}
	}

	private static void addSingleExpense(CategoryService cs, ExpenseService es, User u) throws ServiceException {

		printCategories(cs.getAllCategories(u));

		int cid = readInt("Category id : ");
		Category c = cs.getCategoryById(cid, u);

		if (c == null) {
			System.out.println("Invalid category");
			return;
		}

		Expense e = new Expense();
		e.setUser(u);
		e.setCategory(c);
		e.setAmount(readDouble("Amount : "));
		e.setDate(LocalDateTime.now());
		e.setStatus("ACTIVE");

		System.out.println(es.createExpense(e) != null ? "Added" : "Failed");
	}

	private static void addMultipleExpenses(CategoryService cs, ExpenseService es, User u) throws ServiceException {

		String ch = "yes";
		while (ch.equalsIgnoreCase("yes")) {
			addSingleExpense(cs, es, u);
			System.out.print("Continue? (yes/no): ");
			ch = input.nextLine();
		}
	}

	private static void printCategories(List<Category> list) {
		if (list == null || list.isEmpty()) {
			System.out.println("No categories");
			return;
		}
		for (Category c : list) {
			System.out.println("ID: " + c.getCategory_id() + " | " + c.getCategory_Name());
		}
	}

	private static void printExpenses(List<Expense> list) {
		if (list == null || list.isEmpty()) {
			System.out.println("No expenses");
			return;
		}
		for (Expense e : list) {
			System.out.println("ID: " + e.getExpense_Id() + " | Amount: " + e.getAmount() + " | Category: "
					+ e.getCategory().getCategory_Name() + " | Date: " + e.getDate() + " | Status: " + e.getStatus());
		}
	}
}

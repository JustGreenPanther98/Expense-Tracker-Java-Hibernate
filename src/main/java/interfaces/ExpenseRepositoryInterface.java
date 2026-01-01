package interfaces;

import java.util.List;

import entities.Expense;
import entities.User;
import exceptions.DataAccessException;

public interface ExpenseRepositoryInterface {
    Expense addExpense(Expense expense) throws DataAccessException;
    int removeExpense(int expenseId, User user) throws DataAccessException;
    int removeExpensePermatently(int expenseId,User user) throws DataAccessException;
    List<Expense> getRemovedExpenses(User user) throws DataAccessException;
    Expense restoreRemovedExpense(int expenseId,User user) throws DataAccessException;
    int updateExpense(int expenseId,Expense newExpense ,User user) throws DataAccessException;
    Expense getExpenseById(int expenseId,User user) throws DataAccessException;
    List<Expense> getAllExpenses(User user) throws DataAccessException;
    List<Expense> getExpensesForMonth(User user, int year, int month) throws DataAccessException;
}


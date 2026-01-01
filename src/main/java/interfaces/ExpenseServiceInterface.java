package interfaces;

import java.util.List;
import entities.Expense;
import entities.User;
import exceptions.ServiceException;

public interface ExpenseServiceInterface {
	Expense createExpense(Expense expense) throws ServiceException;
    boolean deleteExpense(int expenseId,User user) throws ServiceException;
    boolean deleteExpensePermatently(int expenseId,User user) throws ServiceException;
    List<Expense> getdeletedExpenses(User user) throws ServiceException;
    Expense restoreDeletedExpense(int expenseId,User user) throws ServiceException;
    boolean updateExpense(int expenseId,Expense newExpense ,User user) throws ServiceException;
    Expense getExpense(int expenseId, User user) throws ServiceException;
    List<Expense> getAllExpenses(User user) throws ServiceException ;
    List<Expense> getListForMonth(User user, int year, int month) throws ServiceException;
}

package service;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import entities.Expense;
import entities.User;
import exceptions.ServiceException;
import interfaces.ExpenseServiceInterface;
import repository.ExpenseRepository;

public class ExpenseService implements ExpenseServiceInterface {

	private SessionFactory sessionFactory;

	public ExpenseService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Expense createExpense(Expense expense) throws ServiceException {
		if (expense == null || expense.getCategory() == null) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			Expense getexpense = repo.addExpense(expense);
			tx.commit();
			return getexpense;
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new ServiceException("Error in service layer while creating an expense", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean deleteExpense(int expenseId, User user) throws ServiceException {
		if (user == null || expenseId <= 0) {
			return false;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			int deleted = repo.removeExpense(expenseId, user);
			tx.commit();
			return deleted != 0;
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new ServiceException("Error in service layer while deleting an expense", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean deleteExpensePermatently(int expenseId, User user) throws ServiceException {
		if (user == null || expenseId <= 0) {
			return false;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			int deleted = repo.removeExpensePermatently(expenseId, user);
			tx.commit();
			return deleted != 0;
		} catch (Exception e) {
			if ( tx != null && tx.isActive())
				tx.rollback();
			throw new ServiceException("Error in service layer while deleting an expense permanently", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public List<Expense> getdeletedExpenses(User user) throws ServiceException {
		if (user == null) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			List<Expense> deletedExpenses = repo.getRemovedExpenses(user);
			tx.commit();
			return deletedExpenses;
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new ServiceException("Error in service layer while fetching the deleted expenses", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public Expense restoreDeletedExpense(int expenseId, User user) throws ServiceException {
		if (user == null || expenseId <= 0) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			Expense restoreDeletedExpenses = repo.restoreRemovedExpense(expenseId, user);
			tx.commit();
			return restoreDeletedExpenses;
		} catch (Exception e) {
			if ( tx != null && tx.isActive())
				tx.rollback();
			throw new ServiceException("Error in service layer while restoring the deleted expenses", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean updateExpense(int expenseId, Expense newExpense, User user) throws ServiceException {
		if (user == null || expenseId <= 0 || newExpense == null || newExpense.getCategory() == null) {
			return false;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			int updatedExpenses = repo.updateExpense(expenseId, newExpense, user);
			tx.commit();
			return updatedExpenses != 0;
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new ServiceException("Error in service layer while updating the expenses", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public Expense getExpense(int expenseId, User user) throws ServiceException {
		if (user == null || expenseId <= 0) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			Expense expense = repo.getExpenseById(expenseId, user);
			tx.commit();
			return expense;
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new ServiceException("Error in service layer while fetching the expense", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public List<Expense> getAllExpenses(User user) throws ServiceException {
		if (user == null) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			List<Expense> expense = repo.getAllExpenses(user);
			tx.commit();
			return expense;
		} catch (Exception e) {
			if ( tx != null && tx.isActive() )
				tx.rollback();
			throw new ServiceException("Error in service layer while fetching the expenses", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public List<Expense> getListForMonth(User user, int year, int month) throws ServiceException {
		if (user == null || year <= 0 || month <= 0 || month > 12) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			ExpenseRepository repo = new ExpenseRepository(session);
			List<Expense> expense = repo.getExpensesForMonth(user, year, month);
			tx.commit();
			return expense;
		} catch (Exception e) {
			if (tx != null && tx.isActive() )
				tx.rollback();
			throw new ServiceException(
					"Error in service layer while fetching the expense for perticular month and year", e);
		} finally {
			if (session != null)
				session.close();
		}
	}
}
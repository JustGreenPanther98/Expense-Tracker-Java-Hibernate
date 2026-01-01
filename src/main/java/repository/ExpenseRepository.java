package repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import entities.Expense;
import entities.User;
import exceptions.DataAccessException;
import interfaces.ExpenseRepositoryInterface;

public class ExpenseRepository implements ExpenseRepositoryInterface {
	Session session;

	public ExpenseRepository(Session session) {
		this.session = session;
	}

	@Override
	public Expense addExpense(Expense expense) throws DataAccessException {
		try {
			session.save(expense);
			return expense;
		} catch (Exception e) {
			throw new DataAccessException("Error in insertion of expense", e);
		}
	}

	@Override
	public int removeExpense(int expenseId, User user) throws DataAccessException {
		try {

			Query deleted = session.createQuery(
					"Update Expense e set e.Status='DELETED' where e.Expense_Id= :eid AND e.user.User_Id = :user AND e.Status = 'ACTIVE'"
							+ "");
			deleted.setParameter("eid", expenseId);
			deleted.setParameter("user", user.getUser_Id());
			int res = deleted.executeUpdate();
			return res;

		} catch (Exception e) {
			throw new DataAccessException("Error in deleting of expense", e);
		}
	}

	@Override
	public int updateExpense(int expenseId, Expense expense, User user) throws DataAccessException {
		try {
			Query query = session.createQuery(
				    "update Expense e " +
				    "set e.category = :category, e.Amount = :amount " +
				    "where e.user = :user " +
				    "and e.Expense_Id = :eid " +
				    "and e.Status = 'ACTIVE'"
				);
			query.setParameter("category", expense.getCategory()); // Category object
			query.setParameter("amount", expense.getAmount());
			query.setParameter("user", user);                       // User object
			query.setParameter("eid", expenseId);

			int res = query.executeUpdate();
			return res;

		} catch (Exception e) {
			throw new DataAccessException("Error in updating the expense", e);
		}
	}

	@Override
	public Expense getExpenseById(int expenseId, User user) throws DataAccessException {
		try {
			Query<Expense> query = session
					.createQuery("from Expense e where e.Expense_Id = :eid AND e.user = :user AND e.Status='ACTIVE'");
			query.setParameter("eid", expenseId);
			query.setParameter("user", user);
			Expense expense = query.uniqueResult();
			return expense;
		} catch (Exception e) {
			throw new DataAccessException("Error in fetching the expense", e);
		}
	}

	@Override
	public List<Expense> getAllExpenses(User user) throws DataAccessException {
		try {
			Query<Expense> query = session.createQuery(
					"from Expense e where (e.user = :user) AND e.Status='ACTIVE'", Expense.class);
			query.setParameter("user", user);
			List<Expense> expense = query.list();
			return expense;
		} catch (Exception e) {
			throw new DataAccessException("Error in fetching the expense", e);
		}
	}

	@Override
	public List<Expense> getExpensesForMonth(User user, int year, int month) throws DataAccessException {
		try {
			LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
			LocalDateTime end = start.plusMonths(1);

			List<Expense> expenses = session
					.createQuery("from Expense e " + "where e.user = :user " + "and e.Date >= :start "
							+ "and e.Date < :end " + "and e.Status = 'ACTIVE'", Expense.class)
					.setParameter("user", user).setParameter("start", start).setParameter("end", end).getResultList();
			return expenses;
		} catch (Exception e) {
			throw new DataAccessException("Unable to perform operation", e);
		}
	}

	@Override
	public int removeExpensePermatently(int expenseId, User user) throws DataAccessException {
		try {

			Query deleted = session.createQuery(
					"delete from Expense e where e.Expense_Id = :eid AND e.user.User_Id = :user AND e.Status = 'ACTIVE'"
							+ "");
			deleted.setParameter("eid", expenseId);
			deleted.setParameter("user", user.getUser_Id());
			int res = deleted.executeUpdate();
			return res;
		} catch (Exception e) {
			throw new DataAccessException("Error in paramently deleting of expense", e);
		}
	}

	@Override
	public List<Expense> getRemovedExpenses(User user) throws DataAccessException {
		try {
			Query<Expense> query = session.createQuery("from Expense e where e.user.User_Id = :user AND e.Status='DELETED'",
					Expense.class);
			query.setParameter("user", user.getUser_Id());
			List<Expense> expense = query.list();
			return expense;
		} catch (Exception e) {
			throw new DataAccessException("Error in fetching the expense", e);
		}
	}

	@Override
	public Expense restoreRemovedExpense(int expenseId, User user) throws DataAccessException {
		try {
			int updated = session.createQuery(
					"update Expense e set e.Status='ACTIVE' where e.Expense_Id = :eid and e.user = :user and e.Status='DELETED'")
					.setParameter("eid", expenseId).setParameter("user", user).executeUpdate();

			if (updated == 0)
				return null;

			return session.createQuery(
					"from Expense e where e.Expense_Id = :eid and e.user = :user AND e.Status = 'ACTIVE'" + "",
					Expense.class).setParameter("eid", expenseId).setParameter("user", user).uniqueResult();

		} catch (Exception e) {
			throw new DataAccessException("Error in updating the expense", e);
		}
	}
}

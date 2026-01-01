package entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Expense_Id")
	private int Expense_Id;
	
	@ManyToOne
	@JoinColumn(name = "Category_id")
	//Foregin key
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "User_Id")
	//Foregin key
	private User user;
	
	private double Amount;
	
	private LocalDateTime Date;
	
	private String Status;//'ACTIVE','DELETED'
	
	@Override
	public String toString() {
		return "Expense [Expense_Id=" + Expense_Id + ", category=" + category + ", user=" + user + ", Amount=" + Amount
				+ ", Date=" + Date + ", Status=" + Status + "]";
	}

	public int getExpense_Id() {
		return Expense_Id;
	}

	public void setExpense_Id(int expense_Id) {
		Expense_Id = expense_Id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public LocalDateTime getDate() {
		return Date;
	}

	public void setDate(LocalDateTime date) {
		Date = date;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	
}

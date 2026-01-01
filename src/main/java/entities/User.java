package entities;

import java.util.List;
import javax.persistence.*;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int User_Id;
	@Column(unique = true)
	private String User_Name;
	@Column(nullable = false)
	private String User_Password;
	@Column(name = "Age")
	private int Age;
	@Column(name = "Status")
	private String Status;

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Category> category;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Expense> expense;

	@Override
	public String toString() {
		return "User Information :\nUser Id => " + User_Id + ", User Name => " + User_Name + ", Age => " + Age +", Password length => "+ (this.User_Password.length());
	}

	public int getUser_Id() {
		return User_Id;
	}

	public void setUser_Id(int user_Id) {
		User_Id = user_Id;
	}

	public String getUser_Name() {
		return User_Name;
	}

	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public List<Category> getCategory() {
		return category;
	}

	public void setCategory(List<Category> category) {
		this.category = category;
	}

	public List<Expense> getExpense() {
		return expense;
	}

	public void setExpense(List<Expense> expense) {
		this.expense = expense;
	}

	public void setPassword(String password) {
		User_Password = password;
	}

	public String getPassword() {
		return this.User_Password;
	}

	public boolean validatePassword(String password) {
		return this.User_Password == password;
	}
}

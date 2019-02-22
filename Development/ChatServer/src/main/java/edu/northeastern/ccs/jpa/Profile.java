package edu.northeastern.ccs.jpa;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The Class Profile.
 */
@Entity
@Table(name="profile")
public class Profile {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	/** The name. */
	private String name;
	
	/** The email. */
	private String email;
	
	/** The password. */
	private String password;
	
	/** The image url. */
	private String imageUrl;
	
	/** The user. */
	@OneToOne
	private User user;

	/**
	 * Instantiates a new profile.
	 *
	 * @param id the id
	 * @param name the name
	 * @param email the email
	 * @param password the password
	 * @param imageUrl the image url
	 */
	public Profile(int id, String name, String email, String password, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.imageUrl = imageUrl;
	}
	
	/**
	 * Instantiates a new profile.
	 */
	public Profile() {
		super();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the image url.
	 *
	 * @return the image url
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Sets the image url.
	 *
	 * @param imageUrl the new image url
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
}

package services;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Reservation;
import beans.User;
import dao.ReservationDAO;
import dao.UsersDAO;
import dto.UserDTO;
import dto.UserDTOJSON;
import dto.UserLoginDTO;

@Path("/users")
public class UserService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(UserLoginDTO user) {
		UsersDAO allUsersDAO = getUsers();

		User userForLogin = allUsersDAO.getUserByUsername(user.username);

		if (userForLogin == null) {
			System.out.println("Nema takvog usera");
			return Response.status(Response.Status.BAD_REQUEST).entity("Password or username are incorrect, try again")
					.build();
		}	
		
		
		
		if (!userForLogin.getPassword().equals(user.password)) {
			System.out.println("SIFRE NISU JEDNAKE");
			return Response.status(Response.Status.BAD_REQUEST).entity("Password or username are incorrect, try again")
					.build();
		}
		
		if(allUsersDAO.isBlocked(user.username)) {
			System.out.println("blokiran je");
			return Response.status(Response.Status.BAD_REQUEST).entity("You are blocked from this application!")
					.build();
		}

		request.getSession().setAttribute("loginUser", userForLogin); // we give him a session

		// We know this, because in users we have 3 types of instances[Administrator,
		// Guest, Host]
		if (userForLogin.getRole().equals("ADMINISTRATOR")) {
			return Response.status(Response.Status.ACCEPTED).entity("/Apartments/administratorDashboard.html").build();

		} else if (userForLogin.getRole().equals("GUEST")) {
			return Response.status(Response.Status.ACCEPTED).entity("/Apartments/guestDashboard.html").build();

		} else if (userForLogin.getRole().equals("HOST")) {
			return Response.status(Response.Status.ACCEPTED).entity("/Apartments/hostDashboard.html").build();

		}

		return Response.status(Response.Status.ACCEPTED).entity("/Apartments/#/loginaaa").build(); // redirect to login
																									// when is login
																									// accepted
		// return Response.ok().build();

	}

	@POST
	@Path("/blockUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> blockUser(UserDTOJSON param){
		System.out.println("blokiram usera: " + param.user.getUserName() + " ID: " + param.user.getID());
		
		UsersDAO allUsersDAO = getUsers();
		allUsersDAO.blockUserById(param.user.getID());
		
		return getUsers().getValues();
	}
	
	@POST
	@Path("/unblockUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> unblockUser(UserDTOJSON param){
		System.out.println("blokiram usera: " + param.user.getUserName() + " ID: " + param.user.getID());
		
		UsersDAO allUsersDAO = getUsers();
		allUsersDAO.unblockUserById(param.user.getID());
		
		return getUsers().getValues();
	}
	
	@POST
	@Path("/registration")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registration(UserDTO user) {
		System.out.println("DODAJEM USERA" + user.username + "\nSa sifrom: " + user.password + "OVDE ZAPRAVO");
		System.out.println("Imena: " + user.name + "\nPrezimena: " + user.surname);

		UsersDAO allUsersDAO = getUsers();

		/* If we have already that user, we can't register him */
		if (allUsersDAO.getUserByUsername(user.username) != null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("We have alredy user with same username. Please try another one").build();
		}

		
		allUsersDAO.addNewUser(user);

		System.out.println("\n\n\t\t USPESNO \n\n");
		return Response.status(Response.Status.ACCEPTED).entity("/Apartments/#/login").build(); // redirect to login
																								// when is registration
																								// accepted
	}

	@GET
	@Path("/getNewUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getNewUser() {
		User user = new User();
		user.setID(9999);
		user.setUserName("Novi userName");
		user.setPassword("Novi pass");
		return user;

		// TODO: promeniti da daje pametniji id useru
	}

	@GET
	@Path("/getJustUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getJustUsers() {
		System.out.println("CALLED GET JUST USERS");
		return getUsers().getValues();
	}

	@GET
	@Path("/getGuestsOfHost")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getGuestsOfHost() {
		System.out.println("\n\n\t\t get guests of host");

		// With this, we get user who is logged in.
		// We are in UserService method login() tie user for session.
		// And now we can get him.
		User user = (User) request.getSession().getAttribute("loginUser");
		UsersDAO allUsersDAO = getUsers();
		
		ReservationDAO reservationDAO = getReservations();
		
		return allUsersDAO.getGuestsOfHost(user, reservationDAO.getValues());
	}
	
	@GET
	@Path("/getReservationsOfHost")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getReservationsOfHost() {
		System.out.println("\n\n\t\t get reservations of host");

		// With this, we get user who is logged in.
		// We are in UserService method login() tie user for session.
		// And now we can get him.
		User user = (User) request.getSession().getAttribute("loginUser");
		UsersDAO allUsersDAO = getUsers();
		
		ReservationDAO reservationDAO = getReservations();
		
		return allUsersDAO.getReservationsOfHost(user, reservationDAO.getValues());
	}
	
	

	private UsersDAO getUsers() {
		UsersDAO users = (UsersDAO) ctx.getAttribute("users");
		if (users == null) {
			users = new UsersDAO();
			users.readUsers();
			ctx.setAttribute("users", users);

		}

		return users;
	}
	
	private ReservationDAO getReservations() {
		ReservationDAO reservations = (ReservationDAO) ctx.getAttribute("reservations");

		if (reservations == null) {
			reservations = new ReservationDAO();
			reservations.readReservations();
			ctx.setAttribute("reservations", reservations);
		}
		return reservations;
	}

}

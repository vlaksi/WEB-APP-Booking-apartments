package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Address;
import beans.Apartment;
import beans.Location;
import beans.User;
import dto.ApartmentChangeDTO;
import dto.ApartmentDTOJSON;
import dto.ApartmentsDTO;

public class ApartmentsDAO {

	private ArrayList<Apartment> apartments;
	private String path;

	public ApartmentsDAO() {
		File podaciDir = new File(System.getProperty("catalina.base") + File.separator + "podaci");
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.path = System.getProperty("catalina.base") + File.separator + "podaci" + File.separator
				+ "apartments.json";
		this.apartments = new ArrayList<Apartment>();

		// UNCOMENT IF WANT TO ADD DUMMY DATA TO FILE addMockupData();

		System.out.println(this.path);
	}

	/**
	 * Read the apartments from the file.
	 */
	public void readApartments() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.path);

		List<Apartment> loadedApartments = new ArrayList<Apartment>();
		try {

			loadedApartments = objectMapper.readValue(file, new TypeReference<List<Apartment>>() {
			});

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("\n\n ucitavam preko object mapera \n\n");
		for (Apartment a : loadedApartments) {
			System.out.println("ID APARTMANA: " + a.getID());
			apartments.add(a);
		}
		System.out.println("\n\n");
	}

	public void saveApartmentsJSON() {

		// Get all apartments
		List<Apartment> allApartments = new ArrayList<Apartment>();
		for (Apartment a : getValues()) {
			allApartments.add(a);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Write them to the file
			objectMapper.writeValue(new FileOutputStream(this.path), allApartments);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Izmena podataka prosledjenog apartmana.
	 */
	public Boolean changeApartment(ApartmentChangeDTO updatedApartment) {

		for (Apartment apartment : apartments) {
			if (apartment.getID().equals(updatedApartment.identificator)) {
				System.out.println(
						"NASAO SAM APARTMAN " + updatedApartment.identificator + " i sad cu mu izmeniti podatke");
				apartment.setPricePerNight(updatedApartment.pricePerNight);
				apartment.setTimeForCheckIn(updatedApartment.timeForCheckIn);
				apartment.setTimeForCheckOut(updatedApartment.timeForCheckOut);
				apartment.setNumberOfRooms(updatedApartment.numberOfRooms);
				apartment.setNumberOfGuests(updatedApartment.numberOfGuests);
				saveApartmentsJSON();
				return true;
			}
		}

		return false;
	}

	public void activateApartment(long identificator) {
		for (Apartment apartment : apartments) {
			if (apartment.getID().equals(identificator)) {
				apartment.setStatus("ACTIVE");
				saveApartmentsJSON();
				return;
			}
		}

	}

	public ArrayList<Apartment> getValues() {
		return apartments;
	}

	public void deleteApartment(int identificator) {

		for (Apartment apartment : apartments) {
			if (apartment.getID().equals(identificator)) {
				apartments.remove(apartment);
				saveApartmentsJSON();
				return;
			}
		}
		return;

	}

	public void addNewApartments(ApartmentDTOJSON newItem) {

		Apartment apartment = new Apartment();
		apartment.setID(newItem.addedApartment.getID());

		apartment.setStatus(newItem.addedApartment.getStatus());
		apartment.setTypeOfApartment(newItem.addedApartment.getTypeOfApartment());
		apartment.setPricePerNight(newItem.addedApartment.getPricePerNight());
		apartment.setTimeForCheckIn(newItem.addedApartment.getTimeForCheckIn());
		apartment.setTimeForCheckOut(newItem.addedApartment.getTimeForCheckOut());
		apartment.setNumberOfRooms(newItem.addedApartment.getNumberOfRooms());
		apartment.setNumberOfGuests(newItem.addedApartment.getNumberOfGuests());
		apartment.setLocation(newItem.addedApartment.getLocation());

		apartments.add(apartment);
		saveApartmentsJSON();
	}

	public Collection<Apartment> getHostApartments(User user) {

		List<Apartment> hostApartments = new ArrayList<Apartment>();
		
		for(int id : user.getApartmentsForRentingHostIDs()) {
			hostApartments.add(getApartmentById(id));
		}
		
		
		return hostApartments;
	}

	public Apartment getApartmentById(Integer id) {
		for(Apartment ap: apartments) {
			if(ap.getID().equals(id.intValue())) {
				return ap;
			}
		}
		return null;
	}
	
	/**
	 * Method for adding dummy data to JSON file of apartments
	 */
	private void addMockupData() {
		// Make list for writing
		List<Apartment> allApartments = new ArrayList<Apartment>();

		Long ID = 1l;
		int logicalDeleted = 0; // 1 - deleted, 0 - not deleted
		String typeOfApartment = "STANDARD"; // it can be STANDARD or ROOM
		Long numberOfRooms = 11l;
		Long numberOfGuests = 5l;
		Location location = new Location("41", "42", new Address("Danila Kisa", "33", "Novi Sad", "21000"));

		ArrayList<String> datesForHosting = new ArrayList<String>();
		datesForHosting.add("12-09-2020 do 22-10-2020");

		Integer hostID = 1;
		ArrayList<Integer> apartmentCommentsIDs = new ArrayList<Integer>();
		apartmentCommentsIDs.add(1);
		apartmentCommentsIDs.add(2);

		String images = "empty";

		Long pricePerNight = 10l;

		String timeForCheckIn = "17:00";
		String timeForCheckOut = "11:00";
		String status = "ACTIVE";

		ArrayList<Integer> apartmentAmentitiesIDs = new ArrayList<Integer>();
		apartmentAmentitiesIDs.add(1);
		apartmentAmentitiesIDs.add(2);

		ArrayList<String> listOfReservationsIDs = new ArrayList<String>();
		listOfReservationsIDs.add("1");
		listOfReservationsIDs.add("2");

		Apartment a1 = new Apartment(ID, logicalDeleted, typeOfApartment, numberOfRooms, numberOfGuests, location,
				datesForHosting, hostID, apartmentCommentsIDs, images, pricePerNight, timeForCheckIn, timeForCheckOut,
				status, apartmentAmentitiesIDs, listOfReservationsIDs);
		Apartment a2 = new Apartment(2l, logicalDeleted, typeOfApartment, numberOfRooms, numberOfGuests, location,
				datesForHosting, hostID, apartmentCommentsIDs, images, pricePerNight, timeForCheckIn, timeForCheckOut,
				status, apartmentAmentitiesIDs, listOfReservationsIDs);

		allApartments.add(a1);
		allApartments.add(a2);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Write them to the file
			objectMapper.writeValue(new FileOutputStream(this.path), allApartments);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

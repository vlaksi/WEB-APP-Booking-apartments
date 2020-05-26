package beans;

public class Comment {
	private Guest guestCommentAuthor; // Gost koji je ostavio komentar
	private Apartment apartmentComment; // Apartman na koji se odnosi komentar
	private String txtOfComment;
	private String ratingForApartment;

	public Comment(Guest guestCommentAuthor, Apartment apartmentComment, String txtOfComment,
			String ratingForApartment) {
		super();
		this.guestCommentAuthor = guestCommentAuthor;
		this.apartmentComment = apartmentComment;
		this.txtOfComment = txtOfComment;
		this.ratingForApartment = ratingForApartment;
	}

	public Guest getGuestCommentAuthor() {
		return guestCommentAuthor;
	}

	public void setGuestCommentAuthor(Guest guestCommentAuthor) {
		this.guestCommentAuthor = guestCommentAuthor;
	}

	public Apartment getApartmentComment() {
		return apartmentComment;
	}

	public void setApartmentComment(Apartment apartmentComment) {
		this.apartmentComment = apartmentComment;
	}

	public String getTxtOfComment() {
		return txtOfComment;
	}

	public void setTxtOfComment(String txtOfComment) {
		this.txtOfComment = txtOfComment;
	}

	public String getRatingForApartment() {
		return ratingForApartment;
	}

	public void setRatingForApartment(String ratingForApartment) {
		this.ratingForApartment = ratingForApartment;
	}
}

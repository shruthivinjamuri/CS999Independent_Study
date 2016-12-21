package AcquireGame;


@SuppressWarnings("serial")
public class GameException extends Exception{

	private String Message;
	private int Id;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
	public GameException(String Message){
		this.Message = Message;
	}
	
}
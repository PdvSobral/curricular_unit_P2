public class teste {
	private int number;
	private String name;

	public void setNumber(int newNumber){
		this.number = newNumber;
	}

	public int getNumber(){
		return this.number;
	}

	public void setName(String newName){
		this.name = newName;
	}

	public String getName(){
		return this.name;
	}

	public void Number(int newNumber){
		this.number=newNumber;
	}

	public int Number(){
		return this.number;
	}
}

@SuppressWarnings("CommentedOutCode")
class teste2 extends teste {
	public teste2(){
		super();
	}

	/*
	private <T> void setProperty(String name, T value) {
		if (name.equals("brilho")) {
			this.__brilho = convertValue(value);
		} else if (name.equals("cor")) {
			this.__cor = convertValue(value);
		}
	}

	private Object convertValue(Object value) {
		// Conversion logic here
		return value;
	}
	*/
}

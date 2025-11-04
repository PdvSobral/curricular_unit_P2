public class teste {
	private int number;
	private String name;

	public void setNumber(int newNumber){
		this.number = newNumber;
		return;
	}

	public int getNumber(){
		return this.number;
	}

	public void setName(String newName){
		this.name = newName;
		return;
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

class teste2 extends teste {
	public teste2(){
		super();
	}
}

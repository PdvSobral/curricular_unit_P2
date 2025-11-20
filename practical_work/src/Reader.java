import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader extends BufferedReader {
	private Runnable handler = null;

	public Reader(InputStream in, int buffer_size){
		super(new InputStreamReader(in), buffer_size);
	}
	public Reader(InputStream in){
		super(new InputStreamReader(in));
	}

	public void setHandler(Runnable handler) {
		this.handler = handler;
	}

	@Override
	public String readLine() throws IOException{
		String temp = super.readLine();
		if (temp == null && this.handler != null) {
			this.handler.run();
		}
		return temp;
	}
}

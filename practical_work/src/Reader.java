import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader extends BufferedReader {
	public Reader(InputStream in, int sz){
		super(new InputStreamReader(in), sz);
	}
	public Reader(InputStream in){
		super(new InputStreamReader(in));
	}


}

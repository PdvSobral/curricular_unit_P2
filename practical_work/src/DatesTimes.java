import java.util.Calendar;

public class DatesTimes {
	// DatesTimes.getInstance().getYear()
	private static final DatesTimes __instance = new DatesTimes();

	private DatesTimes() {}	// Constructor logic

	public static DatesTimes getInstance() {
		return __instance;
	}

	public int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
}

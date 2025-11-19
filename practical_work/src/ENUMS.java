public class ENUMS {
	public enum MACHINE_STATE {
		AVAILABLE,		// The machine is free for play
		IN_USE,			// The machine is currently being played
		OUT_OF_ORDER,	// The machine is not operational
		MAINTENANCE,	// The machine is undergoing repairs or servicing
		COMING_SOON,	// The machine is set to be available shortly
		OUT_OF_TICKETS	// The machine has run out of tokens and can't be played
	}
}

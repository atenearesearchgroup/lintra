package transfo;

public class LinTraParameters {

	/** Configurable parameters */
	public static final int MODEL_CHUNK_SIZE = 50000;
	public static final int JOB_SIZE = 500;
	public static final int NUMBER_OF_THREADS_T1 = 1; //Runtime.getRuntime().availableProcessors();
	public static final int NUMBER_OF_THREADS_T2 = 1;
	public static final boolean T1_AND_THEN_T2 = false; // the opposite is T1_T2_IN_PARALELL
	
	/** LinTra internal parameters */
	public static final String TODO_ID = "1.0";
	public static final String TODO_FLAGS_ID = "0.0";
	public static final String MODEL_FLAGS_ID = "0.0";
	public static final String CURRENT_AREA_ID = "0.0";
//	public static final int RANGE_OF_IDS_WIDTH = 50;
	
}

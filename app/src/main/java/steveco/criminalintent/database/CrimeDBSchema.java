package steveco.criminalintent.database;

/**
 * Created by sretu on 4/11/2016.
 */
public class CrimeDBSchema {
    //used to define string constants in the database
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}

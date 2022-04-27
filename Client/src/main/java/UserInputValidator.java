public class UserInputValidator {
    private static final String CNP_FORMAT = "^[0-9]{13}$";
    private static final String PHONE_FORMAT = "^[0-9]{10}$";
    private static final String EMAIL_FORMAT = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
    private static final String YEAR_FORMAT = "^[1,2][0-9]{3}$";

    public static void validateLibraryUserInfo(String cnp, String name, String address, String phone, String email) throws Exception{
        String message = "";
        if(!cnp.matches(CNP_FORMAT))
            message += "Invalid cnp!\n";
        if(name.equals(""))
            message += "Invalid name!\n";
        if(address.equals(""))
            message += "Invalid address!\n";
        if(!phone.matches(PHONE_FORMAT))
            message += "Invalid phone number!\n";
        if(!email.matches(EMAIL_FORMAT))
            message += "Invalid email!\n";
        if(message.length() != 0)
            throw new Exception(message);
    }

    public static void validateBookInfo(String ISBN, String title, String author, String genre, String publishYear) throws Exception {
        String message = "";
        if(ISBN.equals(""))
            message += "Invalid ISBN!\n";
        if(title.equals(""))
            message += "Invalid title!\n";
        if(author.equals(""))
            message += "Invalid author!\n";
        if(genre.equals(""))
            message += "Invalid genre!\n";
        if(!publishYear.matches(YEAR_FORMAT))
            message += "Invalid year!\n";
        if(message.length() != 0)
            throw new Exception(message);
    }
}

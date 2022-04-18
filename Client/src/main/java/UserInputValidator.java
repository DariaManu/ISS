public class UserInputValidator {
    private static final String CNP_FORMAT = "^[0-9]{13}$";
    private static final String PHONE_FORMAT = "^[0-9]{10}$";
    private static final String EMAIL_FORMAT = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";

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
}

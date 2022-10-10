package company.name;

import company.name.ui.Application;
import company.name.ui.ApplicationConsoleImpl;

public class Main {

    public static void main(String[] args) {
        Application application = new ApplicationConsoleImpl();
        application.run();
    }

}

import application.ApplicationExecuter;
import exception.UIExceptionWrapper;

public class Start {

    public static void main(String[] args) {
        UIExceptionWrapper startWrapper = new UIExceptionWrapper(() -> new ApplicationExecuter().execute());
        startWrapper.execute();
    }
}

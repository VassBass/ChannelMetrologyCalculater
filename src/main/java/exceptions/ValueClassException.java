package exceptions;

public class ValueClassException extends RuntimeException {

    public ValueClassException(Object value){
        super("Запрвшиваемый обьект не является обьектом класса "
                + "\""
                + value.getClass()
                + "\"");
    }
}

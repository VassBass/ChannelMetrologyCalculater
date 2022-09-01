package service;

import model.Person;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.Collection;
import java.util.Optional;

public interface PersonService {
    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getAllNamesWithFirstEmptyString();

    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getNamesOfHeadsWithFirstEmptyString();
}

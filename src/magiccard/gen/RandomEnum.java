/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccard.gen;

import java.util.Random;

/**
 *
 * @author hungcuong
 */
public class RandomEnum<E extends Enum> {

    private static final Random RND = new Random();
    private final E[] values;

    public RandomEnum(Class<E> token) {
        values = token.getEnumConstants();
    }

    public E random() {
        return values[RND.nextInt(values.length)];
    }
}

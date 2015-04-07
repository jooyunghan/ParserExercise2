package fp;

import org.junit.Test;

import static fp.Option.some;
import static org.junit.Assert.assertSame;

/**
 * Created by jooyung.han on 4/7/15.
 */
public class OptionTest {
    static class Animal {

    }
    static class Cat extends Animal {

    }
    static class MyCat extends Cat {

    }
    @Test
    public void testOrElse() throws Exception {
        Cat theCat = new Cat();
        Cat myCat = new MyCat();
        Option<Cat> option1 = some(theCat).orElse(() -> some(myCat));
        assertSame(theCat, option1.get());
        Option<Cat> option2 = Option.<Cat>none().orElse(() -> some(myCat));
        assertSame(myCat, option2.get());
    }




}
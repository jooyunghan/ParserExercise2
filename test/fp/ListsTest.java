package fp;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static fp.Lists.map;
import static org.junit.Assert.assertEquals;

/**
 * Created by jooyung.han on 4/7/15.
 */
public class ListsTest {
    static class Animal {
        AnimalFood getFood() {
            return null;
        }
    }
    static class Cat extends Animal {
        @Override
        CatFood getFood() {
            return null;
        }
    }
    static class AnimalFood {
    }

    static class CatFood extends AnimalFood {
    }

    @Test
    public void testMap() throws Exception {
        List<Cat> cats = Arrays.asList(new Cat(), new Cat());
        List<CatFood> map = map(Cat::getFood, cats);
        assertEquals(2, map.size());
    }
}
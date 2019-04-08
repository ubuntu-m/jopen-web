package io.jopen.web.json;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author maxuefeng
 */
public class FastJsonTest {

    class People implements Serializable, Cloneable {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "People{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @Test
    public void testSimpleAPI(){

        People people = new People();

        people.setName("Jack");

        Object json = JSON.toJSON(people);

        System.err.println(json);
    }
}

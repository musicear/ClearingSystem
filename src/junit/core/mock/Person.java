package junit.core.mock;

import java.util.Collection;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-4-11
 * Time: 23:50:28
 */
public class Person {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Collection getChildren() {
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    private String name;
    private int age;
    private Collection children;
}

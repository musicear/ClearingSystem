package resoft.tips.domain;

/**
 * <p>ÃûÖµ¶Ô</p>
 * Author: liguoyin
 * Date: 2007-7-9
 * Time: 19:45:29
 */
public class KeyValuePair {

    public KeyValuePair() {
    }

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String key;
    private String value;
}

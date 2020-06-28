package resoft.basLink;

/**
 * function: 通讯数据
 * User: albert lee
 * Date: 2005-9-27
 * Time: 16:02:21
 */
public class CommData {
    public CommData(Context context,byte[] data) {
        this.context = context;
        this.data = data;
    }

    public Context getContext() {
        return context;
    }
    public byte[] getData() {
        return data;
    }
    private Context context;
    private byte[] data;
}

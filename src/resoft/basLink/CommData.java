package resoft.basLink;

/**
 * function: ͨѶ����
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

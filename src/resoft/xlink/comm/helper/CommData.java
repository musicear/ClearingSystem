package resoft.xlink.comm.helper;

/**
 * <p>ͨѶ���ݣ������价��</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 22:21:41
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

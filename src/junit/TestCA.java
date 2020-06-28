package junit;

import resoft.tips.util.Signer;


/**
 * Created by IntelliJ IDEA.
 * User: liguoyin
 * Date: 2007-5-29
 * Time: 21:32:09
 * To change this template use File | Settings | File Templates.
 */
public class TestCA {
    public static void main(String[] args) throws Exception{
        Signer signer = Signer.getInstance();
        System.out.println(signer.detachSign("<CFX>fuck</CFX>"));
    }
}

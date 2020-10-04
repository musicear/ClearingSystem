package org.zz.gmhelper;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class GMBaseUtil {
    static {
    	if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
    		System.out.println("security provider BC not found");
    		Security.addProvider(new BouncyCastleProvider());
    	}
    }
}

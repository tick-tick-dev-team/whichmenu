package com.ticktick.whichmenu_backend.config;

import org.apache.ibatis.logging.stdout.StdOutImpl;

public class WhichmenuCustomLogger extends StdOutImpl {

	public WhichmenuCustomLogger(String clazz) {
		super(clazz);
	}
	
	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
    public void debug(String msg) {
        if (msg.contains("Total:") || msg.contains("Preparing:")) {
            super.debug(msg);  // "Total:"이 포함된 메시지만 출력
        }
    }

}

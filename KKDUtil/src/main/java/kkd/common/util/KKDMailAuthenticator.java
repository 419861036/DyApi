package kkd.common.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class KKDMailAuthenticator extends Authenticator {
	String password = null;
	String userName = null;

	public KKDMailAuthenticator() {
	}

	public KKDMailAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName.split("@")[0], password);
	}

}

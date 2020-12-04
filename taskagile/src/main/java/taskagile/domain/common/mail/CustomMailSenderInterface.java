package taskagile.domain.common.mail;

import taskagile.domain.model.user.User;

public interface CustomMailSenderInterface {
	public void send(User user, String templateName, MessageVariable... messageVariable);
}

package io.onedev.server.model.support.pullrequest.changedata;

import io.onedev.server.OneDev;
import io.onedev.server.entitymanager.UserManager;
import io.onedev.server.model.User;

public class PullRequestAssigneeAddData extends PullRequestChangeData {

	private static final long serialVersionUID = 1L;

	private final Long assigneeId;
	
	public PullRequestAssigneeAddData(User assignee) {
		this.assigneeId = assignee.getId();
	}
	
	@Override
	public String getActivity() {
		User user = OneDev.getInstance(UserManager.class).get(assigneeId);
		if (user != null)
			return "added assignee \"" + user.getDisplayName() + "\"";
		else
			return "added assignee unknown";
	}

}

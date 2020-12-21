package taskagile.domain.application;

import taskagile.domain.model.activity.Activity;

public interface ActivityService {

  /**
   * Save an activity
   *
   * @param activity the activity instance
   */
  void saveActivity(Activity activity);
}

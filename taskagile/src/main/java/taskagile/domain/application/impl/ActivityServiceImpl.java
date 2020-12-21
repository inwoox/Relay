package taskagile.domain.application.impl;

import taskagile.domain.application.ActivityService;
import taskagile.domain.model.activity.Activity;
import taskagile.domain.model.activity.ActivityRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

  private ActivityRepository activityRepository;

  public ActivityServiceImpl(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  @Override
  public void saveActivity(Activity activity) {
    activityRepository.save(activity);
  }
}

package taskagile.web.result;

import taskagile.domain.model.user.User;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class MyDataResult {

  public static ResponseEntity<ApiResult> build(User user) {

    Map<String, Object> userData = new HashMap<>();
    userData.put("name", user.getFirstName() + " " + user.getLastName());

    ApiResult apiResult = ApiResult.blank()
      .add("user", userData);

    return Result.ok(apiResult);
  }

}

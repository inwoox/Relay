package taskagile.domain.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


// 무엇이 활성화된 파일 저장소인지 확인하고, 그에 맞는 FileStorage 인스턴스를 반환하는 책임을 가진다.

@Component
public class FileStorageResolver {

  private String activeStorageName;
  private ApplicationContext applicationContext;

  public FileStorageResolver(@Value("${app.file-storage.active}") String activeStorageName,
                             ApplicationContext applicationContext) {
    this.activeStorageName = activeStorageName;
    this.applicationContext = applicationContext;
  }

  /**
   * Resolve the file storage should be used based on
   * active file storage configuration in application.properties
   *
   * @return the active file storage instance
   */
  public FileStorage resolve() {
    return applicationContext.getBean(activeStorageName, FileStorage.class);
  }

}

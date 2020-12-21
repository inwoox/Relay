package taskagile.domain.common.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

  TempFile saveAsTempFile(String folder, MultipartFile file);   // MultipartFile 인스턴스를 application.properties에 명시된 서버의 temp 폴더에 저장
  void saveTempFile(TempFile tempFile);                         // 임시 파일을 최종 목적지에 저장하는데 활용
  String saveUploaded(String folder, MultipartFile file);       // 업로드된 MultipartFile을 최종 목적지에 저장하는데 활용
}

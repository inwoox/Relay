package taskagile.infrastructure.file.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import taskagile.domain.common.file.AbstractBaseFileStorage;
import taskagile.domain.common.file.FileStorageException;
import taskagile.domain.common.file.TempFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

// FileStorage 인터페이스를 구현하는 AbstractBaseFileStorage를 상속하는 FileStorage 구현체 클래스
@Component("s3FileStorage")
public class S3FileStorage extends AbstractBaseFileStorage {

  private static final Logger log = LoggerFactory.getLogger(S3FileStorage.class);

  private Environment environment;
  private String rootTempPath;
  private AmazonS3 s3;

  public S3FileStorage(Environment environment,
                       @Value("${app.file-storage.temp-folder}") String rootTempPath) {
    this.environment = environment;
    this.rootTempPath = rootTempPath;
    if ("s3FileStorage".equals(environment.getProperty("app.file-storage.active"))) {
      this.s3 = initS3Client();
    }
  }

  // MultipartFile 인스턴스를 application.properties에 명시된 서버의 temp 폴더에 저장
  @Override
  public TempFile saveAsTempFile(String folder, MultipartFile multipartFile) {
    return saveMultipartFileToLocalTempFolder(rootTempPath, folder, multipartFile);
  }

  // 임시 파일을 최종 목적지에 저장하는데 활용
  @Override
  public void saveTempFile(TempFile tempFile) {
    Assert.notNull(s3, "S3FileStorage must be initialized properly");

    String fileKey = tempFile.getFileRelativePath();
    String bucketName = environment.getProperty("app.file-storage.s3-bucket-name");
    Assert.hasText(bucketName, "Property `app.file-storage.s3-bucket-name` must not be blank");

    try {
      log.debug("Saving file `{}` to s3", tempFile.getFile().getName());
      
      // 이 메서드에서 파일의 상대 경로를 버킷에 있는 파일의 키 값으로 활용한다.
      // 버킷 이름은 application.properties에서 가져온다.
      // 이렇게 하여 s3 클라이언트를 활용하여 파일을 S3로 업로드한다.
      PutObjectRequest putRequest = new PutObjectRequest(bucketName, fileKey, tempFile.getFile());
      putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
      s3.putObject(putRequest);
      log.debug("File `{}` saved to s3", tempFile.getFile().getName(), fileKey);
    } catch (Exception e) {
      log.error("Failed to save file to s3", e);
      throw new FileStorageException("Failed to save file `" + tempFile.getFile().getName() + "` to s3", e);
    }
  }

  // 업로드된 MultipartFile을 최종 목적지에 저장하는데 활용
  // 이 메서드에서 업로드된 파일인 MultipartFile 인스턴스를 S3에 저장한다.
  @Override
  public String saveUploaded(String folder, MultipartFile multipartFile) {
    Assert.notNull(s3, "S3FileStorage must be initialized properly");

    String originalFileName = multipartFile.getOriginalFilename();
    ObjectMetadata metadata = new ObjectMetadata();   // 커스텀 메타데이터인 Original-File-Name을 추가하기 위해 ObjectMetadata 인스턴스를 생성한다.
    metadata.setContentLength(multipartFile.getSize());
    metadata.setContentType(multipartFile.getContentType());
    metadata.addUserMetadata("Original-File-Name", originalFileName);
    String finalFileName = generateFileName(multipartFile);
    String s3ObjectKey = folder + "/" + finalFileName;

    String bucketName = environment.getProperty("app.file-storage.s3-bucket-name");
    Assert.hasText(bucketName, "Property `app.file-storage.s3-bucket-name` must not be blank");

    try {
      log.debug("Saving file `{}` to s3", originalFileName);
      PutObjectRequest putRequest = new PutObjectRequest(
        bucketName, s3ObjectKey, multipartFile.getInputStream(), metadata);
      putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
      s3.putObject(putRequest);
      log.debug("File `{}` saved to s3 as `{}`", originalFileName, s3ObjectKey);
    } catch (Exception e) {
      log.error("Failed to save file to s3", e);
      throw new FileStorageException("Failed to save file `" + multipartFile.getOriginalFilename() + "` to s3", e);
    }

    return s3ObjectKey;
  }

  // S3 클라이언트 인스턴스를 생성하는데 AmazonS3ClientBuilder를 활용한다.
  private AmazonS3 initS3Client() {
    String s3Region = environment.getProperty("app.file-storage.s3-region");
    Assert.hasText(s3Region, "Property `app.file-storage.s3-region` must not be blank");

    if (environment.acceptsProfiles("dev")) {
      log.debug("Initializing dev S3 client with access key and secret key");

      String s3AccessKey = environment.getProperty("app.file-storage.s3-access-key");
      String s3SecretKey = environment.getProperty("app.file-storage.s3-secret-key");

      Assert.hasText(s3AccessKey, "Property `app.file-storage.s3-access-key` must not be blank");
      Assert.hasText(s3SecretKey, "Property `app.file-storage.s3-secret-key` must not be blank");

      BasicAWSCredentials awsCredentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
      AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

      // S3 클라이언트 인스턴스를 생성하는데 AmazonS3ClientBuilder를 활용한다.
      // 빌더에게 두 종류의 정보를 제공한다. 하나는 application.properties에 설정한 리전 정보, 다른 하나는 자격 증명 정보
      
      // 이 애플리케이션은 아마존의 EC2에 올라갈 것이기 때문에 자격 증명에 대해 두가지 옵션이 있다.
      // 하나는 BasicAWSCredentials을 생성하는데 액세스 키와 시크릿 키를 활용하는 것이고,
      // 다른 하나는 자격 증명 정보를 제공하기 위해 EC2 인스턴스에 정의된 IAM Role을 활용하는 것이다. 
      // 개발 환경에서는 애플리케이션을 랩톱에서 동작시킬 경우, 액세스 키와 시크릿 키를 활용한다.
      // 그렇지 않고, EC2에서 동작시킬 경우에는 IAM Role을 활용한다.
      AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
      builder.setRegion(s3Region);
      builder.withCredentials(credentialsProvider);
      return builder.build();
    } else {
      log.debug("Initializing default S3 client using AIM role");
      return AmazonS3ClientBuilder.standard()
        .withCredentials(new InstanceProfileCredentialsProvider(false))
        .withRegion(s3Region)
        .build();
    }
  }
}

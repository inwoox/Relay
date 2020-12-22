package taskagile.domain.model.attachment;

import taskagile.domain.common.file.FileStorage;
import taskagile.domain.common.file.TempFile;
import taskagile.utils.ImageUtils;
import taskagile.utils.Size;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Component
public class ThumbnailCreator {

  private final static Logger log = LoggerFactory.getLogger(ThumbnailCreator.class);
  private final static Set<String> SUPPORTED_EXTENSIONS = new HashSet<>();
  private final static int MAX_WIDTH = 300;
  private final static int MAX_HEIGHT = 300;

  static {
    SUPPORTED_EXTENSIONS.add("png");
    SUPPORTED_EXTENSIONS.add("jpg");
    SUPPORTED_EXTENSIONS.add("jpeg");
  }

  private ImageProcessor imageProcessor;

  public ThumbnailCreator(ImageProcessor imageProcessor) {
    this.imageProcessor = imageProcessor;
  }

  public void create(FileStorage fileStorage, TempFile tempImageFile) {
    
    // 메서드의 시작점에서 소스파일인 tempImageFile이 서버에 존재하는지 확인하기 위해 어설션을 활용
    Assert.isTrue(tempImageFile.getFile().exists(), "Image file `" +
      tempImageFile.getFile().getAbsolutePath() + "` must exist");

    // 이미지의 확장자를 확인하여, 섬네일 생성이 가능한 이미지인지 확인
    String ext = FilenameUtils.getExtension(tempImageFile.getFile().getName());
    if (!SUPPORTED_EXTENSIONS.contains(ext)) {
      throw new ThumbnailCreationException("Not supported image format for creating thumbnail");
    }

    log.debug("Creating thumbnail for file `{}`", tempImageFile.getFile().getName());

    try {
      String sourceFilePath = tempImageFile.getFile().getAbsolutePath();
      if (!sourceFilePath.endsWith("." + ext)) {
        throw new IllegalArgumentException("Image file's ext doesn't match the one in file descriptor");
      }
      // 파일의 경로가 image.jpg일 경우 , 섬네일 파일의 경로를 image.thumbnail.jpg로 만든다.
      String tempThumbnailFilePath = ImageUtils.getThumbnailVersion(tempImageFile.getFile().getAbsolutePath());

      // 생성할 섬네일의 크기를 가져온다.
      Size resizeTo = getTargetSize(sourceFilePath);

      // 썸네일을 생성한 뒤 임시 이미지 파일로 저장
      imageProcessor.resize(sourceFilePath, tempThumbnailFilePath, resizeTo);

      // 스토리지에 섬네일을 저장한다
      fileStorage.saveTempFile(TempFile.create(tempImageFile.tempRootPath(), Paths.get(tempThumbnailFilePath)));

      // 임시 섬네일 파일 삭제
      Files.delete(Paths.get(tempThumbnailFilePath));
    } catch (Exception e) {
      log.error("Failed to create thumbnail for file `" + tempImageFile.getFile().getAbsolutePath() + "`", e);
      throw new ThumbnailCreationException("Creating thumbnail failed", e);
    }
  }

  private Size getTargetSize(String imageFilePath) throws IOException {
    Size actualSize = imageProcessor.getSize(imageFilePath);
    if (actualSize.getWidth() <= MAX_WIDTH && actualSize.getHeight() <= MAX_HEIGHT) {
      return actualSize;
    }

    if (actualSize.getWidth() > actualSize.getHeight()) {
      int width = MAX_WIDTH;
      int height = (int) Math.floor(((double)width / (double)actualSize.getWidth()) * actualSize.getHeight());
      return new Size(width, height);
    } else {
      int height = MAX_HEIGHT;
      int width = (int) Math.floor(((double) height / (double) actualSize.getHeight()) * actualSize.getWidth());
      return new Size(width, height);
    }
  }
}

package taskagile.domain.model.attachment;

import taskagile.utils.Size;
import org.apache.commons.lang3.math.NumberUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.process.ArrayListOutputConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

// 실제로 이미지의 크기를 조정하고, 이미지의 실제 크기를 가져오는 일을 한다.
// 이 클래스에서는 이미지 관련 처리를 위해 그래픽스매직에 의존한다. 
// 자바에서 그래픽스매직 명령어를 호출하기 위해 im4java 라이브러리를 활용한다.
@Component
public class ImageProcessor {

  private String commandSearchPath;

  // 검색 경로를 설정하는 이유는 MAC에서 그래픽스매직이 Homebrew를 통해 설치되면 그래픽스 매직 명령어가 usr/bin이 아닌 /usr/local/bin 경로에 위치하기 때문
  // brew install graphicsmagick으로 그래픽스매직을 설치해준다.
  public ImageProcessor(@Value("${app.image.command-search-path}") String commandSearchPath) {
    this.commandSearchPath = commandSearchPath;
  }

  // resize 메서드가 하는 일은 im4java API를 사용하여, 다음의 그래픽스 매직 convert 명령어와 비슷한 명령어를 만드는 것이다
  // gm convert -resize 300x185 -quality 70 source.jpg source.thumbnail.jpg

  public void resize(String sourceFilePath, String targetFilePath, Size resizeTo) throws Exception {
    Assert.isTrue(resizeTo.getHeight() > 0, "Resize height must be greater than 0");
    Assert.isTrue(resizeTo.getWidth() > 0, "Resize width must be greater than 0");

    ConvertCmd cmd = new ConvertCmd(true);
    cmd.setSearchPath(commandSearchPath);
    IMOperation op = new IMOperation();
    op.addImage(sourceFilePath);
    op.quality(70d);
    op.resize(resizeTo.getWidth(), resizeTo.getHeight());
    op.addImage(targetFilePath);
    cmd.run(op);
  }

  // 다음 명령어를 만든다 gm identify -format '%w, %h' image.jpg
  public Size getSize(String imagePath) throws IOException {
    try {
      ImageCommand cmd = new ImageCommand();
      cmd.setCommand("gm", "identify");
      cmd.setSearchPath(commandSearchPath);

      ArrayListOutputConsumer outputConsumer = new ArrayListOutputConsumer();
      cmd.setOutputConsumer(outputConsumer);

      IMOperation op = new IMOperation();
      op.format("%w,%h");
      op.addImage(imagePath);
      cmd.run(op);

      List<String> cmdOutput = outputConsumer.getOutput();
      String result = cmdOutput.get(0);
      Assert.hasText(result, "Result of command `gm identify` must not be blank");

      String[] dimensions = result.split(",");
      return new Size(NumberUtils.toInt(dimensions[0]), NumberUtils.toInt(dimensions[1]));
    } catch (Exception e) {
      throw new IOException("Failed to get image's height/width", e);
    }
  }
}

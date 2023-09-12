import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.service.Admin.VideoAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.schuanhe.plooks.Application.class)
public class PageTest {

    @Autowired
    private VideoAdminService videoAdminService;

    @Test
    public void pageTest() {
        List<Video> videoList = videoAdminService.getVideoList(1, 10, 0);
        System.out.println(videoList);
    }
}

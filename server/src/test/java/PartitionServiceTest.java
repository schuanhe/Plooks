import com.schuanhe.plooks.service.User.VideoService;
import com.schuanhe.plooks.service.User.impl.PartitionServiceImpl;
import com.schuanhe.plooks.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = com.schuanhe.plooks.Application.class)
public class PartitionServiceTest {
    @Autowired
    private PartitionServiceImpl partitionServiceImpl;

    @Autowired
    private VideoService videoService;

    @Autowired
    private RedisCache redisCache;

    @Test
    public void testPartitionList() {
        System.out.println(partitionServiceImpl.partitionList());
    }

    @Test
    public void getSubPartitionIds() {
        boolean importantAnnouncement = redisCache.deleteObject("importantAnnouncement");
        System.out.println(importantAnnouncement);

    }
}

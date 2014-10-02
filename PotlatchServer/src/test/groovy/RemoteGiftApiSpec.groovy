import java.util.Date
import java.util.Random

import retrofit.RestAdapter.LogLevel
import retrofit.client.ApacheClient

import com.pataniqa.coursera.potlatch.model.Gift
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder
import com.pataniqa.coursera.potlatch.server.UnsafeHttpsClient

class RemoteGiftApiSpec extends spock.lang.Specification {

    def TEST_URL = "https://localhost:8443"
    def USERNAME1 = "admin"
    def USERNAME2 = "user0"
    def PASSWORD = "pass"
    def CLIENT_ID = "mobile";

    def readWriteVideoSvcUser1 = new SecuredRestBuilder()
        .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
        .setEndpoint(TEST_URL)
        .loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
        .setLogLevel(LogLevel.FULL)
        .username(USERNAME1)
        .password(PASSWORD)
        .clientId(CLIENT_ID)
        .build()
        .create(RemoteGiftApi.class)
        
   def random = new Random()
        
    def randomGift() {
        def id = random.nextLong()
        def title = "Video-"+id
        def description = "Description about video " + id
        def videoUri = "http://coursera.org/some/video-"+id
        def imageUri = "http://coursera.org/some/image-"+id
        def created = new Date()
        def userId = random.nextLong()
        def giftChainId = random.nextLong()
        return new Gift(id, title, description, videoUri, imageUri, created, userId, giftChainId)
    }

    def "Add a gift"() {
        when:
        def gift = randomGift()
        def received = readWriteVideoSvcUser1.insert(gift)
        
        then:
        gift.getTitle() == received.getTitle()
        gift.getDescription() == received.getDescription()
        gift.getGiftChainID() == received.getGiftChainID()
        gift.getUserID() == received.getUserID()
    }
}
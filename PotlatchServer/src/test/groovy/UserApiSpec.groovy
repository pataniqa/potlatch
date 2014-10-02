import java.util.Date
import java.util.Random

import retrofit.RestAdapter.LogLevel
import retrofit.client.ApacheClient

import com.pataniqa.coursera.potlatch.model.User
import com.pataniqa.coursera.potlatch.model.GetId
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi
import com.pataniqa.coursera.potlatch.store.remote.RemoteUserApi
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder
import com.pataniqa.coursera.potlatch.server.UnsafeHttpsClient

class UserApiSpec extends spock.lang.Specification {

    def TEST_URL = "https://localhost:8443"
    def USERNAME1 = "user0"
    def PASSWORD = "pass"
    def CLIENT_ID = "mobile"

    def userSvcUser1 = new SecuredRestBuilder()
        .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
        .setEndpoint(TEST_URL)
        .loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
        .setLogLevel(LogLevel.FULL)
        .username(USERNAME1)
        .password(PASSWORD)
        .clientId(CLIENT_ID)
        .build()
        .create(RemoteUserApi.class)

    def random = new Random()

    def randomUser() {
        def name = "some-random-user-" + random.nextLong() 
        new User(name)
    }
    
    def listUsers() {
        return userSvcUser1.findAll()
    }

    def "Add a user"() {
        when:
        def numberOfUsersBefore = listUsers().size()
        def user = randomUser()
        def received = userSvcUser1.insert(user)

        then:
        user.getName() == received.getName()
        listUsers().size() == numberOfUsersBefore + 1
    }
    
    def "Update a user"() {
        when:
        def user = randomUser()
        def received = userSvcUser1.insert(user)
        def numberOfUsersBefore = listUsers().size()
        def result = userSvcUser1.update(received.getId(), received)

        then:
        listUsers().size() == numberOfUsersBefore
    }
    
    def "Delete a user"() {
        when:
        def users = listUsers()
        def result = userSvcUser1.delete(users.get(0).getId())

        then:
        listUsers().size() == users.size() - 1
    }
    

}

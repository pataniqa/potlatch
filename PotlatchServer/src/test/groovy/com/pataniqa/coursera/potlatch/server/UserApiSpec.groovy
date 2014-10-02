package com.pataniqa.coursera.potlatch.server

import com.pataniqa.coursera.potlatch.model.User
import com.pataniqa.coursera.potlatch.store.remote.RemoteUserApi

class UserApiSpec extends spock.lang.Specification {

    def userSvcUser = new ApiSpecHelper().svcUser.create(RemoteUserApi.class)
    
    def numberOfUsers() {
        return userSvcUser.findAll().size()
    }

    def "Create, retrieve, update and delete a user"() {
        
        when: "a user is added"
        def numberOfUsersBefore = numberOfUsers()
        def user = new User("some-random-user-" + new Random().nextLong())
        def newUser = userSvcUser.insert(user)

        then: "the new user should have the same name"
        user.getName() == newUser.getName()
        and: "there should be one more user"
        numberOfUsers() == numberOfUsersBefore + 1

        when: "a user is updated"
        numberOfUsersBefore = numberOfUsers()
        userSvcUser.update(newUser.getId(), newUser)

        then: "there should be the same number of users"
        numberOfUsers() == numberOfUsersBefore

        when: "a user is deleted"
        numberOfUsersBefore = numberOfUsers()
        userSvcUser.delete(newUser.getId())

        then: "there should be one less user"
        numberOfUsers() == numberOfUsersBefore - 1
        
    }

}

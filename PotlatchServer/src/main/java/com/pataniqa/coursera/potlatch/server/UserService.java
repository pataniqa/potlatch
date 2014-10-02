package com.pataniqa.coursera.potlatch.server;

import static com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi.ID;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.pataniqa.coursera.potlatch.model.User;
import com.pataniqa.coursera.potlatch.server.model.ServerUser;
import com.pataniqa.coursera.potlatch.server.repository.UserRepository;
import com.pataniqa.coursera.potlatch.store.remote.RemoteUserApi;

@Controller
public class UserService {

    static Logger log = Logger.getLogger(GiftService.class.getName());
    
    @Autowired private UserRepository users;

    @RequestMapping(value = RemoteUserApi.USER_PATH, method = RequestMethod.POST)
    public @ResponseBody
    ServerUser insert(@RequestBody User user) {
        return users.save(new ServerUser(user));
    }

    @RequestMapping(value = RemoteUserApi.USER_PATH, method = RequestMethod.GET)
    public @ResponseBody
    Collection<ServerUser> findAll() {
        return Lists.newArrayList(users.findAll());
    }

    @RequestMapping(value = RemoteUserApi.USER_ID_PATH, method = RequestMethod.PUT)
    public @ResponseBody
    ServerUser update(@PathVariable(ID) long id, @RequestBody User user) {
        return users.save(users.findOne(id).update(user));
    }

    @RequestMapping(value = RemoteUserApi.USER_ID_PATH, method = RequestMethod.DELETE)
    public @ResponseBody boolean delete(@PathVariable(ID) long id) {
        users.delete(id);
        return true;
    }

}

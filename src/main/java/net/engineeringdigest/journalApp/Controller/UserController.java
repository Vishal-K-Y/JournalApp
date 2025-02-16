package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Service.HashifyService;
import net.engineeringdigest.journalApp.Service.UserService;
import net.engineeringdigest.journalApp.Service.WeatherService;
import net.engineeringdigest.journalApp.api.response.HashResponse;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private HashifyService hashifyService;

    @GetMapping("/health")
    public String healthCheck(){
        return "working fine!!";
    }
    //add
//    @PostMapping
//    public String addUser(@RequestBody User user){
//        //userService.saveEntry(user);
//        userService.saveNewUser(user);
//        return "User added successfully";
//    }

    //fetch all users
//    @GetMapping
//    public List<User> getAll(){
//        return userService.getAllUsers();
//    }

    //find user with id
    @GetMapping("/id/{id}")
    public Optional<User> getUserById(@PathVariable long id){
        return userService.findUserById(id);
    }

    //find user with name
    @GetMapping("/name/{name}")
    public Optional<User> getUserById(@PathVariable String name){
        return userService.findByUserName(name);
    }

    //delete user
//    @DeleteMapping("/id/{id}")
//    public String removeUser(@PathVariable long id){
//        userService.removeUser(id);
//        return "User removed successfully";
//    }

    //modify existing user
//    @PutMapping("/id/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
//        User oldUserData=userService.findUserById(id).orElse(null);
//        if(oldUserData!=null){
//            oldUserData.setUserName((user.getUserName()==null || user.getUserName().isEmpty()? oldUserData.getUserName() : user.getUserName()));
//            oldUserData.setPassword((user.getPassword()==null || user.getPassword().isEmpty()? oldUserData.getPassword() : user.getPassword()));
//            userService.saveEntry(oldUserData);
//            return new ResponseEntity<>(oldUserData, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User oldUserData=userService.findByUserName(user.getUserName()).orElse(null);
//            oldUserData.setUserName((user.getUserName()==null || user.getUserName().isEmpty()? oldUserData.getUserName() : user.getUserName()));
//            oldUserData.setPassword((user.getPassword()==null || user.getPassword().isEmpty()? oldUserData.getPassword() : user.getPassword()));
        if(oldUserData!=null){
            oldUserData.setUserName(userName);
            oldUserData.setPassword(user.getPassword());
            userService.saveNewUser(oldUserData);
            return new ResponseEntity<>(oldUserData, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public String removeUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        userService.deleteByUserName(authentication.getName());
        return "User removed successfully";
    }

    @GetMapping("/greetings")
    public ResponseEntity<?> greeting(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse=weatherService.getWeather("Mumbai");
        String greeting= "";
        if(weatherResponse!=null){
            greeting= ", weather feels like "+ weatherResponse.getCurrent().getFeelslike()+" and description is- "+ weatherResponse.getCurrent().getWeatherDescriptions().get(0);
        }
        return new ResponseEntity<>("Hi "+authentication.getName()+ greeting , HttpStatus.OK);
    }

    @GetMapping("/hashingName")
    public ResponseEntity<?> hashingName(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        HashResponse hashResponse=hashifyService.generateHash(userName);
        String getHashFromName="";
        if(hashResponse!=null){
            getHashFromName=", the hash you generated is "+hashResponse.getDigest()+" and type "+hashResponse.getType();
        }
        return new ResponseEntity<>("Hi "+ userName + getHashFromName , HttpStatus.OK);
    }
}

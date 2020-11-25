package br.com.wep.app.Services.Entities;

import br.com.wep.app.config.TokenService;
import br.com.wep.app.model.Entities.Event;
import br.com.wep.app.model.Entities.User;
import br.com.wep.app.model.Repos.EventRepo;
import br.com.wep.app.model.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TokenService tokenService;

    public boolean delete(int event_id, String auth){
        Event event = eventRepo.findById(event_id).get();
        User user = userRepo.getUserByEmail(tokenService.decodeToken(auth).getSubject());

        if(event.getUser().getId() != user.getId()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }else{
            eventRepo.deleteById(event.getId());
            return true;
        }
    }
}

package br.com.wep.app.Services.Entities;

import br.com.wep.app.exceptions.InvalidUserEventException;
import br.com.wep.app.exceptions.LikeNotFound;
import br.com.wep.app.model.Entities.Event;
import br.com.wep.app.model.Entities.Like;
import br.com.wep.app.model.Entities.User;
import br.com.wep.app.model.Repos.EventRepo;
import br.com.wep.app.model.Repos.LikeRepo;
import br.com.wep.app.model.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepo likeRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EventRepo eventRepo;

    public List<Like> index(){
        return (List<Like>) likeRepo.findAll();
    };

    public Like save(final int userId, final int eventId) throws Exception {

         Like existLike = likeRepo.findLikebyEventAndUser(userId, eventId);

         if (existLike != null) throw new Exception("Já existe a curtida do usuário para o evento.");

        Event event = eventRepo.findById(eventId).get();
        User user = userRepo.findById(userId).get();

        Like newLike = new Like(event, user);

        likeRepo.save(newLike);

        return newLike;

    };

    public Like deleteLike(final int eventId, final int userId) throws InvalidUserEventException, LikeNotFound {

        User user = userRepo.findById(userId).get();
        Event event = eventRepo.findById(eventId).get();

        if (user == null) throw new InvalidUserEventException("Erro ao deletar");
        if (event == null) throw new InvalidUserEventException("Erro ao deletar");

        Like like = likeRepo.findLikebyEventAndUser(userId, eventId);

        if (like == null) throw new LikeNotFound("Erro ao deletar");

        likeRepo.delete(like);

        eventRepo.save(event);

        return like;

    };

    
}

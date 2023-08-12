package Services;

import com.pengrad.telegrambot.request.SendMessage;

import java.util.Optional;

public interface ShelterService {
    pro.sky.entity.Shelter chooseShelter(String type);

    SendMessage giveMenu(Long fromId);

    SendMessage start(pro.sky.entity.Shelter shelter, Long fromId);

    SendMessage aboutShelter(pro.sky.entity.Shelter shelter, Long fromId);

    SendMessage getGuardContact(pro.sky.entity.Shelter shelter, Long fromId);

    SendMessage infoShelter(pro.sky.entity.Shelter shelter, Long fromId);

    pro.sky.entity.Shelter saveShelterToRepository(pro.sky.entity.Shelter shelter);

    Optional<pro.sky.entity.Shelter> findShelter(Long id);
}

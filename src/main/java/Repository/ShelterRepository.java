package Repository;

public interface ShelterService {
    Shelter chooseShelter(String type);

    SendMessage giveMenu(Long fromId);

    SendMessage start(Shelter shelter, Long fromId);

    SendMessage aboutShelter(Shelter shelter, Long fromId);

    SendMessage getGuardContact(Shelter shelter, Long fromId);

    SendMessage infoShelter(Shelter shelter, Long fromId);

    Shelter saveShelterToRepository(Shelter shelter);

    Optional<Shelter> findShelter(Long id);
}

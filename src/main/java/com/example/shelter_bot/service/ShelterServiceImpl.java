package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.repository.ShelterRepository;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShelterServiceImpl implements ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterServiceImpl(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Метод отвечает за выбор приюта, соответствующего определённому типу и вывод списка возможных действий.
     *
     * @return {@link Shelter} nullable
     */
    @Override
    public Shelter chooseShelter(PetType petType) {
        Shelter shelter = new Shelter();
        Optional<PetType> petTypeTest = Optional.ofNullable(petType);
        if (petTypeTest.isPresent()) {
            shelter = shelterRepository.findShelterByPetTypeIs(petType);
        }
        return shelter;
    }

    /**
     * Метод вывода меню для формирования первоначального запроса пользователя.
     *
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage giveMenu(Long fromId) {
        String menu = "Выберите пункт меню:";
        return new SendMessage(fromId, menu).replyMarkup(requestKeyboardHandler());
    }

    /**
     * Стартовый метод для работы с приютом.
     *
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage start(Shelter shelter, Long fromId) {
        String mess = String.format(" * Тип приюта: %s * \nВыберите действие:", shelter.getPetType().toString());
        SendMessage sendMessage = new SendMessage(fromId, mess);
        sendMessage.parseMode(ParseMode.Markdown)
                .replyMarkup(getKeyboard());
        return sendMessage;
    }

    /**
     * Вывод информации о приюте.
     *
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage aboutShelter(Shelter shelter, Long fromId) {
        return new SendMessage(fromId, shelter.getAbout()).replyMarkup(getKeyboard());
    }

    /**
     * Получение контактных данных охраны.
     *
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage getGuardContact(Shelter shelter, Long fromId) {
        Optional<String> guardContact = Optional.ofNullable(shelter.getGuard());
        String mess = guardContact.orElse("Нет поста охраны.");
        return new SendMessage(fromId, mess).replyMarkup(getKeyboard());
    }

    /**
     * Вывод общей информации о приюте: адрес, расписание работы, схема проезда.
     *
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage infoShelter(Shelter shelter, Long fromId) {
        String info = String.format("<strong>Вы можете найти нас по адресу:</strong>" +
                        " \n%s\n<strong>Наш график работы:</strong>\n%s\n<strong>Схема проезда: </strong>%s",
                shelter.getAddress(), shelter.getSchedule(), shelter.getLocationMap());
        return new SendMessage(fromId, info).parseMode(ParseMode.HTML).replyMarkup(getKeyboard());
    }

    /**
     * Метод вызова клавиатуры для формирования первоначального запроса пользователя.
     *
     * @return клавиатура.
     */
    private Keyboard requestKeyboardHandler() {
        return new InlineKeyboardMarkup().addRow(
                new InlineKeyboardButton("Узнать информацию о приюте").callbackData("/stage1"),
                new InlineKeyboardButton("Как взять животное из приюта").callbackData("/stage2")
        ).addRow(
                new InlineKeyboardButton("Прислать отчёт о питомце").callbackData("/stage3"),
                new InlineKeyboardButton("Позвать волонтёра").callbackData("/volunteer")
        );
    }

    /**
     * Клавиатура для консультации нового пользователя.
     *
     * @return {@link Keyboard}
     */
    private Keyboard getKeyboard() {
        return new InlineKeyboardMarkup().addRow(
                new InlineKeyboardButton("Рассказать о приюте").callbackData("/about"),
                new InlineKeyboardButton("Как к нам попасть").callbackData("/info")
        ).addRow(
                new InlineKeyboardButton("Контактные данные охраны").callbackData("/guard"),
                new InlineKeyboardButton("Техника безопасности").url("https://clck.ru/34Nb7f").callbackData("/safety")
        ).addRow(
                new InlineKeyboardButton("Свяжитесь со мной").callbackData("/contact"),
                new InlineKeyboardButton("Позвать волонтёра").callbackData("/volunteer")
        );
    }

    /**
     * Метод сохранения данных о приюте в базу данных.
     *
     * @param shelter экземпляр класса {@link Shelter}
     */
    @Override
    public Shelter saveShelterToRepository(Shelter shelter) {
        shelterRepository.save(shelter);
        return shelter;
    }

    /**
     * Метод поиска приюта по его идентификатору.
     *
     * @param id идентификатор приюта.
     */
    @Override
    public Optional<Shelter> findShelter(Long id) {
        return shelterRepository.findById(id);
    }
}

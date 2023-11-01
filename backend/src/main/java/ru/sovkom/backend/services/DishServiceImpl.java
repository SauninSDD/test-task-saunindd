package ru.sovkom.backend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.repositories.ClientRepository;
import ru.sovkom.backend.repositories.DishRepository;

import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService{
    private final DishRepository dishRepository;
    private final ClientRepository clientRepository;


    public DishServiceImpl(DishRepository dishRepository, ClientRepository clientRepository) {
        this.dishRepository = dishRepository;
        this.clientRepository = clientRepository;
    }


    @Override
    @Transactional
    public long addDish(Dish dish) {
        log.info("Добавляет блюдо с именем {}", dish.getName());

        var isExistsDish = dishRepository.existsByName(dish.getName());
        if (!isExistsDish) {
            dishRepository.save(dish);
        }
        return dishRepository.save(dish).getId();
    }

    @Override
    public List<Dish> getListDishes() {
        log.info("Получает все блюда");
        return dishRepository.findAll();
    }

    @Override
    public boolean updateDish(Dish dish) {
        log.info("Обновляет блюдо с именем {}", dish.getName());

        var isExists = dishRepository.existsById(dish.getId());
        if (isExists) {
            dishRepository.save(dish);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean deleteDish(Dish dish) {
        log.info("Удаляет блюдо  {}", dish);
        long countClientsWithFavoriteDish = clientRepository.countClientsWithFavoriteDish(dish);
        var isExistsDish = dishRepository.existsById(dish.getId());
        if (isExistsDish && countClientsWithFavoriteDish == 0) {
            dishRepository.deleteById(dish.getId());
            return true;
        }

        return false;
    }

    @Override
    public List<Dish> getDishesByName(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return dishRepository.findAll();
        } else {
            return dishRepository.findDishesByName(stringFilter);
        }
    }

    }

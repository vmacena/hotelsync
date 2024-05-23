package br.edu.ifsp.hotelsync.domain.usecases.room.find;

import br.edu.ifsp.hotelsync.domain.entities.room.Room;
import br.edu.ifsp.hotelsync.domain.persistence.dao.RoomDAO;

import java.util.NoSuchElementException;

public class FindOneRoomUseCaseImpl implements FindOneRoomUseCase {

    private final RoomDAO repository;

    public FindOneRoomUseCaseImpl(RoomDAO repository) {
        this.repository = repository;
    }


    @Override
    public Room findRoom(Long id) {
        return repository.findOneByKey(id).orElseThrow(() ->
                new NoSuchElementException("Room of id " + id + " not found"));
    }
}

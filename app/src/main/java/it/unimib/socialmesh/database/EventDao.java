package it.unimib.socialmesh.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.unimib.socialmesh.model.Event;

/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * <a href="https://developer.android.com/training/data-storage/room/accessing-data">...</a>
 */
@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY name DESC")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE remoteId = :id")
    Event getEvent(long id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertNewsList(List<Event> eventList);

    @Insert
    void insertAll(Event... event);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertEventsList(List<Event> eventsList);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void deleteAll();


    @Delete
    void deleteAllWithoutQuery(Event... event);

}

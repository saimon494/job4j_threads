package ru.job4j.synch;

import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserStorageTest {

    private static class ThreadTransfer extends Thread {

        private final UserStorage userStorage;
        private final User user;

        public ThreadTransfer(UserStorage userStorage, User user) {
            this.userStorage = userStorage;
            this.user = user;
        }

        @Override
        public void run() {
            this.userStorage.add(user);
            this.userStorage.update(user);
            this.userStorage.delete(user);
            this.userStorage.transfer(user.getId(), user.getId(), user.getAmount());
        }
    }

    @Test
    public void whenAddThenTransfer() throws InterruptedException {
        UserStorage userStorage = new UserStorage();
        User user1 = new User(1, 100);
        User user2 = new User(2, 200);
        Thread first = new ThreadTransfer(userStorage, user1);
        Thread second = new ThreadTransfer(userStorage, user2);
        first.start();
        second.start();
        first.join();
        second.join();
        userStorage.add(user1);
        userStorage.add(user2);
        userStorage.transfer(1, 2, 50);
        assertThat(user1.getAmount(), is(50));
        assertThat(user2.getAmount(), is(250));
        assertFalse(userStorage.transfer(1, 3, 50));
        assertFalse(userStorage.transfer(1, 2, 150));
        user1.setAmount(300);
        userStorage.update(user1);
        assertTrue(userStorage.transfer(1, 2, 150));
        assertThat(user1.getAmount(), is(150));
        userStorage.delete(user2);
        assertFalse(userStorage.update(user2));
    }
}
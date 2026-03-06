package week4.app.repository.impl;

import week4.app.models.User;
import week4.app.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Integer save(User user) {
        return 0;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void updateAvatar(Integer userId, String avatarUrl) {

    }

    @Override
    public void updateLastLogoutTime(Integer userId, Long timestamp) {

    }

    @Override
    public Long getLastLogoutTime(Integer userId) {
        return 0L;
    }
}

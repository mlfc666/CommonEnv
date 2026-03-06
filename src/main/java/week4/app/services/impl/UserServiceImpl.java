package week4.app.services.impl;

import week4.app.dto.LoginDTO;
import week4.app.repository.UserRepository;
import week4.app.services.UserService;
import week4.framework.models.MultipartFile;

import java.util.Map;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> register(LoginDTO dto) {
        return Map.of();
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        return Map.of();
    }

    @Override
    public String uploadAvatar(Integer userId, MultipartFile file) {
        return "";
    }

    @Override
    public void logout(Integer userId) {

    }

    @Override
    public boolean isTokenValid(Integer userId, Long iat) {
        return false;
    }
}

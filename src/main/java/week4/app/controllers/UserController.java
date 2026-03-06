package week4.app.controllers;

import week4.app.dto.LoginDTO;
import week4.app.dto.UserInfoDTO;
import week4.app.services.UserService;
import week4.framework.annotations.*;
import week4.framework.models.MultipartFile;
import week4.framework.utils.JwtUtils;

import java.util.Map;

@RestController
public class UserController {
    @Inject
    private UserService userService;

    @PostMapping("/api/user/register")
    public String register(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> claims = Map.of(); // TODO: 添加更多用户信息
        return JwtUtils.createToken(loginDTO.username(), 3600, claims);
    }

    @PostMapping("/api/user/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> claims = Map.of(); // TODO: 添加更多用户信息
        return JwtUtils.createToken(loginDTO.username(), 3600, claims);
    }
    @PostMapping("/api/user/info")
    public UserInfoDTO info() {
        return new UserInfoDTO();
    }

    @PostMapping("/api/user/upload-avatar")
    public void uploadAvatar(@RequestPart("avatar") MultipartFile avatar) {

    }

    @PostMapping("/api/user/logout")
    public void logout() {
    }
}
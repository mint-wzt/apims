package me.zhengjie.modules.security.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.security.vo.JwtUser;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    public UserDetailsServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        UserDto user = userService.findByName(username);
        if (user == null) {
            throw new BadRequestException("账号不存在");
        } else {
            if (!user.getEnabled()) {
                throw new BadRequestException("账号未激活");
            }
            return createJwtUser(user);
        }
    }

    private UserDetails createJwtUser(UserDto user) {
        JwtUser jwtUser =  new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getSex(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                user.getName(),
                Optional.ofNullable(user.getDept()).map(DeptSmallDto::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(JobSmallDto::getName).orElse(null),
                Optional.ofNullable(user.getRegion()).map(RegionSmallDto::getExtName).orElse(null),
                user.getDept().getId(),
                user.getRegion().getId(),
                roleService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateTime(),
                user.getLastPasswordResetTime()
        );
        log.info(JSON.toJSONString(jwtUser));
        return jwtUser;
    }
}

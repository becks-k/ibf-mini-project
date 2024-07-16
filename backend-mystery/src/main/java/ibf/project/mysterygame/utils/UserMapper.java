package ibf.project.mysterygame.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ibf.project.mysterygame.models.authentication.SignUp;
import ibf.project.mysterygame.models.authentication.User;
import ibf.project.mysterygame.models.authentication.UserEntity;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserEntity user);

    @Mapping(target = "password", ignore = true) 
    UserEntity signUpToUserEntity(SignUp signUp);

}

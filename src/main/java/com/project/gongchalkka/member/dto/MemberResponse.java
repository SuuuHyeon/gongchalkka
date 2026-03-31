package com.project.gongchalkka.member.dto;

import com.project.gongchalkka.global.jwt.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String nickname;
    private Role role;
}

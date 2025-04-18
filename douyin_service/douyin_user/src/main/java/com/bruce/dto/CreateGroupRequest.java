package com.bruce.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreateGroupRequest {

    @NotNull(message = "群组名称不能为空")
    @Size(min = 1, max = 100, message = "群组名称长度应在1到100个字符之间")
    private String groupName;        // 群组名称

    private Long createdBy;

    @NotNull(message = "群组类型不能为空")
    private Integer groupType;       // 群组类型（例如：1=公开群，2=私密群等）

    @NotNull(message = "群成员不能为空")
    private List<Long> memberIds;    // 群成员ID列表，至少包含一个成员，通常包括群主

    private String description;      // 群组描述，选填

    private Boolean isGroupAdmin;    // 是否是管理员，默认是true

    private String avatarUrl;        // 群组头像，选填
}

package com.bruce.video.entity;

import lombok.Data;

@Data
public class VideoOne {


private  String musicName;

private  String videoUrl;

private  String nickName;

private  String userPic;

private  String comment;

}

//    select m.music_name ,  v.video_url , u.nickname , u.user_pic , c.comment from video v
//    inner join user u on v.auth_id = u.id
//    inner join music m on v.music_id = m.music_video_id
//    inner join comment c on v.video_id = c.comment_video_id
//    ;

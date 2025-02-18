package com.bruce.dao;

import com.bruce.entity.VideoOne;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * (Video)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-25 13:03:24
 */
@Mapper
public interface VideoOneDao {






@Select("Select u.user_pic as touimg,u.nickname,u.back_img as backing,u.city,v.video_img as videoimgurl,v.video_url as videohref from " +
        "user u left join video v on u.id=v.auth_id where u.id=#{userid}")
    List<Map>  getauth(@Param("userid") Integer userid);










    @Select({
            "SELECT",
            "    m.music_name as music,",
            "    v.video_id as videoid,",
            "    v.video_comment,",
            "    u.id as userid,",
            "    m.music_avatar as musicAvatar,",
            "    m.music_id,",
            "    v.video_url as videoUrl ,",
            "    v.video_comment,",
            "    u.nickname AS username,",
            "    u.user_pic as userAvatar,",
//            "    COUNT(c.comment) AS comment_count,",
            "    (SELECT COUNT(ll.like) FROM likeone ll WHERE ll.like_videoid = v.video_id and `like` = 1 ) AS likeNum,",
            "    (SELECT COUNT(cc.comment) FROM comment cc WHERE cc.comment_video_id = v.video_id and cc.comment is not null ) AS commentNum,",
//            "    COUNT(l.share) AS share_num,",
//            "    COUNT(l.collect) AS collect_num",
            "    (SELECT COUNT(ll.share) FROM likeone ll WHERE ll.like_videoid = v.video_id and share = 1 ) AS shareNum,",
            "    (SELECT COUNT(ll.collect) FROM likeone ll WHERE ll.like_videoid = v.video_id and collect = 1 ) AS collectNum",

            "FROM video v",
            "    left JOIN user u ON v.auth_id = u.id",
            "    left JOIN music m ON v.music_id = m.music_video_id",
            "    LEFT JOIN comment c ON v.video_id = c.comment_video_id",
            "    LEFT JOIN likeone l ON v.video_id = l.like_videoid",
            "GROUP BY m.music_name, v.video_url, u.nickname, u.user_pic, v.video_id",
            "LIMIT #{limit} OFFSET #{offset}"
    })
    List<Map> queryVideoInfo(@Param("limit") int limit, @Param("offset") int offset,@Param("videoType") String videoType);


    @Select({
            "SELECT",
            "    m.music_name as music,",
            "    v.video_id as videoid,",
            "    v.video_comment,",
            "    u.id as userid,",
            "    m.music_avatar as musicAvatar,",
            "    m.music_id,",
            "    v.video_url as videoUrl ,",
            "    v.video_comment,",
            "    u.nickname AS username,",
            "    u.user_pic as userAvatar,",
//            "    COUNT(c.comment) AS comment_count,",
            "    (SELECT COUNT(ll.like) FROM likeone ll WHERE ll.like_videoid = v.video_id and `like` = 1 ) AS likeNum,",
            "    (SELECT COUNT(cc.comment) FROM comment cc WHERE cc.comment_video_id = v.video_id and cc.comment is not null ) AS commentNum,",
//            "    COUNT(l.share) AS share_num,",
//            "    COUNT(l.collect) AS collect_num",
            "    (SELECT COUNT(ll.share) FROM likeone ll WHERE ll.like_videoid = v.video_id and share = 1 ) AS shareNum,",
            "    (SELECT COUNT(ll.collect) FROM likeone ll WHERE ll.like_videoid = v.video_id and collect = 1 ) AS collectNum",

            "FROM video v",
            "    left JOIN user u ON v.auth_id = u.id",
            "    left JOIN music m ON v.music_id = m.music_video_id",
            "    LEFT JOIN comment c ON v.video_id = c.comment_video_id",
            "    LEFT JOIN likeone l ON v.video_id = l.like_videoid",
            "GROUP BY m.music_name, v.video_url, u.nickname, u.user_pic, v.video_id",
            "LIMIT #{limit} OFFSET #{offset}"
    })
    List<Map> queryVideoInfoNoLogin(@Param("limit") int limit, @Param("offset") int offset);





    @Update("UPDATE likeone SET `like` = 0 WHERE like_userid = #{userid} AND like_videoid = #{videoid}")
    int tolike(@Param("userid") int userid, @Param("videoid") int videoid);

    @Insert("INSERT INTO likeone (like_userid, like_videoid, `like`, create_time) VALUES (#{userid}, #{videoid}, 1, NOW())")
    int toFirstlike(@Param("userid") int userid, @Param("videoid") int videoid);

    @Select("select * from likeone where like_userid = #{userid} and like_videoid =#{videoid} ")
    List<VideoOne> getlikeexit(@Param("userid") int userid, @Param("videoid") int videoid);


    @Select("SELECT * FROM likeone WHERE like_userid = #{userid} AND like_videoid = #{videoid} AND `like` = 1")
    List<Map> getlikeexitMap(@Param("userid") int userid, @Param("videoid") int videoid);




    @Select({
            "<script>",
            "SELECT * FROM",
            "<if test='type == \"like\"'>",
            "likeone",
            "</if>",
            "<if test='type == \"collect\"'>",
            "collect",
            "</if>",
            "WHERE 1=1",  // 这个条件是为了简化后续的 AND 逻辑
            "<if test='userid != null'>",
            "AND like_userid = #{userid}",
            "</if>",
            "<if test='videoid != null'>",
            "AND like_videoid = #{videoid}",
            "</if>",
            "</script>"
    })
    List<VideoOne> getlikeexitone(@Param("userid") Integer userid, @Param("videoid") Integer videoid, @Param("type") String type);










}


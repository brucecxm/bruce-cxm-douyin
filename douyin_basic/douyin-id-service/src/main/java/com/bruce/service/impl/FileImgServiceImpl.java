package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.FileImgDao;
import com.bruce.entity.FileImg;
import com.bruce.service.FileImgService;
import org.springframework.stereotype.Service;

/**
 * (FileImg)表服务实现类
 *
 * @author makejava
 * @since 2024-11-19 17:34:01
 */
@Service("fileImgService")
public class FileImgServiceImpl extends ServiceImpl<FileImgDao, FileImg> implements FileImgService {

}


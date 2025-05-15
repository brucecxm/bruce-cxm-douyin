package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bruce.entity.Employee;
import com.bruce.mapper.EmployeeMapper;
import com.bruce.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

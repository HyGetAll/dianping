package com.hmdp;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.service.IUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@Slf4j
public class GenerateToken {
    @Resource
    private MockMvc mockMvc;

    @Resource
    private IUserService userService;

    @Resource
    private ObjectMapper mapper;

    @Test
    // 忽视异常
    @SneakyThrows
    public void login() {
        String phone = "";
        String code = "";
        //注意！这里的绝对路径设置为自己想要的地方
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("D:\\A_RedisProject\\resource\\token.txt"));
        //先模拟10个用户的登录
        for (int i = 10; i < 1010; i++) {
            /**
             * 通过id从数据库中获得user对象
             * 注意这里要看用户表中有没有id从10到20的用户数据
             * 也就是说要生成1000个用户token，
             * 你首先的有连续的从1到1000的用户id。
             */
            User user = userService.getById(i);
            phone = user.getPhone();
            //创建虚拟请求，模拟通过手机号，发送验证码
            ResultActions perform1 = mockMvc.perform(MockMvcRequestBuilders
                    .post("/user/code?phone=" + phone));
            //获得Response的body信息
            String resultJson1 = perform1.andReturn().getResponse().getContentAsString();
            //将结果转换为result对象
            Result result = JSONUtil.toBean(resultJson1, Result.class);
            //获得验证码
            code = result.getData().toString();
            //创建登录表单
            LoginFormDTO loginFormDTO = new LoginFormDTO();
            loginFormDTO.setCode(code);
            loginFormDTO.setPhone(phone);
            //将表单转换为json格式的字符串
            String loginFormDtoJson = JSONUtil.toJsonStr(loginFormDTO);
            //创建虚拟请求，模拟登录
            ResultActions perform2 = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                    //设置contentType表示为json信息
                    .contentType(MediaType.APPLICATION_JSON)
                    //放入json对象
                    .content(loginFormDtoJson));

            String resultJson2 = perform2.andReturn().getResponse().getContentAsString();
            Result result2 = JSONUtil.toBean(resultJson2, Result.class);
            //获得token
            String token = result2.getData().toString();
            //写入
            osw.write(token+"\n");
        }
        //关闭输出流
        osw.close();

    }

        /**
         * 生成tokens.txt文件
         * @param list
         * @param suffixPath
         * @throws Exception
         */
        private static void writeToTxt(List<String> list, String suffixPath) throws Exception {
            // 1. 创建文件
            File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources" + suffixPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 2. 输出
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            for (String content : list) {
                bw.write(content);
                bw.newLine();
            }
            bw.close();
            log.info("tokens.txt文件生成完毕！");
        }

}

package com.chengshiun;

import com.chengshiun.dto.MemberRegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void testRegister_success() throws Exception {
        //創建一個新的 member
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test2@gmail.com");
        memberRegisterRequest.setPassword("222");
        memberRegisterRequest.setName("Test 2");
        memberRegisterRequest.setAge(20);

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        //測試是否能使用該 member 登入
        RequestBuilder loginRequestBuilder = MockMvcRequestBuilders
                .post("/userLogin")
                .with(httpBasic("test2@gmail.com", "222"));

        mockMvc.perform(loginRequestBuilder)
                .andExpect(status().is(200));

        //是否可以觀看免費電影
        RequestBuilder watchFreeMovieRequestBuilder = MockMvcRequestBuilders
                .post("/watchFreeMovie")
                .with(httpBasic("test2@gmail.com", "222"));

        mockMvc.perform(loginRequestBuilder)
                .andExpect(status().is(200));
    }

    @Test
    public void testUserLogin_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/userLogin")
                .with(httpBasic("normal@gmail.com", "normal"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

}

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CsrfTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void watchFreeMovie_withoutCsrfToken_fail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/watchFreeMovie")
                .with(httpBasic("normal@gmail.com", "normal"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    public void watchFreeMovie_withCsrfToken_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/watchFreeMovie")
                .with(httpBasic("normal@gmail.com", "normal"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    @Test
    public void ignoreRequestMatchers_register_And_userLogin_success() throws Exception {
        //register api
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("csrfTest@gmail.com");
        memberRegisterRequest.setPassword("csrfTest");
        memberRegisterRequest.setName("Csrf Test");
        memberRegisterRequest.setAge(20);

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder register_RequestBuilder = MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(register_RequestBuilder)
                .andExpect(status().is(201));

        //userLogin api
        RequestBuilder userLogin_RequestBuilder = MockMvcRequestBuilders
                .post("/userLogin")
                .with(httpBasic("csrfTest@gmail.com", "csrfTest"));

        mockMvc.perform(userLogin_RequestBuilder)
                .andExpect(status().is(200));
    }
}

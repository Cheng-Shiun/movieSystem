package com.chengshiun;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SubscribeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void subscribe_success_withNormalMember() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", 2);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/subscribe")
                .session(session)
                .with(httpBasic("normal@gmail.com", "normal"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));

        //訂閱成功後是否可以觀看 vip 電影
        RequestBuilder vipRequestBuilder = MockMvcRequestBuilders
                .post("/watchVipMovie")
                .session(session)
                .with(csrf());

        mockMvc.perform(vipRequestBuilder)
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void unsubscribe_success_withVipMember() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", 3);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/unsubscribe")
                .session(session)
                .with(httpBasic("vip@gmail.com", "vip"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));

        //取消訂閱後是否可以觀看 vip 電影
        RequestBuilder vipRequestBuilder = MockMvcRequestBuilders
                .post("/watchVipMovie")
                .session(session)
                .with(csrf());

        mockMvc.perform(vipRequestBuilder)
                .andExpect(status().is(403));
    }
}
